public class TestImmutableClass {
    //immutable class = no one can change its contents

    public static class UserAccount
    {
        private final String username; //final = constant
        private final String password;

        public UserAccount(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        @Override
        public boolean equals(Object obj) {
            UserAccount objAccount = (UserAccount)obj;
            return this.username.equals(objAccount.username);
        }
    }

    public static void main(String[] args)
    {
        UserAccount alice = new UserAccount("alice", "1234");
        UserAccount anotherAlice = new UserAccount("alice", "1234");

        if(alice == anotherAlice) //different accounts
        {
            System.out.println("Same account");
        }
        else
        {
            System.out.println("Different accounts");
        }
        //== only for numeric primitives

        if(alice.equals(anotherAlice)) //same accounts
        {
            System.out.println("Same account");
        }
        else
        {
            System.out.println("Different accounts");
        }

        //shows the reference address
        System.out.println("Alice account is " + alice);
        System.out.println("Alice account is " + alice.toString());
    }
}
