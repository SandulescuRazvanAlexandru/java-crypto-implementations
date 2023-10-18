import java.util.Base64;

public class Main {
    public static String getHexRepresentation(byte[] array) {
        String output = "";
        for(byte value : array) {
            output += String.format("0x%02x ", value);
        }
        return output;
    }

    public static String getHex(byte[] array) {
        String output = "";
        for(byte value : array) {
            output += String.format("%02x", value);
        }
        return output;
    }

    public static void main(String[] args) {

        //data types

        int intValue = 1000000;
        byte byteValue = 20;

        //2 byte value
        char charValue = 92;


        //for Eclipse you have the syso  + Ctrl Space shortcut
        System.out.println("The integer value " + intValue);
        System.out.println(String.format("The integer value is %d and its hexa representation is %x", intValue, intValue));
        System.out.println(String.format("The hex value is 0x%08x", intValue));
        System.out.println(String.format("The hex value of the byte is 0x%02x, si valoarea int: %d", byteValue, byteValue));

        System.out.println();

        int initialValue = 0x1A56;
        byte initialByte = 0x23;

        System.out.println(String.format("Initial int = %d and initial byte = %d", initialValue, initialByte));

        byte initialBinaryByte = (byte) 0b10010011;
        System.out.println("Initial binary value is " + initialBinaryByte);


        byte oneBytePassword = 'a';
        System.out.printf("\nThe one byte password is %d", oneBytePassword);
        System.out.printf("\nThe one byte password is 0x%x \n\n", oneBytePassword);

        //init the 1 byte password with the 5th bit from right to left with 1 and the other bits 0
        oneBytePassword = 16;
        oneBytePassword = 0x10;
        oneBytePassword = 0b00010000;
        oneBytePassword = 1 << 4;		//1 = 00000001 << 4 -> 00010000

        //init the byte with the next configuration 10101010
        oneBytePassword = (byte) 170;
        oneBytePassword = (byte) 0xAA;
        oneBytePassword = (byte) 0b10101010;
        oneBytePassword = (byte) (1 << 7 | 1 << 5 | 1 << 3 | 1 << 1);

        //checking a bit value
        oneBytePassword = (byte)0b10001000;
        //check if the 4th bit from right to left is 1
        //define a mask
        byte checkMask = (byte) 1 << 3;
        if((oneBytePassword & checkMask) != 0) {
            System.out.println("The 4th bit is 1");
        }
        else {
            System.out.println("The 4th bit is 0");
        }

        System.out.println("===============================================");
        String password = "password";
        byte[] bytePassword = password.getBytes();
        System.out.println("bytePassword array = " + getHexRepresentation(bytePassword));
        System.out.println("The size of the password byte array " + bytePassword.length);

        String passwordCopy = new String(bytePassword);
        System.out.println("The password copy is " + passwordCopy);

        byte[] byteArray = { (byte)1<<3, (byte) 0xAA, (byte) 0b10001000, (byte) 1<<5 };

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        for(byte value : byteArray) {
            System.out.println(value);
        }

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        System.out.println("Byte array = " + getHexRepresentation(byteArray));
        System.out.println("Byte array = " + getHex(byteArray).toUpperCase());


        //Base64 encoding FOR BINARY (ONLY USED FOR BINARY)
        oneBytePassword = 0b00000111;
        System.out.println(String.format("The char value is %c", oneBytePassword));
        oneBytePassword = 0b00000110;
        System.out.println(String.format("The char value is %c", oneBytePassword));

        System.out.println("---------");
        //Don't convert them to strings and back
        String receivedPassword = new String(byteArray);
        System.out.println("Received password " + receivedPassword);
        //you can get a different value in binary
        System.out.println("Received password in bytes is " + getHexRepresentation(receivedPassword.getBytes()));
        System.out.println("---------");


        String base64Password = Base64.getEncoder().encodeToString(byteArray);
        System.out.println("Base64 encoding of the password is " + base64Password);

        byte[] reversePassword = Base64.getDecoder().decode(base64Password);
        System.out.println("Reversed password is " +
                getHexRepresentation(reversePassword));

        //printing the reference value - useless
        System.out.println(reversePassword);

    }
}
