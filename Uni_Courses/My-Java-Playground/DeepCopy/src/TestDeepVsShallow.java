import java.util.Date;

public class TestDeepVsShallow {

    //the certificate stores only the public key
    //the priv keys are only in the key store
    public static class Certificate
    {
        String owner;
        byte[] publicKey;
        Date expirationDate;

        public static String getHex(byte[] array)
        {
            String output = "";
            for(byte value : array)
            {
                output += String.format("%02x", value);
            }
            return output;
        }

        public Certificate(String owner, byte[] publicKey, Date expirationDate) {
           // this.owner = new String (owner); - no need, strings are immutable
            this.owner = owner;
            //by default a shallow copy
            //this.publicKey = publicKey;

            //!!!!create deep copy!!!!
            //this.publicKey = new byte[publicKey.length];
            //for(int i=0; i <publicKey.length; i++)
            //{
              //  this.publicKey[i] = publicKey[i];
            //}
            // equivalent to
            this.publicKey = publicKey.clone();

            this.expirationDate = (Date) expirationDate.clone();
        }

        //how the object should be displayed
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n Owner: " + this.owner);
            stringBuilder.append("\n Public key: " + getHex(this.publicKey));
            stringBuilder.append("\n Date: " + this.expirationDate);
            return stringBuilder.toString();
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Certificate copy = new Certificate(this.owner, this.publicKey, this.expirationDate);
            return copy;
        }
    }

    public static void main(String[]args) throws CloneNotSupportedException {
        byte[] pubKey = {1<<3, 1<<6, 1<<4, 1};
        Certificate ismCertificate = new Certificate("ism.ase.ro", pubKey, new Date());
        System.out.println(ismCertificate);
        pubKey[1] = (byte) 0xFF; //works only for shallow copy
        System.out.println(ismCertificate);

        //shallow copy
        Certificate studentIsmCertificate = ismCertificate;
        studentIsmCertificate.owner = "student";
        System.out.println(ismCertificate.toString());

        studentIsmCertificate = (Certificate) ismCertificate.clone();
        studentIsmCertificate.owner = "csie.ase.ro";
        System.out.println(ismCertificate.toString());
        System.out.println(studentIsmCertificate.toString());
    }

}
