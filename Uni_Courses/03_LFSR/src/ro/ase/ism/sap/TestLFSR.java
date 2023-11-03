package ro.ase.ism.sap;

public class TestLFSR {
	
	public static String getHex(byte[] array) {
		String output = "";
		for(byte value : array) {
			output += String.format("%02x", value);
		}
		return output;
	}


	public static byte shiftAndInsertRegister(byte[] register, byte nextBit) {

		// get the last bit from the last byte = is the random one
		// byte bit1Mask = 0b00000001;
		byte randomBit = (byte) ((1 & register[3]));

		// get the last bit from each byte of the register
		byte lastBitFrom1stByte = (byte) ((1 & register[0]));
		byte lastBitFrom2ndByte = (byte) ((1 & register[1]));
		byte lastBitFrom3rdByte = (byte) ((1 & register[2]));

		// shift the register
		register[0] = (byte) ((register[0] >>> 1)& 0b01111111);
		register[1] = (byte) ((register[1] >>> 1)& 0b01111111);
		register[2] = (byte) ((register[2] >>> 1)& 0b01111111);
		register[3] = (byte) ((register[3] >>> 1)& 0b01111111);

		// insert the previous byte bit into the next byte
		register[0] = (byte) (register[0] | (nextBit << 7));
		register[1] = (byte) (register[1] | (lastBitFrom1stByte << 7));
		register[2] = (byte) (register[2] | (lastBitFrom2ndByte << 7));
		register[3] = (byte) (register[3] | (lastBitFrom3rdByte << 7));

		return randomBit;
	}

	public static byte[] getPseudoRandomValues(String password, int noBytes) {

		byte[] result = new byte[noBytes];

		if (password.length() < 4) {
			return null;
		}
		byte[] initialSeed = password.getBytes();

		byte[] register = new byte[4];
		for (int i = 0; i < 4; i++) {
			register[i] = initialSeed[i];
		}
		
		System.out.println(getHex(register));

		byte randomByte = 0;

		//we do an extra 4 iterations to remove the initial seed
		for (int j = 0; j < noBytes; j++) {
			// ------------ we get a random byte
			for (int i = 0; i < 8; i++) {
				// ---------- we get a random bit
				byte bit1Mask = 0b00000001;
				byte bit2Mask = 0b00000010;
				byte bit3Mask = 0b00000100; // 1<<2
				byte bit6Mask = 1 << 5; // 0b00100000
				byte bit8Mask = (byte) (1 << 7); // 0b10000000
				byte firstByte8bitMask = (byte) 0b10000000;

				// get the value of the x^31 bit -> the 8th (right to left)
				// bit in the 1st byte of the register

				byte bit32Value = (byte) (((firstByte8bitMask & register[0]) >>> 7)& 0b01111111);
				byte bit8Value = (byte) (((bit8Mask & register[3]) >>> 7)& 0b01111111);
				byte bit6Value = (byte) (((bit6Mask & register[3]) >>> 5)& 0b01111111);
				byte bit3Value = (byte) (((bit3Mask & register[3]) >>> 2)& 0b01111111);
				byte bit2Value = (byte) (((bit2Mask & register[3]) >>> 1)& 0b01111111);
				byte bit1Value = (byte) ((bit1Mask & register[3]));

				byte nextBit = (byte) (bit32Value ^ bit8Value ^ bit6Value ^ bit3Value ^ bit2Value ^ bit1Value);

				
				System.out.println("Before shift:" + getHex(register));
				
				byte randomBit = shiftAndInsertRegister(register, nextBit);
				
				System.out.println("After shift: " + getHex(register));

				randomByte = (byte) ((randomByte += randomBit) << 1);

				// randomByte = (byte) ((randomBit << (7 - i)) | randomByte);
				// ------------------
			}
			// ------------------
//			if(j >= 4) {
//				result[j-4] = randomByte;
//			}
			result[j] = randomByte;
		}

		return result;
	}

	public static void main(String[] args) {
		
		String pass = "1234";
		byte[] random = getPseudoRandomValues(pass, 8);
		System.out.println(getHex(random));
	}

}
