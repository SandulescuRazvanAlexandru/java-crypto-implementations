package ro.ase.ism.sap.day1;

import java.util.Date;

public class TestDeepVsShallow {
	
	public static String getHex(byte[] array) {
		String output = "";
		for(byte value : array) {
			output += String.format("%02x", value);
		}
		return output;
	}
	
	public static class Certificate {
		String owner;
		byte[] publicKey;
		Date expirationDate;
				
		public Certificate(String owner, byte[] publicKey, Date expirationDate) {
			super();
			this.owner = owner;
			
			//this.owner = new String(owner);
			
			//by default a shallow copy
			//this.publicKey = publicKey;
					
			//deep copy
//			this.publicKey = new byte[publicKey.length];
//			for(int i = 0; i < publicKey.length; i++) {
//				this.publicKey[i] = publicKey[i];
//			}
			
			this.publicKey = publicKey.clone();			
			this.expirationDate = (Date) expirationDate.clone();
		}

		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("\n Owner: " + this.owner);
			stringBuilder.append("\n Public Key: " + getHex(this.publicKey));
			stringBuilder.append("\n Date: " + this.expirationDate);
			return stringBuilder.toString();
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			Certificate copy = 
					new Certificate(this.owner, this.publicKey, this.expirationDate);
			return copy;
		}
	}

	public static void main(String[] args) throws CloneNotSupportedException {
		
		byte[] pubKey = {1<<3, 1<<6, 1<<4, 1};
		
		Certificate ismCertificate = new Certificate("ISM.ase.ro", pubKey, new Date());
		
		System.out.println(ismCertificate.toString());
		
		pubKey[1] = (byte) 0xFF;
		
		System.out.println(ismCertificate.toString());
		
		
		//shallow copy
		Certificate studentIsmCeritifcate = ismCertificate;
		studentIsmCeritifcate.owner = "student.ism.ase.ro";
		
		System.out.println(ismCertificate.toString());
		
		//deep copy
		studentIsmCeritifcate = (Certificate) ismCertificate.clone();
		studentIsmCeritifcate.owner = "csie.ase.ro";
		
		System.out.println(ismCertificate.toString());
		System.out.println(studentIsmCeritifcate.toString());
	}

}
