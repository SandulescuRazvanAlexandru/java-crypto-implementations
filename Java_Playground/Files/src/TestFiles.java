import java.io.*;
import java.util.Base64;

public class TestFiles {
    //byte - InputStream(read), OutputStream(write)
    //byte - pipeline (source - multiple nodes - destination)
    //obj serialization - object -> sequence of bytes in a file
    //char - Reader, Writer

    public static void printFolderContent(String path){
        File folder = new File(path);
        if(folder.exists() && folder.isDirectory()){
            //get the list of all the files and folders
            File[] entries = folder.listFiles();
            for(File entry : entries){
                if(entry.isDirectory()){
                    System.out.println("\t " + entry.getName() + "Folder content: ");
                    printFolderContent(entry.getPath());
                }
                else{
                    System.out.println(entry.getName() );
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

        //interact with the File system
        File entry = new File("D:\\ISM\\SAP_II\\06_Files");

        //check if entry is a folder
        if(entry.exists() && entry.isDirectory()){
            System.out.println("Folder content: ");
            printFolderContent(entry.getAbsolutePath());
        }
            File newFolder = new File(".Temp");
            if(!newFolder.exists()){
            newFolder.mkdir();
        }

        //interact with text files
        //create it in the root, not in the src
        File textMessage = new File("SecretMessage.txt");//creates the file
        if(!textMessage.exists()){
            textMessage.createNewFile();
        }

        //writing
        //we use the filewriter to open the file with append mode
        FileWriter fileWriter = new FileWriter(textMessage, true);
        PrintWriter printer = new PrintWriter(fileWriter); //writes in the file
        printer.println("Hello!");
        printer.println("This is a secret message!");
        printer.println("Don't tell anyone");

        byte[] secretKey = {1<<3, 1<<5, (byte) 0b10110011, 1 << 3};
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
        //String line = " ";
        //while((line = bufferReader.readLine()) != null){
          //  System.out.println(line);
        ///}

        String line = bufferReader.readLine();
        while(line != null){
            System.out.println(line);
            line = bufferReader.readLine();
        }
        reader.close();

        //binary
        File binaryFile = new File("myData.bin");
        if(!binaryFile.exists()){
            binaryFile.createNewFile();
        }

        //write into binary files
        //get access to binary binaryFile
        FileOutputStream fos = new FileOutputStream(binaryFile, false);
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF("This is some data"); //string
        dos.writeInt(23); //int
        dos.writeBoolean(true); //boolean
        dos.writeDouble(100.5); //double
       //write the secretKey size - we need it for reading
        dos.writeInt(secretKey.length);
        dos.write(secretKey); //byte array

        dos.close();
        System.out.println("The end");

        //read from binary files
        FileInputStream fis = new FileInputStream(binaryFile);
        DataInputStream dis = new DataInputStream(fis);
        //the values should be read in the order they were written
        String msg = dis.readUTF();
        int value = dis.readInt();
        boolean flag = dis.readBoolean();
        double dValue = dis.readDouble();
        int keySize = dis.readInt();
        byte[] key = new byte[keySize];
        dis.read(key);
        System.out.println("Message is " + msg);
        System.out.println("Key size is " + keySize);
        System.out.println("Key: " + Base64.getEncoder().encodeToString(key));
        System.out.println("The flag is " + false);
        dis.close();

        //using RandomAccessFile to read at any offset inside the file
        RandomAccessFile raf = new RandomAccessFile(binaryFile, "r");
        //we know that the binary file starts with a string and before that
        // we have 2 bytes for the string size
        int stringSize = raf.readShort();//we ve just read the first 2B

        //jump the string value + its 2 byte size -> to get to the int
        raf.seek(stringSize + 2);

        //raf.skipBytes(stringSize);

       //read only the integer
       // raf.seek(19);
        int theInt = raf.readInt();
        System.out.println("The integer is " + theInt);
    }
}
