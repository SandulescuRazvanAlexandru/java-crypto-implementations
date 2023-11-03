import java.io.*;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws IOException {

        //===CITIRE TXT===//
        File message = new File("Message.txt");

        if(!message.exists()) {
            //message.createNewFile();
            System.out.println("Fisierul nu exista");
            return;
        }

        FileReader reader = new FileReader(message);
        BufferedReader bufferReader = new BufferedReader(reader);
        System.out.println("File content:");

        String line  = bufferReader.readLine();
        while(line != null) {
            System.out.println(line);
            line = bufferReader.readLine();
        }

        reader.close();


        //===SCRIERE TXT===//

        File message2 = new File("Message2.txt");
        if(!message2.exists()) {
            message2.createNewFile();
//            System.out.println("Fisierul nu exista");
//            return;
        }

        FileWriter fileWriter = new FileWriter(message2, false); //modificam true sau false
        PrintWriter printer = new PrintWriter(fileWriter);
        printer.println("Hello!");
        printer.println("This is a secret message!");
        printer.println("Don't tell anyone");

//        byte[] secretKey = {1 << 3, 1 << 5, (byte) 0b10110011, 1 << 3};
//        //don't do this
//        String wrongStringVersion = new String(secretKey);
//        printer.println(wrongStringVersion);
//
//        String base64Key = Base64.getEncoder().encodeToString(secretKey);
//        printer.println(base64Key);

        printer.close();

        System.out.println("===============================================================");


        //===Scriere binary===//
        File binaryFile = new File("myData.bin");
        if(!binaryFile.exists()) {
            binaryFile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(binaryFile, false);
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF("This is some data");		//a String
        dos.writeInt(23);						//an Integer
        dos.writeBoolean(true);					//a boolean
        dos.writeDouble(100.5);					//a double
        byte[] secretKey = {1 << 3, 1 << 5, (byte) 0b10110011, 1 << 3};
        //write the secretKey size
        //we need it for reading
        dos.writeInt(secretKey.length);             //an Integer
        dos.write(secretKey);					    //a byte array

        dos.close();


        //===Citire binary===//
        FileInputStream fis = new FileInputStream(binaryFile);
        DataInputStream dis = new DataInputStream(fis);

        String msg = dis.readUTF();
        int value = dis.readInt();
        boolean flag = dis.readBoolean();
        double dValue = dis.readDouble();
        int keySize = dis.readInt();
        byte[] key = new byte[keySize];
        dis.read(key);

        System.out.println("Message is: " + msg);
        System.out.println("Key size is " + keySize);
        System.out.println("The flag is " + flag);
        System.out.println("Key: " + Base64.getEncoder().encodeToString(key));

        dis.close();
    }
}
