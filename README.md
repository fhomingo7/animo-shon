# Animo ShOn: An Online Shopping Application for Lasallians
In the current situation worldwide, students are forced to continue their education through online means. As online classes persevere during the pandemic, students may encounter  classes that require specific materials such as electronics or other tools that are not commonly found at home. In order to keep up with the class requirements, they will need to buy these materials or tools. However, as the pandemic continues, they are discouraged from going out and are advised to just use alternative means to acquire said materials and tools. With that in mind, Animo ShOn is created to cater the needs of Lasallians and supply them with the needed materials.

# Requirements and Dependencies
Language - Java

Application - Android Studio

Database - Firebase

# Environment Setup
1. Download Android Studio and have a Firebase database
2. Clone the repository
3. Create a virtual device (phone category) on Android Studio
4. Download necessary files in default
5. Run the program

# Revision Logs
### April 4, 2021 
Starting screen and login screen implemented 

Program/Application Design is initialized

Register.java
- Initialized Email and Password texts
- Added the registerNewUser method
- registerNewUser() is used to add any email and password to firebase

Login.java
- Initialized Email and Password texts
- Added loginUserAccount method
- loginUserAccount() is used to access the firebase and check whether credentials match
- On line 72, startActivity is defaulted from Login.java to MainActivity.java since no home/dashboard class made
- Redirects to Main Menu

### April 18, 2021
Register.java
- Confirm Password added
- Edited registration failed prompts

Main Menu
- Buttons are implemented but not functional

### April 21, 2021
Login.java
- Started an Admin Login method "loginAdminAccount"

Main Menu
- User can now navigate through the different pages of the main screen
- Java files for other footer buttons are added
- Search, Shopping Cart, and Messages windows are added

# Contributors
Programmed by Shawn Castillo, Jason Ching, Florenz Domingo

De La Salle University, BS Computer Engineering, 119
