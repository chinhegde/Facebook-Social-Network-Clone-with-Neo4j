package com.check.verify;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.InternalRecord;
import org.neo4j.driver.types.Node;

import static org.neo4j.driver.Values.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HelloWorldExample implements AutoCloseable {
    private final Driver driver;

    public HelloWorldExample(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws RuntimeException {
        driver.close();
    }
    
    public void createNodes() {
    	try (Session session = driver.session()) {
    		// Delete any previous nodes
    		session.run("MATCH (u)-[r]->(m) DELETE r");
    		session.run("MATCH (u) DELETE u");
    		
    		// Create lists to store user information
    		List<String> names = Arrays.asList("Chinmayi Hegde", "Shradha Chaturvedi", "Anushree Shah", "Nidhi Zare", "Priyanka Sharma", "Ramya Hegde");
    		List<String> emails = Arrays.asList("ch13@sjsu.edu", "shc@sjsu.edu", "ashah@sjsu.edu", "nz04@sjsu.edu", "pr98s@sjsu.edu", "ramya@sjsu.edu");
    		List<String> usernames = Arrays.asList("chegde", "sc1998", "shah_a", "nznz", "spriyanka", "ramya");

    		// Iterate through the lists to create user nodes
    		for (int i = 0; i < names.size(); i++) {
    		    String name = names.get(i);
    		    String email = emails.get(i);
    		    String username = usernames.get(i);
    		    
    		    session.run("CREATE (:User {name: $name, email: $email, username: $username})", parameters("name", name, "email", email, "username", username));
    		}
//    		Result result_2 = session.run("MATCH (u:User) RETURN u");
//            
//            while (result_2.hasNext()) {
//            	InternalRecord record = (InternalRecord) result_2.next();
//                Node postNode = record.get("u").asNode();
//                String userName = postNode.get("name").asString();
//                System.out.println(">>"+userName);
//            }

    		
            
            // Create the Post nodes
    		List<String> contents = Arrays.asList("fun w friends", "<Photo>", "<Reshared Photo>");
    		List<String> dates = Arrays.asList("2023-04-02", "2022-04-18", "2022-06-23");
    		List<String> times = Arrays.asList("16:42:00", "08:53:00", "08:43:00");
    		List<String> locations = Arrays.asList("San Francisco, CA", "New York, NY", "San Diego, CA");

    		for (int i = 0; i < contents.size(); i++) {
    		    session.run("CREATE (:Post { content: $content, date: date($date), time: time($time), location: $location })",
    		                parameters("content", contents.get(i), "date", dates.get(i), "time", times.get(i), "location", locations.get(i)));
    		}
    		
    		

            // Create Comment nodes
    		List<Integer> commentIds = Arrays.asList(1, 2, 3, 4);
    		List<String> commentContents = Arrays.asList("Yayyyyy", "Interesting!", "We need to go here!", "I couldn't agree more!");
    		List<String> commentDates = Arrays.asList("05-03-2023", "06-08-2023", "06-02-2023", "2023-04-30");
    		List<String> commentTimes = Arrays.asList("06:51:23 PST", "09:31:32 PST", "17:21:38 PST", "15:18:45 PST");

    		for (int i = 0; i < commentIds.size(); i++) {
    		    session.run("CREATE (:Comment {commentID: $id, content: $content, date: $date, time: $time})", parameters("id", commentIds.get(i), "content", commentContents.get(i), "date", commentDates.get(i), "time", commentTimes.get(i)));
    		}
            
            System.out.println("User nodes created.");
            System.out.println("Comment nodes created.");
            System.out.println("Post nodes created.");
            
            String username1 = "chegde";
            String username2 = "shah_a";
            String username3 = "nznz";
            String username4 = "spriyanka";
            String username5 = "ramya";
            String username6 = "sc1998";

            String query = "MATCH (u1:User {username: $username1}), "
            		+ "(u2:User {username: $username2}), "
            		+ "(u3:User {username: $username3}), "
            		+ "(u4:User {username: $username4}), "
            		+ "(u5:User {username: $username5}), "
            		+ "(u6:User {username: $username6}) " +
                           "CREATE (u1)-[:friendship]->(u2), "
                           + "(u1)-[:friendship]->(u3), "
                           + "(u1)-[:friendship]->(u4), "
                           + "(u1)-[:friendship]->(u5), "
                           + "(u1)-[:friendship]->(u6), "
                           + "(u2)-[:friendship]->(u3), "
                           + "(u2)-[:friendship]->(u5)";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("username1", username1);
            parameters.put("username2", username2);
            parameters.put("username3", username3);
            parameters.put("username4", username4);
            parameters.put("username5", username5);
            parameters.put("username6", username6);

            session.run(query, parameters);
    		
    		
            System.out.println("\nFriendship relation created.");


            String username = "chegde";
            String content1 = "fun w friends";
            String content2 = "<Photo>";
            String content3 = "<Reshared Post>";

            session.run("MATCH (u:User {username: $username}), (p:Post) WHERE p.content = $content1 OR p.content CONTAINS $content2 OR p.content CONTAINS $content3 CREATE (u)-[r:POSTS]->(p);", parameters("username", username, "content1", content1, "content2", content2, "content3", content3));
            
            System.out.println("Posts relation created for user - Chinmayi.");
            
            session.run("MATCH (c:Comment {commentID: $commentID}), (u:User {username: $username}) CREATE (u)-[:comments]->(c)", parameters("commentID", 1, "username", "sc1998"));
            session.run("MATCH (c:Comment {commentID: $commentID}), (u:User {username: $username}) CREATE (u)-[:comments]->(c)", parameters("commentID", 2, "username", "shah_a"));
            session.run("MATCH (c:Comment {commentID: $commentID}), (u:User {username: $username}) CREATE (u)-[:comments]->(c)", parameters("commentID", 3, "username", "ramya"));
            session.run("MATCH (c:Comment {commentID: $commentID}), (u:User {username: $username}) CREATE (u)-[:comments]->(c)", parameters("commentID", 4, "username", "sc1998"));
            
            System.out.println("Comments relation created.");
            
            session.run("MATCH (p:Post {content: $content}), (c1:Comment {commentID: $commentID}) CREATE (c1)-[r:commented_on]->(p)", parameters("content", "fun w friends", "commentID", 1));
            session.run("MATCH (p:Post {content: $content}), (c2:Comment {commentID: $commentID}) CREATE (c2)-[r:commented_on]->(p)", parameters("content", "<Reshared Post>", "commentID", 2));
            session.run("MATCH (p:Post {content: $content}), (c3:Comment {commentID: $commentID}) CREATE (c3)-[r:commented_on]->(p)", parameters("content", "<Reshared Post>", "commentID", 3));
            session.run("MATCH (p:Post {content: $content}), (c4:Comment {commentID: $commentID}) CREATE (c4)-[r:commented_on]->(p)", parameters("content", "<Photo>", "commentID", 4));
            session.run("MATCH (c1:Comment {commentID: $commentID1}),(c2:Comment {commentID: $commentID2}) CREATE (c1)-[:commented_on]->(c2)", parameters("commentID1", 5, "commentID2", 3));
            session.run("MATCH (c3:Comment {commentID: $commentID1}),(c4:Comment {commentID: $commentID2}) CREATE (c3)-[:commented_on]->(c4)", parameters("commentID1", 5, "commentID2", 1));
            
            System.out.println("Commented_on relation created.");
            
            String postContent = "fun w friends";
            String comment1Content = "Yayyyyy";
            String comment2Content = "Interesting!";
            
            session.run("MATCH (p:Post {content: $content}), (u1:User {username: $user1}), (u2:User {username: $user2})"
            		+ "CREATE (u1)-[:LIKES]->(p), (u2)-[:LIKES]->(p)", parameters("content", postContent, "user1", "nznz", "user2", "spriyanka"));
            session.run("MATCH (p1:Post), (u3:User {username: $user3}) CREATE (u3)-[:LIKES]->(p1)", parameters("user3", "ramya"));
            session.run("MATCH (c:Comment {content: $content}) SET c.likes = $likes", parameters("content", comment1Content, "likes", 10));
            session.run("MATCH (c:Comment {content: $content}) SET c.likes = $likes", parameters("content", comment2Content, "likes", 5));
            
            System.out.println("Likes relation created.");
            
//    		Result result_2 = session.run("MATCH (u:User)-[r:LIKES]->(p:Post) RETURN u");
//          
//	          while (result_2.hasNext()) {
//	          	InternalRecord record = (InternalRecord) result_2.next();
//	              Node postNode = record.get("u").asNode();
//	              String userName = postNode.get("name").asString();
//	              System.out.println(">>"+userName);
//	          }
            
            String username11 = "nznz";
            String content11 = "fun w friends";
            String content21 = "<Photo>";
            String username21 = "ramya";
            String content31 = "<Reshared Post>";

            session.run("MATCH (n:User {username: $username1}), (p1:Post {content: $content1}), (p2:Post {content: $content2}) CREATE (n)-[:SHARES]->(p1), (n)-[:SHARES]->(p2)", parameters("username1", username11, "content1", content11, "content2", content21));
            session.run("MATCH (r:User {username: $username2}), (p:Post {content: $content3}) CREATE (r)-[:SHARES]->(p)", parameters("username2", username21, "content3", content31));
        
            System.out.println("Shares relation created.");
            
            
        }
    }

    public void displayUserProfile(String name) {
    	System.out.println("\n----- USER PROFILE -----");
        try (Session session = driver.session()) 
        {
        	Result result = session.run("MATCH (u:User) RETURN u");
        	while (result.hasNext()) {
            	InternalRecord record = (InternalRecord) result.next();
                Node userNode = record.get("u").asNode();
                String userName = userNode.get("name").asString();
                if (userName.contains(name)) {
                System.out.println("User's name:" + userName);
                String uName = userNode.get("username").asString();
                System.out.println("Username:" + uName);
                String uemail= userNode.get("email").asString();
                System.out.println("Emaid ID:" + uemail);
            }
        	}
        }
    }
    
    public void listFriends(String name) {
    	System.out.println("\n----- FRIENDS LIST -----");
        try (Session session = driver.session()) 
        {
        	 Result result = session.run("MATCH (u:User {name: $name})-[:friendship]->(u1:User) RETURN u1",
                     parameters("name", name));

             System.out.println(name+"'s friends:");
             while (result.hasNext()) {
            	 InternalRecord record = (InternalRecord) result.next();
                 Node friend = record.get("u1").asNode();
                 String friendName = friend.get("name").asString();
                 System.out.println("- " + friendName);
             }

        	}
        }
    
    public void postLikes(String name) {
    	System.out.println("\n----- FRIENDS WHO LIKE YOUR POSTS -----");
        try (Session session = driver.session()) 
        {
        	Result result = session.run(
                    "MATCH (u1:User)-[:LIKES]->(p:Post) RETURN DISTINCT u1",
                    parameters("name", name));

                System.out.println("Friends who like your posts:");
             while (result.hasNext()) {
            	 InternalRecord record = (InternalRecord) result.next();
                 Node friend = record.get("u1").asNode();
                 String friendName = friend.get("name").asString();
                 System.out.println("- " + friendName);
             }
        	}
    }
    
    public void friendOfFriend(String username) {
    	System.out.println("\n----- FRIENDS OF FRIENDS -----");
        try (Session session = driver.session()) 
        {
        	Result result = session.run(
                    "MATCH (u:User {username: $username})-[:friendship*2]->(f:User) RETURN f",
                    parameters("username", username));

                System.out.println("Friends of friends are:");
             while (result.hasNext()) {
            	 InternalRecord record = (InternalRecord) result.next();
                 Node friend = record.get("f").asNode();
                 String friendName = friend.get("name").asString();
                 System.out.println("- " + friendName);
             }
        	}
    }
    
    public static void displayMenu() {
        System.out.println("\n------ MENU ------");
        System.out.println("1. List your friends");
        System.out.println("2. Show friends who like your posts");
        System.out.println("3. Make a friends list of one of your friends");
        System.out.println("4. Display User Profile");
        System.out.println("5. Exit");
    }

    public static void main(String... args) {
    	
    	try (Scanner scanner = new Scanner(System.in)) {
			try (HelloWorldExample greeter = new HelloWorldExample("bolt://localhost:7687", "neo4j", "chinmayihegde")) {
			    greeter.createNodes();
			    
			    while (true) {
			        displayMenu();
			        System.out.print("Enter your choice: ");
			        int choice = scanner.nextInt();
			        scanner.nextLine(); // Consume newline character

			        switch (choice) {
			            case 1:
			            	greeter.listFriends("Chinmayi Hegde");
			                break;
			            case 2:
			            	greeter.postLikes("Chinmayi Hegde");
			                break;
			            case 3:
			            	greeter.friendOfFriend("chegde");
			            	greeter.listFriends("Anushree Shah");
			                break;
			            case 4:
			            	System.out.print("Which user profile would you like to see?");
			            	String uname = scanner.nextLine() ;
			            	greeter.displayUserProfile(uname);
			            	break;
			            case 5:
			                System.out.println("Exiting...");
			                return;
			            default:
			                System.out.println("Invalid choice. Please try again.");
			        }

			        System.out.println();
			    }         
			}
		} 
  }
}
