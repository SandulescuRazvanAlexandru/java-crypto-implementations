public class TestStringInt {

    public static class Account
    {
        String username;
        String password;
    }

    public static void main(String[] args) {

        Account aliceAcc = new Account();
        Account johnAcc = new Account();

        String fileName = "myPasswords.txt";
        String anotherFile = "myPasswords.txt";

        //here you check references - we found it
        if (fileName == anotherFile) {
            System.out.println("We found it!");
        } else {
            System.out.println("It's different");
        }

        anotherFile = new String("myPasswords.txt");

        if (fileName == anotherFile) { // - it's different
            System.out.println("We found it!");
        } else {
            System.out.println("It's different");
        }


        //here you compare content - comparing strings - we found it
        if (fileName.equals(anotherFile))
        {
            System.out.println("We found it");
        }
        else
        {
            System.out.println("It's different");
        }

        int aIntValue = 23; //primitive
        Integer aIntObject = aIntValue; //object - autoboxing
        int initialValue = aIntObject;

        System.out.println("Initial int value is "+initialValue);

        //up to 127 they will be the same - they are managed as Strings
        Integer smallInt = 100;
        Integer  anotherSmallInt = 100;

        //check the references
        if(smallInt == anotherSmallInt)
        {
            System.out.println("The 2 int values are the same");
        }
        else
        {
            System.out.println("They are different");
        }

        smallInt = 200;
        anotherSmallInt = 200;

        if(smallInt == anotherSmallInt) // they are different bcs > 127 => there will be created 2 different objects in memory
        {
            System.out.println("The 2 int values are the same");
        }
        else
        {
            System.out.println("They are different");
        }

        if(smallInt.equals(anotherSmallInt))
        {
            System.out.println("The 2 int values are the same");
        }
        else
        {
            System.out.println("They are different");
        }
        //=> ALWAYS use equals

        String message = "Hello";
        String message2 = message;
        message2 = "Bye";
        //immutable - once an obj is created, it can't be changed
        message2 = "This " + "is " + "a " + "message";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("This").append("is").append("a").append("message");
        message2 = stringBuffer.toString();


    }
}
