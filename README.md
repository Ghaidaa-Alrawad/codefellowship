# CodeFellowship 

This application is a social platform built using Spring Boot to practice user authentication, profile management, and posting. Users can log in, sign up, create posts, view profiles.

---

## Features

- User Authentication: Secure login with BCrypt-hashed passwords.
- User Profile: Display username, first and last name, date of birth, and bio.
- User Registration: Seamless signup process with automatic login.
- Homepage: Different views for logged-in and non-logged-in users.
- Logging Out: Easy logout option for logged-in users.
- Creating Posts: Users can create posts to share their thoughts and updates.
- Viewing Posts: Users can view posts on their profile page.
- Viewing Other Users: Users can view the information about other users by their id's

---

## Getting Started

- Clone the repository
- Navigate to the project directory
- Create a database and then add your properties to applicaiton.properties file
- Run the application

---

## Usage

**Logging In**
- Access the login page by going to /login.
- Enter your username and password.
- Click the "Log in" button.

**Signing Up**
- Access the signup page by going to /signup.
- Fill in the required information, including username, password, first name, last name, date of birth, and bio.
- Click the "Sign Up" button.

**User Profile**
- Once logged in, your user profile information will be displayed on the homepage.

**Creating Posts**
-Logged-in users can create posts by visiting their profile page (/myprofile, from the navbar after logging in) and using the "Create a New Post" form.

**Viewing Posts and other users info**
- Users can view posts on their profile page and other users' profiles.