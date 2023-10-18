import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.Base64;
import java.util.Locale;

public class TestDataTypes {

    public static String getHexRepresentation(byte[] array)
    {
        String output = "";
        for(byte value : array)
        {
            output += String.format("0x%02x ", value);
        }
        return output;
    }

    public static String getHex(byte[] array)
    {
        String output = "";
        for(byte value : array)
        {
            output += String.format("%02x", value);
        }
        return output;
    }

    public static void main(String args[]) {
        //data types - java does not have unsigned data types
        //1st bit is the sign
        //int = 32b value / 4B value
        //2digits = 1B
        int intValue = 1000000;
        byte byteValue = 20; // = char in C/C++
        char charValue = 92; // 2B value

        System.out.println("The integer value is " + intValue);
        System.out.println(String.format("The integer value is %d and its hexa representation is %x", intValue, intValue)); //%d decimal; %x hexa
        System.out.println(String.format("The hex value is 0x%08x", intValue)); //08x represents every B
        System.out.println(String.format("The hex value of the byte 0x%02x", byteValue)); //08x represents every B

        //initialization in hexa
        int initialValue = 0x1A56; //init w 2B; = 00001A56
        byte initialByte = 0x23;

        System.out.println(String.format("Initial int = %d and initial byte = %d", initialValue, initialByte));

        byte initialBinaryByte = (byte) 0b10010011;//sign bit = 1 => negative
        System.out.println("Initial binary value is " + initialBinaryByte);

        byte oneBytePassword = 'a';
        System.out.printf("The one byte password is %d \n", oneBytePassword);
        System.out.printf("The one byte password is 0x%x \n", oneBytePassword);

        //init the 1B pass with the 5th bit from right to left with 1
        //the counting starts w 1
        oneBytePassword = 16;
        oneBytePassword = 0x10; //0001 = 1 0000 = 0
        oneBytePassword = 0b00010000;
        oneBytePassword = 1 << 4; // 1 = 00000001 << 4 => 0001 0000 // x2^4 (x16)
        //multiplication = shift to left
        //division = shift to right

        //init the B with the next configuration 1010 1010
        oneBytePassword = (byte) 170;
        oneBytePassword = (byte) 0xAA;
        oneBytePassword = (byte) 0b10101010;
        oneBytePassword = (byte) (1 << 7 | 1 << 5 | 1 << 3 | 1); // |(or) = addition
        oneBytePassword = (byte) (1 << 7 | 1 << 5 | 1 << 3 | 1 << 1); // |(or) = addition

        //checking a bit value
        oneBytePassword = (byte) 0b10001000;
        //check if the 4th bit from right to left is 1
        //define a mask
        byte checkMask = (byte) 1 << 3; //0000 1000
        if ((oneBytePassword & checkMask) != 0) {
            System.out.println("The 4th bit is 1");
        } else {
            System.out.println("The 4th bit is 0");
        }

        //password as a byte array
        String password = "password";
        byte[] bytePassword = password.getBytes();
        System.out.println("The size of the password byte array is "+bytePassword.length);
        //get a string out of a byte array
        String passwordCopy = new String(bytePassword);
        System.out.println("The password copy is " + passwordCopy);

        //password as a hexa array
        byte[] byteArray = {(byte)1<<3, (byte) 0xAA, (byte) 0b10001000, (byte)1<<5};
        System.out.println("Byte array = " + getHexRepresentation(byteArray));
        System.out.println("Byte array = " + getHex(byteArray).toUpperCase());

        //Base64 ENCODING (NOT encryption)
        oneBytePassword = 0b00000111;
        System.out.println(String.format("The char value is %c", oneBytePassword));
        oneBytePassword = 0b000001110;
        System.out.println(String.format("The char value is %c", oneBytePassword));
        //DON'T CONVERT BYTES TO STRINGS => the values will be lost

        String receivedPassword = new String(byteArray);
        System.out.println("Received password " + receivedPassword);
        System.out.println("Received password in bytes is " + getHex(receivedPassword.getBytes()));

        //BASE64 takes 6 bits from the start => ASCII chars
        //the last 2 bits of a byte are grouped with the next 4 bits from the next byte
        //byte array => string
        // base64 trail/signature: ==
        String base64Password = Base64.getEncoder().encodeToString(byteArray);
        System.out.println("Base64 encoding of the password is: "+base64Password);

        byte[] reversePassword = Base64.getDecoder().decode(base64Password);
        System.out.println("Reversed password is  " + getHexRepresentation(reversePassword));

        //printing the reference value - useless
        System.out.println(reversePassword);
    }
}

