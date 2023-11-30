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

