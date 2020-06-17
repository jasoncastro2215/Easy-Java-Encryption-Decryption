# Command line arguments
### Options:
### 1. -alg [argument] - the prefered encryption/decryption algorithm, argument is either "shift" or "unicode".
### 2. -mode [argument] - encrypt or decrypt, argument is either "enc" or "dec".
### 3. -key [argument] - how many step to move used by the encryption algorithm, argument is integer.
### 4. -data [argument] - the text to be encrypted.
### 5. -in [argument] - if you want to encrypt/decrypt data on a file, must be 1 line, if -data and -in is present, the program will choose -data, argument is the path of the file.
### 6. -out [argument] - encrypted/decrypted text will be saved on a file, if not declared the encrypted/decrypted text will be shown in the console.

# Installation
### Java 11 is installed in my computer

# How to run

### open terminal
### go to project path
### type:
### java Encryption.java -alg shift -mode enc -key 5 -data Encrypt this text -in file.txt -out file.txt