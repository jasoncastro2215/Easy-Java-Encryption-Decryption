import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Encryption {
    public static void main(String[] args) {
        String fileOutput = "";
        String operation = "enc";
        String strInput = "";
        String fileInput = "";
        String algorithm = "shift";
        int num = 0;
        boolean is_data = false;

        for (int i = 0; i < args.length; i++) {
            if (!is_data) {
                if (args[i].equals("-mode")) {
                    if (args[i+1].equals("dec"))
                        operation = "dec";
                } else if (args[i].equals("-key")) {
                    num = Integer.parseInt(args[i+1]);
                } else if (args[i].equals("-data")) {
                    is_data = true;
                } else if (args[i].equals("-in")) {
                    fileInput = args[i+1];
                } else if (args[i].equals("-out")) {
                    fileOutput = args[i+1];
                } else if (args[i].equals("-alg")) {
                    algorithm = args[i+1];
                }
            } else {
                if (i == args.length-1 || args[i+1].matches("-mode|-key|-in|-out|-alg")) {
                    strInput += args[i];
                    is_data = false;
                    continue;
                }
                strInput += args[i] + " ";
            }
        }
        if (strInput.isEmpty() && !fileInput.isEmpty()) {
            File file = new File(fileInput);

            try (Scanner scanner = new Scanner(file)) {
                strInput = scanner.nextLine();
            } catch (FileNotFoundException e) {
                System.out.println("Error");
            }
        }
        StringBuilder sbInput = new StringBuilder(strInput);
        InitAlgo initalgo = new InitAlgo();
        Algo algo = initalgo.assignAlgo(algorithm);
        algo.performOperation(operation, strInput, sbInput, num, fileOutput);
    }
}

abstract class Algo {
    protected String strInput;
    protected StringBuilder sbInput;
    protected int num;
    protected String fileOutput;

    public void performOperation(String operation, String strInput, StringBuilder sbInput, int num, String fileOutput) {
        this.strInput = strInput;
        this.sbInput = sbInput;
        this.num = num;
        this.fileOutput = fileOutput;

        switch (operation){
            case "enc":
                encrypt();
                break;
            case "dec":
                decrypt();
                break;
        }
    }

    protected void output() {
        if (fileOutput.isEmpty()) {
            System.out.println(sbInput.toString());
            return;
        }
        File file = new File(fileOutput);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(sbInput.toString());
        } catch (IOException e) {
            System.out.printf("Error");
        }
    }

    protected void encrypt(){};
    protected void decrypt(){};
}

class ShiftMethod extends Algo {

    protected void encrypt() {
        for (int i = 0; i < strInput.length(); i++) {
            char c = strInput.charAt(i);
            String ch = Character.toString(c);
            boolean is_upper = false;
            if (ch.matches("[a-zA-Z]")) {
                is_upper = ch.equals(ch.toUpperCase());
                if (is_upper) {
                    c = ch.toLowerCase().charAt(0);
                }
                if ((int) c + num > 122) {
                    if (is_upper)
                        sbInput.setCharAt(i, Character.toString((char) (num+(int) c-122+97-1))
                                .toUpperCase().charAt(0));
                    else
                        sbInput.setCharAt(i, (char) (num+(int) c-122+97-1));
                } else {
                    if (is_upper)
                        sbInput.setCharAt(i, Character.toString((char) ((int) c + num))
                                .toUpperCase().charAt(0));
                    else
                        sbInput.setCharAt(i, (char) ((int) c + num));
                }
            }
        }
        output();
    }

    protected void decrypt() {
        for (int i = 0; i < strInput.length(); i++) {
            char c = strInput.charAt(i);
            String ch = Character.toString(c);
            boolean is_upper = false;
            if (ch.matches("[a-zA-Z]")) {
                is_upper = ch.equals(ch.toUpperCase());
                if (is_upper) {
                    c = ch.toLowerCase().charAt(0);
                }
                if ((int) c - num < 97) {
                    if (is_upper)
                        sbInput.setCharAt(i, Character.toString((char) ((int) c-num+122-97+1))
                                .toUpperCase().charAt(0));
                    else
                        sbInput.setCharAt(i, (char) ((int) c-num+122-97+1));
                } else {
                    if (is_upper)
                        sbInput.setCharAt(i, Character.toString((char) ((int) c - num))
                                .toUpperCase().charAt(0));
                    else
                        sbInput.setCharAt(i, (char) ((int) c - num));
                }
            }
        }
        output();
    }
}

class UnicodeMethod extends Algo {

    protected void encrypt() {
        for (int i = 0; i < strInput.length(); i++) {
            char c = strInput.charAt(i);
            sbInput.setCharAt(i, (char) ((int) c + num));
        }
        output();
    }

    protected void decrypt() {
        for (int i = 0; i < strInput.length(); i++) {
            char c = strInput.charAt(i);
            sbInput.setCharAt(i, (char) ((int) c - num));
        }
        output();
    }
}

abstract class RetAlgo {

    abstract Algo assignAlgo(String type);

    Algo orderTable(String type) throws InterruptedException {
        Algo algo = assignAlgo(type);
        if (algo == null) {
            System.out.println("Sorry, we are not able to determine the algorithm you inputted\n");
            return null;
        }
        return algo;
    }
}

class InitAlgo extends RetAlgo {
    @Override
    Algo assignAlgo(String type) {
        if (type.equals("shift")) {
            return new ShiftMethod();
        } else if (type.equals("unicode")) {
            return new UnicodeMethod();
        } else return null;
    }
}

