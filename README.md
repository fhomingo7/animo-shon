# Animo ShOn: An Online Shopping Application for Lasallians
In the current situation worldwide, students are forced to continue their education through online means. As online classes persevere during the pandemic, students may encounter  classes that require specific materials such as electronics or other tools that are not commonly found at home. In order to keep up with the class requirements, they will need to buy these materials or tools. However, as the pandemic continues, they are discouraged from going out and are advised to just use alternative means to acquire said materials and tools. With that in mind, Animo ShOn is created to cater the needs of Lasallians and supply them with the needed materials.

# Requirements and Dependencies
Language - Java

Application - Android Studio

Database - Firebase

Virtual Device in Android Studio
- Pixel 4 API 24
- Resolution: 1080 x 2280; 440 dpi
- API: 24
- Target: Android 7.0 (Google APIs)

# Environment Setup
1. Download the latest version of Android Studio and have a Firebase database
2. Clone the repository
3. Create a virtual device (phone category) on Android Studio
5. Download necessary files in default
6. Run the program

Note: There may be database related changes in order to make the software work

# Revision Logs
### April 04, 2021 
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

### April 30, 2021
Admin Menu
- Able to login as admin using username: admin, password: admin
- Able to upload product information and save it to database

Main Menu
- Able to view products in home page

### May 05, 2021
Product View
- Layout is implemented
- Image entity for 4 images is placed

Me Menu
- xml is implemented
- button functions not yet working except logout
- NOTE: logout just redirects user to main_menu.xml

### May 09, 2021
Revamped Login and Register Features
- users use their id number in order to login and register
- name is also needed when registering
- temporary admin account settings (username: 12345, password: password)

Added User Functions
- user is now able to like and unlike items
- user is now able to add to their cart and see total price
- user can also change quantity of items in the cart
- sliding animation is temporarily implemented 

### May 18, 2021
My Profile 
- Functioning and can update user information

Added User Functions
- Added confirm shipping details and confirm order to cart list
- Added verification for user to order and ship products

### May 19, 2021
Added Admin Functions
- Allows administrator to check user Orders and the products inside the Orders

### May 23, 2021
Added Admin Functions
- Administrator can edit products (delete button that does not yet function) and approve order

Payment Method
- added radio buttons for Gcash and COD options since we have no experience in bank/card payment 

### May 26, 2021
Addittional Features
- user cannot check out if user has not picked delivery option
- received package (customer feedback)
- prompts in login is updated
- search still has bugs

# Contributors
Programmed by Shawn Castillo, Jason Ching, Florenz Domingo

De La Salle University, BS Computer Engineering, 119
