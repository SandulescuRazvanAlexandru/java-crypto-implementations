package sandulescu.razvanalexandru.ism.sap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static String getHex(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("%02X", value);
        }
        return output;
    }

    public static void printFolderContent(String path, int t) throws Exception {
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            File[] entries = folder.listFiles();
            for (File entry : entries) {
                for (int j = 0; j < t; j++) {
                    System.out.printf("\t");
                }
                if (entry.isDirectory()) {
                    System.out.println(entry.getName() + ": ");
                    ++t;
                    printFolderContent(entry.getAbsolutePath(), t);
                    --t;
                } else {
                    System.out.println(entry.getName());
                }
            }
        } else {
            throw new Exception("Not found");
        }
    }

    public static byte[] getHashMac(String inputFileName, byte[] password, String algorithm) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] hashMac;

        File file = new File(inputFileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(password, algorithm));

        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        byte[] bufferArray = new byte[1024];
        int numberOfBytesFromFile = bufferedInputStream.read(bufferArray);

        while (numberOfBytesFromFile != -1) {
            mac.update(bufferArray, 0, numberOfBytesFromFile);
            numberOfBytesFromFile = bufferedInputStream.read(bufferArray);
        }

        hashMac = mac.doFinal();

        return hashMac;
    }

    public static void printConsoleEachHashMac(String path, byte[] password, String algorithm) throws Exception {
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    printConsoleEachHashMac(file.getAbsolutePath(), password, algorithm);
                } else {
                    byte[] hashMac = getHashMac(file.getAbsolutePath(), password, algorithm);
                    System.out.println("HashMac for " + file.getName() + " is: " + getHex(hashMac));
                }
            }
        } else {
            throw new Exception("Not found");
        }
    }

    public static void writeFileEachHashMac(String path, PrintWriter printer, byte[] password, String algorithm) throws Exception {
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    writeFileEachHashMac(file.getAbsolutePath(), printer, password, algorithm);
                } else {
                    byte[] hashMac = getHashMac(file.getAbsolutePath(), password, algorithm);
                    printer.println("HashMac for " + file.getName() + " is: " + getHex(hashMac));
                }
            }
        } else {
            throw new Exception("Not found");
        }
    }

    public static void main(String[] args) throws Exception {

        String password = "ismsecret";
        byte[] bytesPassword = password.getBytes();
        String algorithm = "HmacSHA1";

        //just to test the HashMac algorithm - it works properly
/*
        byte[] hashMac = getHashMac("SomeText.txt", bytesPassword, algorithm);
        System.out.println("HashMac is: " + getHex(hashMac));
*/

        //afisare completa a tuturor (sub)folderelor, recursiv, cu multiple \t (pentru a vedea fiecare fisier carui folder apartine)
/*
        System.out.println("===================================Afisarea completa a (sub)folderelor===================================");
        File entry = new File("C:\\Users\\Razvan\\Desktop\\HMAC Assignement\\TestFolder");
        int t = 0;
        printFolderContent(entry.getAbsolutePath(), t);
        System.out.println("=========================================================================================================");
*/

        //afisarea completa a hashmac-urilor txt-urilor la consola
/*
        System.out.println("===================================Afisarea completa a hashmac-urilor txt-urilor===================================");
        File file = new File("C:\\Users\\Razvan\\Desktop\\HMAC Assignement\\TestFolder");
        printConsoleEachHashMac(file.getAbsolutePath(), bytesPassword, algorithm);
        System.out.println("=========================================================================================================");
*/

        int choice = 1; // 1 - Status update: computes and stores the HashMAC of all the files
                        // 2 - Integrity check: verifies if any of the monitored files has been changed since the last status update

        if (choice == 1) {
            //scrierea hashmac-urilor in fisiere, recursiv
            File scanari = new File("C:\\Users\\Razvan\\Desktop\\HMAC Assignement\\Scanari");
            File fileSource = new File("C:\\Users\\Razvan\\Desktop\\HMAC Assignement\\TestFolder");
            if (scanari.exists() && scanari.isDirectory()) {
                File scan = new File("C:\\Users\\Razvan\\Desktop\\HMAC Assignement\\Scanari\\Scanarea nr " + (scanari.listFiles().length + 1) + ".txt");
                if (!scan.exists()) {
                    scan.createNewFile();
                }
                PrintWriter printer = new PrintWriter(scan);
                writeFileEachHashMac(fileSource.getAbsolutePath(), printer, bytesPassword, algorithm);

                printer.close();
            }

            System.out.println("Scanare efectuata");
        } else if(choice == 2) {
            //crearea de rapoarte
            File scanari = new File("C:\\Users\\Razvan\\Desktop\\HMAC Assignement\\Scanari");
            if (scanari.exists() && scanari.isDirectory()) {
                if (scanari.listFiles().length < 2) {
                    System.out.println("Efectuati inca o scanare pentru a vizualiza un raport (trebuie minim 2 scanari)");
                } else {
                    File[] scanariTotale = scanari.listFiles();

                    File compare1 = scanariTotale[scanariTotale.length - 2];
                    File compare2 = scanariTotale[scanariTotale.length - 1];

                    FileReader reader1 = new FileReader(compare1);
                    FileReader reader2 = new FileReader(compare2);
                    BufferedReader bufferReader1 = new BufferedReader(reader1);
                    BufferedReader bufferReader2 = new BufferedReader(reader2);

                    String line1 = bufferReader1.readLine();
                    String line2 = bufferReader2.readLine();

                    File rapoarte = new File("C:\\Users\\Razvan\\Desktop\\HMAC Assignement\\Rapoarte");
                    if (rapoarte.exists() && rapoarte.isDirectory()) {
                        File rap = new File("C:\\Users\\Razvan\\Desktop\\HMAC Assignement\\Rapoarte\\Raport nr " + (rapoarte.listFiles().length + 1) + ".txt");
                        if (!rap.exists()) {
                            rap.createNewFile();
                        }
                        PrintWriter printer = new PrintWriter(rap);

                        while (line1 != null && line2 != null) {

                            if (line1.equals(line2)) {
                                printer.println("OK " + line1);
                            } else {
                                printer.println("CORRUPTED " + line1); // aici puteam sa afisez old value (line1) vs new value (line2), dar am zis sa nu mai incarc programul
                            }

                            line1 = bufferReader1.readLine();
                            line2 = bufferReader2.readLine();
                        }

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        printer.println("Scanarea s-a efectuat la: " + dtf.format(now));

                        printer.close();
                    }

                    reader1.close();
                    reader2.close();

                    System.out.println("Raport efectuat");
                }

            }
        } else {
            System.out.println("Alegeti dintre 1 si 2");
        }
    }
}