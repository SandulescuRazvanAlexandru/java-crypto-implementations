package ro.ase.ism.sap.day1;

public class TestEnums {
	
	public enum HashAlgorithm {
		
		MD5(128,"Message Digest"), 
		SHA1(160,"Secure Hash Algorithm - 1"), 
		SHA2(256, "Secure Hash Algorithm - 2"), 
		SHA256(256, "Secure Hash Algorithm - 256"), 
		SHA512(512, "Secure Hash Algorithm - 512");
		
		private int size;
		private String fullName;
		
		private HashAlgorithm(int size, String name) {
			this.size = size;
			this.fullName = name;
		}

		public int getSize() {
			return size;
		}

		public String getFullName() {
			return fullName;
		}
		
		public byte[] getHashValue(String filename) {
			return null;
		}
		
	}

	public static void main(String[] args) {
		
		HashAlgorithm md = HashAlgorithm.MD5;
		System.out.println("Hash function is " + md);
		
		int hashSize = md.getSize();
		String name = md.getFullName();
		System.out.println(String.format(
				"The used hash function is %s and generates values on %d bits",
				name, hashSize));
		
		byte[] mdValue = md.getHashValue("test.txt");
		
	}

}
