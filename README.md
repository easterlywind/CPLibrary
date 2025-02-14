﻿# Library Management App 📚

This is a **Library Management Application** built using JavaFX and MySQL. The application provides features for both library staff and users, including adding/editing books, managing users, borrowing/returning books, and more.


## Recommendation System Integration

This app integrates a **Recommendation System** to suggest books to users based on their reading history and the similarity between books. The recommendation system uses **Item-based Collaborative Filtering**, which calculates the similarity between books based on 
user borrowing patterns. The system is implemented in a separate repository. You can find more details about it [here](https://github.com/easterlywind/recommend-system).


## Technologies Used
- **JavaFX** for the user interface.
- **MySQL** for the database.
- **Google Books API** for fetching additional book details.
- **Collaborative Filtering** for the recommendation system.

## How to Run

### Step 1: Clone the Repositories
```bash
# Clone the Library Management App repository
git clone https://github.com/easterlywind/library-management-app.git
cd library-management-app

# Clone the Recommendation System repository
git clone https://github.com/easterlywind/recommend-system.git
```
### Step 2: Set up the Database
```bash
# Log in to MySQL
mysql -u root -p

# Create a new database
CREATE DATABASE cplibrary;

# Import the SQL script
USE cplibrary;
SOURCE infrastructure/database.sql;
```

### Step 3: Configure Database Connection
- Open the [DatabaseConnection.java](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fcplibrary%2FDatabaseConnection.java).
- Update the database URL, username, and password to match your MySQL configuration.

### Step 4: Build and Run the Application
```bash
# Build the project using Maven
mvn clean install

# Run the project
mvn javafx:run
```
