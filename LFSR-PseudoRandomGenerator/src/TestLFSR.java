import java.nio.charset.StandardCharsets;

public class TestLFSR {
//LFSR - shift register that takes a linear function of aprevious state as an input
    // taps = the bits that affect the state

    public static String getHex(byte[] array) {
        String output = "";
        for(byte value : array) {
            output += String.format("%02x", value);
        }
        return output;
    }

    public static byte shiftAndInsertRegister(byte[] register, byte nextBit)
    {
        //get the last bit from the last B = the random one
        //byte bit1Mask = 0b00000001; = 1
        byte randomBit = (byte) ((1 & register[3]));

        //get thhe last bit from each byte of the register
        byte lastBitFrom1stByte = (byte) ((1 & register [0]));
        byte lastBitFrom2ndByte = (byte) ((1 & register [1]));
        byte lastBitFrom3rdByte = (byte) ((1 & register [2]));

        //shift the register
        register[0] = (byte) (register[0] >> 1);
        register[1] = (byte) (register[1] >> 1);
        register[2] = (byte) (register[2] >> 1);
        register[3] = (byte) (register[3] >> 1);

        //insert the prev byte bit into the next byte w OR
        register[0] = (byte) (register[0] | (nextBit << 7));
        register[1] = (byte) (register[1] | (lastBitFrom1stByte << 7));
        register[2] = (byte) (register[2] | (lastBitFrom2ndByte << 7));
        register[3] = (byte) (register[3] | (lastBitFrom3rdByte << 7));

        return randomBit;
    }

    public static byte[] getPseudoRandomValues(String password, int noBytes) {

        byte[] result = new byte[noBytes];

       //the pass must have the required size
        if (password.length() < 4) {
            return null;
        }

        //get the no of bytes
        byte[] initialSeed = password.getBytes();

        System.out.println("initial seed:  " + initialSeed);

        byte[] register = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            register[i] = initialSeed[i];
        }

        System.out.println(getHex(register));
        byte randomByte = 0;

        //we do an extra 4 iteration to remove the initial seed

        for(int j = 0; j < noBytes; j++) {
            for (int i = 0; i < 8; i++) {
                //we get a random byte
                //we need to check 7 bits
                byte bit1Mask = 0b00000001; //1
                byte bit2Mask = 0b00000010; //2// (1<<1)
                byte bit3Mask = 0b00000100; // 2^2 // (1<<2)
                byte bit6Mask = 0b00100000; //1<<5
                byte bit8Mask = (byte) (1 << 7); //0b1000 0000
                byte firstByte8bitMask = (byte) 0b10000000;

                //get the value of x^31 -> the 8th bit(right to left) in the first byte of the register
                //right shift with 7 so the 1/0 will go to the last position
                byte bit32Value = (byte) ((firstByte8bitMask & register[0]) >> 7);
                byte bit8Value = (byte) ((bit8Mask & register[3]) >> 7);
                byte bit6Value = (byte) ((bit6Mask & register[3]) >> 5);
                byte bit3Value = (byte) ((bit3Mask & register[3]) >> 2);
                byte bit2Value = (byte) ((bit2Mask & register[3]) >> 1);
                byte bit1Value = (byte) ((bit1Mask & register[3]));

                //get the pseudo random bit
                //the random bit is the result of the polynom; it will be 8th bit from left to right in the lfsr
                //the lfsr will shift and the bit that gets out is actually the random bit
                //after 32 iterations,the next bit will become the pseudo random bit
                byte nextBit = (byte) (bit32Value ^ bit8Value ^ bit6Value ^ bit3Value ^ bit2Value ^ bit1Value);

                //thr bit1Value will be the random bit because it will get out first
                //byte randomBit = (byte) ((bit1Mask&register[3]));

                System.out.println("Before shift: " + getHex(register));
                byte randomBit = shiftAndInsertRegister(register, nextBit);
                System.out.println("After shift: " + getHex(register));

                randomByte = (byte) ((randomByte += randomBit) << 1);
            }
            // ------------------
            //			if(j >= 4) {
            //				result[j-4] = randomByte;
            //			}
            result[j] = randomByte;
        }

                return result;
    }


    public static void main(String[]args)
    {
        String pass = "1234";
        byte[] random = getPseudoRandomValues(pass, 8);
        System.out.println(getHex(random));
    }
}
