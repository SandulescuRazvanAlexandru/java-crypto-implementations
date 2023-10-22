public class Main {

    public static String getHex(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("%02x", value);
        }
        return output;
    }

    public static byte shiftAndInsert_1(byte[] register, byte nextBit) {

        byte randomBit = (byte) ((1 << 5) & register[2]);

        byte lastBitFrom1stByte = (byte) (1 & register[0]);
        byte lastBitFrom2ndByte = (byte) (1 & register[1]);

        register[0] = (byte) ((register[0] >>> 1)& 0b01111111);
        register[1] = (byte) ((register[1] >>> 1)& 0b01111111);
        register[2] = (byte) ((register[2] >>> 1)& 0b01111111);

        register[0] = (byte) (register[0] | (nextBit << 7));
        register[1] = (byte) (register[1] | (lastBitFrom1stByte << 7));
        register[2] = (byte) (register[1] | (lastBitFrom2ndByte << 7));

        return randomBit;
    }

    public static byte shiftAndInsert_2(byte[] register, byte nextBit) {

        byte randomBit = (byte) ((1 << 2) & register[2]);

        byte lastBitFrom1stByte = (byte) (1 & register[0]);
        byte lastBitFrom2ndByte = (byte) (1 & register[1]);

        register[0] = (byte) ((register[0] >>> 1)& 0b01111111);
        register[1] = (byte) ((register[1] >>> 1)& 0b01111111);
        register[2] = (byte) ((register[2] >>> 1)& 0b01111111);

        register[0] = (byte) (register[0] | (nextBit << 7));
        register[1] = (byte) (register[1] | (lastBitFrom1stByte << 7));
        register[2] = (byte) (register[1] | (lastBitFrom2ndByte << 7));

        return randomBit;
    }

    public static byte shiftAndInsert_3(byte[] register, byte nextBit) {

        byte randomBit = (byte) ((1 << 1) & register[2]);

        byte lastBitFrom1stByte = (byte) (1 & register[0]);
        byte lastBitFrom2ndByte = (byte) (1 & register[1]);

        register[0] = (byte) ((register[0] >>> 1)& 0b01111111);
        register[1] = (byte) ((register[1] >>> 1)& 0b01111111);
        register[2] = (byte) ((register[2] >>> 1)& 0b01111111);

        register[0] = (byte) (register[0] | (nextBit << 7));
        register[1] = (byte) (register[1] | (lastBitFrom1stByte << 7));
        register[2] = (byte) (register[1] | (lastBitFrom2ndByte << 7));

        return randomBit;
    }

    public static byte[] getPseudoRandomBytes(String password, int numberOfBytesRequired) {

        byte[] result = new byte[numberOfBytesRequired];

        if (password.length() < 8) {
            return null;
        }

        byte[] initialSeed = password.getBytes();

        byte[] register1 = new byte[3];
        register1[0] = initialSeed[0];
        register1[1] = initialSeed[1];

        byte _1BitFrom3thByteInitialSeed = (byte) ((1 << 7) & initialSeed[2]);
        byte _2BitFrom3thByteInitialSeed = (byte) ((1 << 6) & initialSeed[2]);
        byte _3BitFrom3thByteInitialSeed = (byte) ((1 << 5) & initialSeed[2]);
        register1[2] = (byte) (register1[2] | (_1BitFrom3thByteInitialSeed << 7));
        register1[2] = (byte) (register1[2] | (_2BitFrom3thByteInitialSeed << 6));
        register1[2] = (byte) (register1[2] | (_3BitFrom3thByteInitialSeed << 5));

        byte[] register2 = new byte[3];

        byte _4BitFrom3thByteInitialSeed = (byte) ((1 << 4) & initialSeed[2]);
        byte _5BitFrom3thByteInitialSeed = (byte) ((1 << 3) & initialSeed[2]);
        byte _6BitFrom3thByteInitialSeed = (byte) ((1 << 2) & initialSeed[2]);
        byte _7BitFrom3thByteInitialSeed = (byte) ((1 << 1) & initialSeed[2]);
        byte _8BitFrom3thByteInitialSeed = (byte) (1 & initialSeed[2]);
        register2[0] = (byte) (register2[0] | (_4BitFrom3thByteInitialSeed << 7));
        register2[0] = (byte) (register2[0] | (_5BitFrom3thByteInitialSeed << 6));
        register2[0] = (byte) (register2[0] | (_6BitFrom3thByteInitialSeed << 5));
        register2[0] = (byte) (register2[0] | (_7BitFrom3thByteInitialSeed << 4));
        register2[0] = (byte) (register2[0] | (_8BitFrom3thByteInitialSeed << 3));

        byte _1bitFrom4thByteInitialSeed = (byte) ((1 << 7) & initialSeed[3]);
        byte _2bitFrom4thByteInitialSeed = (byte) ((1 << 6) & initialSeed[3]);
        byte _3bitFrom4thByteInitialSeed = (byte) ((1 << 5) & initialSeed[3]);
        register2[0] = (byte) (register2[0] | (_1bitFrom4thByteInitialSeed << 2));
        register2[0] = (byte) (register2[0] | (_2bitFrom4thByteInitialSeed << 1));
        register2[0] = (byte) (register2[0] | _3bitFrom4thByteInitialSeed);

        byte _4bitFrom4thByteInitialSeed = (byte) ((1 << 4) & initialSeed[3]);
        byte _5bitFrom4thByteInitialSeed = (byte) ((1 << 3) & initialSeed[3]);
        byte _6bitFrom4thByteInitialSeed = (byte) ((1 << 2) & initialSeed[3]);
        byte _7bitFrom4thByteInitialSeed = (byte) ((1 << 1) & initialSeed[3]);
        byte _8bitFrom4thByteInitialSeed = (byte) (1 & initialSeed[3]);
        register2[1] = (byte) (register2[1] | (_4bitFrom4thByteInitialSeed << 7));
        register2[1] = (byte) (register2[1] | (_5bitFrom4thByteInitialSeed << 6));
        register2[1] = (byte) (register2[1] | (_6bitFrom4thByteInitialSeed << 5));
        register2[1] = (byte) (register2[1] | (_7bitFrom4thByteInitialSeed << 4));
        register2[1] = (byte) (register2[1] | (_8bitFrom4thByteInitialSeed << 3));

        byte _1bitFrom5thByteInitialSeed = (byte) ((1 << 7) & initialSeed[4]);
        byte _2bitFrom5thByteInitialSeed = (byte) ((1 << 6) & initialSeed[4]);
        byte _3bitFrom5thByteInitialSeed = (byte) ((1 << 5) & initialSeed[4]);
        register2[1] = (byte) (register2[1] | (_1bitFrom5thByteInitialSeed << 2));
        register2[1] = (byte) (register2[1] | (_2bitFrom5thByteInitialSeed << 1));
        register2[1] = (byte) (register2[1] | _3bitFrom5thByteInitialSeed);

        byte _4bitFrom5thByteInitialSeed = (byte) ((1 << 4) & initialSeed[4]);
        byte _5bitFrom5thByteInitialSeed = (byte) ((1 << 3) & initialSeed[4]);
        byte _6bitFrom5thByteInitialSeed = (byte) ((1 << 2) & initialSeed[4]);
        byte _7bitFrom5thByteInitialSeed = (byte) ((1 << 1) & initialSeed[4]);
        byte _8bitFrom5thByteInitialSeed = (byte) (1 & initialSeed[4]);
        register2[2] = (byte) (register2[2] | (_4bitFrom5thByteInitialSeed << 7));
        register2[2] = (byte) (register2[2] | (_5bitFrom5thByteInitialSeed << 6));
        register2[2] = (byte) (register2[2] | (_6bitFrom5thByteInitialSeed << 5));
        register2[2] = (byte) (register2[2] | (_7bitFrom5thByteInitialSeed << 4));
        register2[2] = (byte) (register2[2] | (_8bitFrom5thByteInitialSeed << 3));

        byte _1bitFrom6thByteInitialSeed = (byte) ((1 << 7) & initialSeed[5]);
        register2[2] = (byte) (register2[2] | (_1bitFrom6thByteInitialSeed << 2));

        byte _2bitFrom6thByteInitialSeed = (byte) ((1 << 6) & initialSeed[5]);
        byte _3bitFrom6thByteInitialSeed = (byte) ((1 << 5) & initialSeed[5]);
        byte _4bitFrom6thByteInitialSeed = (byte) ((1 << 4) & initialSeed[5]);
        byte _5bitFrom6thByteInitialSeed = (byte) ((1 << 3) & initialSeed[5]);
        byte _6bitFrom6thByteInitialSeed = (byte) ((1 << 2) & initialSeed[5]);
        byte _7bitFrom6thByteInitialSeed = (byte) ((1 << 1) & initialSeed[5]);
        byte _8bitFrom6thByteInitialSeed = (byte) (1 & initialSeed[5]);

        byte[] register3 = new byte[3];

        register3[0] = (byte) (register3[0] | (_2bitFrom6thByteInitialSeed << 7));
        register3[0] = (byte) (register3[0] | (_3bitFrom6thByteInitialSeed << 6));
        register3[0] = (byte) (register3[0] | (_4bitFrom6thByteInitialSeed << 5));
        register3[0] = (byte) (register3[0] | (_5bitFrom6thByteInitialSeed << 4));
        register3[0] = (byte) (register3[0] | (_6bitFrom6thByteInitialSeed << 3));
        register3[0] = (byte) (register3[0] | (_7bitFrom6thByteInitialSeed << 2));
        register3[0] = (byte) (register3[0] | (_8bitFrom6thByteInitialSeed << 1));

        byte _1bitFrom7thByteInitialSeed = (byte) ((1 << 7) & initialSeed[6]);
        register3[0] = (byte) (register3[0] | _1bitFrom7thByteInitialSeed);

        byte _2bitFrom7thByteInitialSeed = (byte) ((1 << 6) & initialSeed[6]);
        byte _3bitFrom7thByteInitialSeed = (byte) ((1 << 5) & initialSeed[6]);
        byte _4bitFrom7thByteInitialSeed = (byte) ((1 << 4) & initialSeed[6]);
        byte _5bitFrom7thByteInitialSeed = (byte) ((1 << 3) & initialSeed[6]);
        byte _6bitFrom7thByteInitialSeed = (byte) ((1 << 2) & initialSeed[6]);
        byte _7bitFrom7thByteInitialSeed = (byte) ((1 << 1) & initialSeed[6]);
        byte _8bitFrom7thByteInitialSeed = (byte) (1 & initialSeed[6]);
        register3[1] = (byte) (register3[1] | (_2bitFrom7thByteInitialSeed << 7));
        register3[1] = (byte) (register3[1] | (_3bitFrom7thByteInitialSeed << 6));
        register3[1] = (byte) (register3[1] | (_4bitFrom7thByteInitialSeed << 5));
        register3[1] = (byte) (register3[1] | (_5bitFrom7thByteInitialSeed << 4));
        register3[1] = (byte) (register3[1] | (_6bitFrom7thByteInitialSeed << 3));
        register3[1] = (byte) (register3[1] | (_7bitFrom7thByteInitialSeed << 2));
        register3[1] = (byte) (register3[1] | (_8bitFrom7thByteInitialSeed << 1));

        byte _1bitFrom8thByteInitialSeed = (byte) ((1 << 7) & initialSeed[7]);
        register3[1] = (byte) (register3[1] | _1bitFrom8thByteInitialSeed);

        byte _2bitFrom8thByteInitialSeed = (byte) ((1 << 6) & initialSeed[7]);
        byte _3bitFrom8thByteInitialSeed = (byte) ((1 << 5) & initialSeed[7]);
        byte _4bitFrom8thByteInitialSeed = (byte) ((1 << 4) & initialSeed[7]);
        byte _5bitFrom8thByteInitialSeed = (byte) ((1 << 3) & initialSeed[7]);
        byte _6bitFrom8thByteInitialSeed = (byte) ((1 << 2) & initialSeed[7]);
        byte _7bitFrom8thByteInitialSeed = (byte) ((1 << 1) & initialSeed[7]);
        byte _8bitFrom8thByteInitialSeed = (byte) (1 & initialSeed[7]);
        register3[2] = (byte) (register3[2] | (_2bitFrom8thByteInitialSeed << 7));
        register3[2] = (byte) (register3[2] | (_3bitFrom8thByteInitialSeed << 6));
        register3[2] = (byte) (register3[2] | (_4bitFrom8thByteInitialSeed << 5));
        register3[2] = (byte) (register3[2] | (_5bitFrom8thByteInitialSeed << 4));
        register3[2] = (byte) (register3[2] | (_6bitFrom8thByteInitialSeed << 3));
        register3[2] = (byte) (register3[2] | (_7bitFrom8thByteInitialSeed << 2));
        register3[2] = (byte) (register3[2] | (_8bitFrom8thByteInitialSeed << 1));

        for (int j = 0; j < numberOfBytesRequired; j++) {
            byte randomByte = 0;
            for (int i = 0; i < 8; i++) {
                byte bit13MaskRegister1 = 1 << 3;
                byte bit16MaskRegister1 = 1;
                byte bit17MaskRegister1 = (byte) (1 << 7);
                byte bit18MaskRegister1 = 1 << 6;
                byte bit13ValueRegister1 = (byte) (((bit13MaskRegister1 & register1[1]) >>> 3)& 0b01111111);
                byte bit16ValueRegister1 = (byte) (bit16MaskRegister1 & register1[1]);
                byte bit17ValueRegister1 = (byte) (((bit17MaskRegister1 & register1[2]) >> 7)& 0b01111111);
                byte bit18ValueRegister1 = (byte) (((bit18MaskRegister1 & register1[2]) >> 6)& 0b01111111);
                byte nextBit1 = (byte) (bit13ValueRegister1 ^ bit16ValueRegister1 ^ bit17ValueRegister1 ^ bit18ValueRegister1);

                byte bit20MaskRegister2 = 1 << 4;
                byte bit21MaskRegister2 = 1 << 3;
                byte bit20ValueRegister2 = (byte) (((bit20MaskRegister2 & register2[2]) >> 4)& 0b01111111);
                byte bit21ValueRegister2 = (byte) (((bit21MaskRegister2 & register2[2]) >> 3)& 0b01111111);
                byte nextBit2 = (byte) (bit20ValueRegister2 ^ bit21ValueRegister2);

                byte bit7MaskRegister3 = 1 << 1;
                byte bit20MaskRegister3 = 1 << 4;
                byte bit21MaskRegister3 = 1 << 3;
                byte bit22MaskRegister3 = 1 << 2;
                byte bit7ValueRegister3 = (byte) (((bit7MaskRegister3 & register3[0]) >> 1)& 0b01111111);
                byte bit20ValueRegister3 = (byte) (((bit20MaskRegister3 & register3[2]) >> 4)& 0b01111111);
                byte bit21ValueRegister3 = (byte) (((bit21MaskRegister3 & register3[2]) >> 3)& 0b01111111);
                byte bit22ValueRegister3 = (byte) (((bit22MaskRegister3 & register3[2]) >> 2)& 0b01111111);
                byte nextBit3 = (byte) (bit7ValueRegister3 ^ bit20ValueRegister3 ^ bit21ValueRegister3 ^ bit22ValueRegister3);

                byte randomBit1 = shiftAndInsert_1(register1, nextBit1);
                byte randomBit2 = shiftAndInsert_2(register2, nextBit2);
                byte randomBit3 = shiftAndInsert_3(register3, nextBit3);

                byte randomBitFinal = (byte) (randomBit1 ^ randomBit2 ^ randomBit3);

                randomByte = (byte) ((randomBitFinal << (7 - i)) | randomByte);
            }
            result[j] = randomByte;
        }
        return result;
    }

    public static void convertFromByteArrayToIntArray (byte[] array) {
        int i;
        for (i = 0; i < array.length; i = i + 4) {
            byte[] b = new byte[4];
            b[0] = array[i];
            b[1] = array[i + 1];
            b[2] = array[i + 2];
            b[3] = array[i + 3];

            //here it is a bug, TODO
            int nr = ((b[0] & 0xFF) << 24) | ((b[1] & 0xFF) << 16) | ((b[2] & 0xFF) << 8 ) | ((b[3] & 0xFF));

            System.out.println(nr);
        }

        System.out.println();
    }

    public static void main(String[] args) {
        String password = "someLFSR";
        System.out.println("Initial password: " + getHex(password.getBytes()));
        convertFromByteArrayToIntArray(password.getBytes());
        System.out.println();

        byte[] random = getPseudoRandomBytes(password, 8);
        System.out.println(getHex(random));

        System.out.println();

        random = getPseudoRandomBytes(password, 14);
        System.out.println(getHex(random));

        random = getPseudoRandomBytes(password, 20);
        System.out.println(getHex(random));

        random = getPseudoRandomBytes(password, 30);
        System.out.println(getHex(random));

        random = getPseudoRandomBytes(password, 100);
        System.out.println(getHex(random));

        //convertFromByteArrayToIntArray(random);
    }
}
