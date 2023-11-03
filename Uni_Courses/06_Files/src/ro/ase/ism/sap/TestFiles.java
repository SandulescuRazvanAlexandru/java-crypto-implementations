package ro.ase.ism.sap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Base64;

public class TestFiles {
	
	public static void printFolderContent(String path) {
		File folder = new File(path);
		if(folder.exists() && folder.isDirectory()) {
			File[] entries = folder.listFiles();
			for(File entry : entries) {
				if(entry.isDirectory()) {
					System.out.println("\t "+ entry.getName() + " Folder content: ");
					printFolderContent(entry.getAbsolutePath());
				}
				else {
					System.out.println(entry.getName());
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		
		//interact with the File System
		File entry = new File("D:\\ASE 2021-2022\\secureapp\\06_Files");
		if(entry.exists() && entry.isDirectory()) {
			System.out.println("Folder content: ");
			printFolderContent(entry.getAbsolutePath());
		}
		File newFolder = new File("Temp");
		if(!newFolder.exists()) {
			newFolder.mkdir();
		}
		
		//interact with text files
		File textMessage = new File("SecretMessage.txt");
		if(!textMessage.exists()) {
			textMessage.createNewFile();
		}
		
		//writing
		//we use a FileWriter to open file with append mode
		FileWriter fileWriter = new FileWriter(textMessage, false);
		PrintWriter printer = new PrintWriter(fileWriter);
		printer.println("Hello !");
		printer.println("This is a secret message !");
		printer.println("Don't tell anyone");
		
		byte[] secretKey = {1 << 3, 1 << 5, (byte) 0b10110011, 1 << 3};
		//don't do this
		String wrongStringVersion = new String(secretKey);
		printer.println(wrongStringVersion);
		
		String base64Key = Base64.getEncoder().encodeToString(secretKey);
		printer.println(base64Key);
		
		printer.close();
		
		//reading
		FileReader reader = new FileReader(textMessage);
		BufferedReader bufferReader = new BufferedReader(reader);
		System.out.println("File content: ");
		
//		String line = "";	
//		while( (line = bufferReader.readLine()) != null) {
//			System.out.println(line);
//		}
		
		String line  = bufferReader.readLine();
		while(line != null) {
			System.out.println(line);
			line = bufferReader.readLine();
		}
		
		reader.close();
		
		//binary
		File binaryFile = new File("myData.bin");
		if(!binaryFile.exists()) {
			binaryFile.createNewFile();
		}
		
		//write into binary files
		FileOutputStream fos = new FileOutputStream(binaryFile, false);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeUTF("This is some data");		//a String
		dos.writeInt(23);						//an Integer
		dos.writeBoolean(true);					//a boolean
		dos.writeDouble(100.5);					//a double
		//write the secretKey size
		//we need it for reading
		dos.writeInt(secretKey.length);
		dos.write(secretKey);					//a byte array
		
		dos.close();
		
		System.out.println("The end");
		
		//read from binary files
		FileInputStream fis = new FileInputStream(binaryFile);
		DataInputStream dis = new DataInputStream(fis);
		
		String msg = dis.readUTF();
		int value = dis.readInt();
		boolean flag = dis.readBoolean();
		double dValue = dis.readDouble();
		int keySize = dis.readInt();
		byte[] key = new byte[keySize];
		dis.read(key);
		
		System.out.println("Message is " + msg);
		System.out.println("Key size is " + keySize);
		System.out.println("The flag is " + flag);
		System.out.println("Key: " + Base64.getEncoder().encodeToString(key));
		
		dis.close();
		
		//using a RandomAccesFile to read at any offset inside the file
		RandomAccessFile raf = new RandomAccessFile(binaryFile, "r");
		//we know that the binary file starts with a String and before that 
		//	we have 2 bytes for the String size
		
		int stringSize = raf.readShort();
		
		//jump the string value + its 2 byte size
		
		//raf.seek(stringSize + 2);
		
		raf.skipBytes(stringSize);
			
		//read only the integer
		//raf.seek(19);
		int theInt = raf.readInt();
		System.out.println("The integer is " + theInt);
	}

}

