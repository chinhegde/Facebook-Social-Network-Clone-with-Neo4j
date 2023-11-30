# Facebook Social Network Clone with Neo4j

## Facebook Database
### Explanation of Data Model

#### Types of nodes
- User: Each user has a name, email, and username property. Some have DOB, location, etc.
- Comment: Comments have a content, date, and time property. Some have “likes”.
- Post: I have represented three types of posts: regular text posts, photos, and reshared posts. Each post has a content, date, and time property. Posts can have a location and attached media property (for photos, videos, etc.), while reshared posts can have a reference to the original post.

#

#### Types of relations
- Friendship: (User -> User) Represents that they are friends.
- Likes: (User -> Post) Represents that the user liked the post.
- Comments: (User -> Comment) Represents that the user made the comment.
- Commented_on: (Comment -> Comment) or (Comment -> Post) Represents that the comment was made on the post/comment.
- Posts: (User -> Post) Represents that the user made the post

### Creating data instances
1. Create User nodes
We can observe that all users have the properties “Name”, “Email” and “Username”. Some users also have “Date of Birth”, “Lives in” and Privacy settings” for their profile.
```
  CREATE (:User {name: "Chinmayi Hegde", email: "ch13@sjsu.edu", username: "chegde", lives_in: "San Jose"});
```

```
  CREATE (:User {name: "User Four", email: "u4@sjsu.edu", username: "u4",  dob: date('1995-01-01')});
```

```
  CREATE (:User {name: "User Six", email: "u46@sjsu.edu", username: "u46",  privacy_setting: "friends"});
```

2. Create Post nodes
All posts have the properties “Content”, “Date” and “Time”. Some posts have “Privacy”, “Attached media” and “Location”. Additionally, we can also have other properties such as “Hashtags”, “Additional settings” for commenting and “Tags” as a relation.

```
CREATE (:Post { content: "fun w friends", date: date('2023-04-02'),
time: time('16:42:00'), location: "San Francisco, CA",
attached_media: "https://facebook.com/photoxyz.jpg", privacy:
"friends"});
```

```
CREATE (:Post {content: "<Photo>", date: date('2022-04-18'), time:
time('08:53:00'), location: "New York, NY"});
```

3. Create comment nodes
```
CREATE (:Comment {commentID: 1, content: "Yayyyyy", date:
"05-03-2023", time: "06:51:23 PST"});
```

4. Adding “Likes” attribute to Comment node
```
MATCH (c:Comment {content: "Yayyyyy"}) SET c.likes = 10;
MATCH (c:Comment {content: "Interesting!"}) SET c.likes = 5;
```

5. Create “Friendship” relation: USER -> USER
```
MATCH (u1:User {username: 'chegde'}), (u2:User {username: 'shah_a'}),
(u3:User {username: 'nznz'}), (u4:User {username: 'spriyanka'}),
(u5:User {username: 'ramya'}), (u6:User {username: 'sc1998'})
```
```
CREATE (u1)-[:friendship]->(u2), (u1)-[:friendship]->(u3),
(u1)-[:friendship]->(u4), (u1)-[:friendship]->(u5),
(u1)-[:friendship]->(u6), (u2)-[:friendship]->(u3),
(u2)-[:friendship]->(u5);
```
```match (c:User)-[r:friendship]-(m:User) return c.name, r, m.name;```
[Source](https://stackoverflow.com/questions/24010932/neo4j-bidirectional-relationship)
The command above is bi-directional.
