# DietDiary</br>

##Features of the App:</br>
1. Home</br>
2. Step Counter</br>
3. Search Recipes</br>
4. Favourites</br>
5. Share</br>

We have used fragments for each feature of our App.</br>

###Home:</br>

###Step Counter:</br>
a. UI to start counting the footsteps of the user.</br>
b. Calculating the calories burnt and Miles covered by the user associated with the number of footsteps.</br>
c. The calories burnt are stored using the Shared Preferences.</br>

###Search Recipes:</br>
a. UI for the User to select the ingredients from the custom dropdown with check box.</br>
b. Searching the recipes, using Search Button, based on the selected ingredients and calorie less than calories burnt by the user.</br>
c. Populating the recipes in the form of item-subitem (recipe- corresponding ingredients) custom list.</br>
d. A provision for the users to click on any recipe to view the procedure.</br>
e. A provision for the users to long click on any recipe to add it to the favourites.</br>

###Favourites: </br>
a. UI to display all the recipes, the user has selected to be a part of favourites.
b. When user clicks on any recipe, the corresponding ingredients and procedures are fetched from the database.

###Share:</br>
a. Implemented sharing using Facebook API.</br>
b. Users can share any health tips or thoughts through this app.</br>
c. User can make a post to Facebook about the diet plan he followed and how it benefited him.</br>

###Search Icon:</br>
a. Clicking on this icon, user can navigate to the google search if he wants to search for any other information.</br>

###Back­end :</br>

1. Sensor Manager has been used to detect and count user’s footsteps.</br>
2. We have used XML for recipe content.</br>
3. Reading the XMl file has been done through XMLPullParser and then inserted into the database.</br>
4. We have incorporated SQLiteDatabase in our project to insert and retrieve the recipes.</br>
5. An algorithm that will search for healthy recipe from the database based on the calorie</br>
   count of the ingredients and user’s calorie expenditure.</br>
   
   ##Demo of the App


