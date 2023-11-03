package ro.ase.ism.sap;

public class TestShift {

	public static void main(String[] args) {
		byte value = (byte) 0b10001000;
		String print = String.format("%8s", Integer.toBinaryString(value & 0xFF)).replace(' ', '0');
		System.out.println(print); // 10001000
		
		value = (byte) (value >> 1); //signed shift
		
		print = String.format("%8s", Integer.toBinaryString(value & 0xFF)).replace(' ', '0');
		System.out.println(print); // 11000100
		
		value = (byte) (value >>> 1); //unsigned shift
		
		print = String.format("%8s", Integer.toBinaryString(value & 0xFF)).replace(' ', '0');
		System.out.println(print); // 111000100 still 1 as the first
		
		value = (byte) (value >>> 1);
		value = (byte) (value & 0b01111111);	//cancel the 1st bit 1 if any
		print = String.format("%8s", Integer.toBinaryString(value & 0xFF)).replace(' ', '0');
		System.out.println(print); // 01110001 
	}

}
