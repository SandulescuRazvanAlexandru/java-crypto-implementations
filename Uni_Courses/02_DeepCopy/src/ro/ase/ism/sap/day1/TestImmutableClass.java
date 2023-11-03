package ro.ase.ism.sap.day1;

public class TestImmutableClass {
	
	
	public static class UserAccount {
		
		private final String username;
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
			UserAccount objAccount = (UserAccount) obj;
			return this.username.equals(objAccount.username);
		}				
	}

	public static void main(String[] args) {
		
		UserAccount alice = new UserAccount("alice", "1234");
		UserAccount anotherAlice = new UserAccount("alice", "1234");
		
		if(alice == anotherAlice) {
			System.out.println("Same accounts");
		}
		else {
			System.out.println("Different accounts");
		}
		
		if(alice.equals(anotherAlice)) {
			System.out.println("Same accounts");
		}
		else {
			System.out.println("Different accounts");
		}
		
		System.out.println("Alice account is " + alice);
		System.out.println("Alice account is " + alice.toString());
		
	}

}
