import java.util.Base64;

public class Main {

    public static String getHexRepresentation(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("0x%02x ", value);
        }
        return output;
    }

    public static String getHex(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("%02x", value);
        }
        return output;
    }

    public static void main(String[] args) {

        //------------------------------------------------
        //data types
        int intValue = 1000000;
        byte byteValue = 20;
        //2 byte value
        char charValue = 92;

//        System.out.println("The integer value " + intValue);
//        System.out.println(String.format("The integer value is %d and its hexa representation is %x", intValue, intValue));
//        System.out.println(String.format("The hex value is 0x%08x", intValue));
//        System.out.println(String.format("The hex value of the byte is 0x%02x", byteValue));

        System.out.println(String.format("\nInteger value is %d and hex value is 0x%X", intValue, intValue));

        System.out.println("---------------------------------------------------");
        //------------------------------------------------
        //lucru pe biti

        int initialValue = 0x1A56; //este scris normal, nu little endian sau ceva
        byte initialByte = 0x23;
        System.out.println(String.format("Initial int = %d and initial byte = %d", initialValue, initialByte));
        byte initialBinaryByte = (byte) 0b10010011; //the first bit este bit de semn, take care
        System.out.println("Initial binary value is " + initialBinaryByte);

        byte oneBytePassword = 'a';
        System.out.printf("The one byte password is %d\n", oneBytePassword); //97
        System.out.printf("The one byte password is 0x%X\n", oneBytePassword); //0x61

        //init the 1 byte password with the 5th bit from right to left with 1 and the other bits 0
        oneBytePassword = 16;
        oneBytePassword = 0x10;
        oneBytePassword = 0b00010000;
        oneBytePassword = 1 << 4;        //1 = 00000001 << 4 -> 00010000

        //init the byte with the next configuration 10101010
        oneBytePassword = (byte) 170;
        oneBytePassword = (byte) 0xAA;
        oneBytePassword = (byte) 0b10101010;
        oneBytePassword = (byte) (1 << 7 | 1 << 5 | 1 << 3 | 1 << 1);

        //checking a bit value
        oneBytePassword = (byte) 0b10001000;
        //check if the 4th bit from right to left is 1
        //define a mask
        byte checkMask = (byte) 1 << 3;
        if ((oneBytePassword & checkMask) != 0) {
            System.out.println("The 4th bit is 1");
        } else {
            System.out.println("The 4th bit is 0");
        }

        String password = "password";
        byte[] bytePassword = password.getBytes();
        System.out.println("The size of the password byte array " + bytePassword.length); //8

        for (byte b : bytePassword) {
            System.out.println(b);
        } //imi afiseaza gen 112 - p; 97 - a
        for (byte b : bytePassword) {
            System.out.printf("%X ", b);
        } //imi afiseaza in hexa gen 70 - 112 - p
        for (byte b : bytePassword) {
            System.out.printf("%b ", b);
        } //imi afiseaza in hexa gen 70 - 112 - p

        System.out.println();

        //get the string out of a byte array
        //it will build the string again
        String passwordCopy = new String(bytePassword);
        System.out.println("The password copy is " + passwordCopy);


        //lucru cu biti - base64
        byte[] byteArray = {(byte) 1 << 3, (byte) 0xAA, (byte) 0b10001000, (byte) 1 << 5};

        //now we can see any byte array to hexa representation
        System.out.println("Byte array = " + getHexRepresentation(byteArray));
        System.out.println("Byte array = " + getHex(byteArray).toUpperCase());

        //when you want to store binary data(value) as strings
        //if we have a password at bit level, dont convert it to string, you will lose values (some weird conversation)
        //so lets use Base64 encoding (CKqIIA== este specific base64 acele egaluri de la final?)
        oneBytePassword = 0b00000111;
        System.out.println(String.format("The char value is %c", oneBytePassword)); //%c - character
        oneBytePassword = 0b00000110;
        System.out.println(String.format("The char value is %c", oneBytePassword)); //nu sunt printable

        //Do NOT do it like this
        //Don't convert them to strings and back
        String receivedPassword = new String(byteArray);
        System.out.println("Received password " + receivedPassword);
        //you can get a different value in binary
        System.out.println("Received password in bytes is " + getHexRepresentation(receivedPassword.getBytes()));

        System.out.println("##########################################");

        //DO it like this
        //it will not lose our data
        String base64Password = Base64.getEncoder().encodeToString(byteArray);
        System.out.println("Base64 encoding of the password is " + base64Password);
        byte[] reversePassword = Base64.getDecoder().decode(base64Password);
        System.out.println("Reversed password is " + getHexRepresentation(reversePassword));

        //the binary password value

        //printing the reference value - useless
        System.out.println(reversePassword);



        //daca vrei sa stochezi, nu stoca in String, e vorba strict de stocare
        //nu in string ca nu toate caracterele sunt printable
        //pot string, doar cand stiu ca am coduri ascii acolo

        //e ok sa fac aia cu string, doar cand am caractere printabile, nu bytes enpulea
    }
}
