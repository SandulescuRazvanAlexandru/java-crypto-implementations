import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class Main {

    //byteToArray
    public static String getHex(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("%02x", value);
        }
        return output;
    }


    private static File findUserFile(byte[] userHashValue, File userFileDirectory) {
        byte[] fileBuffer = new byte[1];
        for (File userFile : userFileDirectory.listFiles()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(userFile);
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

                while (fileInputStream.read(fileBuffer) != -1) {
                    messageDigest.update(fileBuffer);
                }
                byte[] result = messageDigest.digest();

                fileInputStream.close();
                if (Arrays.equals(userHashValue, result)) {
                    System.out.format("%20s %s\n", "base64", Base64.getEncoder().encodeToString(result));
                    System.out.format("%20s %s\n", "SHA2", getHex(result));
                    System.out.format("%20s %s\n", "User file", userFile.getName());
                    return userFile;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        String userFilePathRoot = "./userFiles";
        File userFileDirectory = new File(userFilePathRoot);
        if (!userFileDirectory.exists()) {
            System.out.println("User file directory not found");
        }

        String userBase64Value = "xNmqhL2yII9OE+Oi/lRuRC8m46cuYXf8gtDyYJgFNN8=";
        byte[] userHashValue = Base64.getDecoder().decode(userBase64Value);
        File userFile = findUserFile(userHashValue, userFileDirectory);
        if (userFile == null) {
            System.out.println("User file not found");
            return;
        }

    }
}
