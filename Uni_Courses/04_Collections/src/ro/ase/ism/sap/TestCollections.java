package ro.ase.ism.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestCollections {

	public static class User {
		int id;
		String username;
		int age;
		
		public User(int id, String username, int age) {
			super();
			this.id = id;
			this.username = username;
			this.age = age;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		@Override
		public boolean equals(Object obj) {
			User objUser = (User) obj;
			return this.username.equals(objUser.username);
		}

		@Override
		public int hashCode() {
			return this.id;
		}
		
		
	}
	
	public static class GenericBox{
		Object content;

		public GenericBox(Object content) {
			this.content = content;
		}

		public Object getContent() {
			return content;
		}

		public void setContent(Object content) {
			this.content = content;
		}
	}
	
	public static class RealGenericBox<T>{
		T content;

		public RealGenericBox(T content) {
			super();
			this.content = content;
		}

		public T getContent() {
			return content;
		}

		public void setContent(T content) {
			this.content = content;
		}
		
	}
	
	public static void main(String[] args) {
		
		GenericBox stringBox = new GenericBox("Hello!");
		String message = (String) stringBox.getContent();
		System.out.println("Msg = " + message);
		
		//this is the issue
		stringBox.setContent(new User(1,"Some", 20));
		
		//you get a Class Cast exception
		//message = (String) stringBox.getContent();
		
		if(stringBox.getContent() instanceof String) {
			message = (String) stringBox.getContent();
			System.out.println("Msg = " + message);
		}
		else {
			System.out.println("Who put something else in a String box ?");
		}
		
		RealGenericBox<String> anotherStringBox = new RealGenericBox<>("Hello !");
		RealGenericBox<User> userBox = new RealGenericBox<User>(new User(1,"John Doe",20));
		
		message = anotherStringBox.getContent();
		System.out.println("Msg = " + message);
		
		//anotherStringBox.setContent(new User());
		
		userBox.setContent(new User(1,"John Doe",20));
		
		RealGenericBox<Integer> intBox = new RealGenericBox<Integer>(10);
		
		//collections
		List<Integer> numbers = new ArrayList<>();
		numbers.add(10);
		numbers.add(20);
		numbers.add(30);
		numbers.add(40);
		numbers.add(40);
		numbers.add(40);

		System.out.println("Values = ");
		for(int i = 0; i < numbers.size(); i++) {
			System.out.printf("%d ", numbers.get(i));
		}
		
		System.out.println("\nValues = ");
		for(int value : numbers) {
			System.out.printf("%d ", value);
		}
		
		if(numbers.contains(40)) {
			System.out.println("\nWe have the value in the collection");
		}
		
		List<User> users = new ArrayList<>();
		users.add(new User(1,"Alice", 21));
		users.add(new User(2,"John", 21));
		users.add(new User(3,"Bob", 21));
		users.add(new User(4,"Vader", 21));
		users.add(new User(4,"Vader", 21));
		
		System.out.println("Users: ");
		for(User user : users) {
			System.out.println(user.getUsername());
		}
		
		User vader = new User(4,"Vader", 21);
		if(users.contains(vader)) {
			System.out.println("Vader is here");
		}
		else {
			System.out.println("We are safe");
		}
		
		//sets
		Set<User> uniqueUsers = new HashSet<>();
		uniqueUsers.add(new User(1,"Alice", 21));
		uniqueUsers.add(new User(2,"John", 21));
		uniqueUsers.add(new User(3,"Bob", 21));
		uniqueUsers.add(new User(4,"Vader", 21));
		uniqueUsers.add(new User(4,"Vader", 21));
		
		System.out.println("Unique Users: ");
		for(User user : uniqueUsers) {
			System.out.println(user.getUsername());
		}
		
		//maps
		
		Map<Integer, User> usersRegistry = new HashMap<>();
		usersRegistry.put(1, new User(1,"Alice", 21));
		usersRegistry.put(2, new User(2,"John", 21));
		usersRegistry.put(3, new User(3,"Bob", 21));
		usersRegistry.put(4, new User(4,"Vader", 21));
		
		System.out.println("Registry entries");
		for(Integer id : usersRegistry.keySet()) {
			System.out.println(usersRegistry.get(id).username);
		}
		
		//update the user with id 4
		usersRegistry.put(4, new User(4,"Luke", 21));
		
		User user = usersRegistry.get(4);
		if(user!=null) {
			System.out.println("We found it: " + user.username);
		}
		else
		{
			System.out.println("No user with that id");
		}
	}

}
