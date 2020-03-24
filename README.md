## Food Recipes
Android app to search for food recipes by ingredient or category.

## Screenshots
<p>
  <img src="/Screenshots/Screenshot_1582252407.png" width="270" height="450"/>
  <img src="/Screenshots/Screenshot_1582252476.png" width="270" height="450"/>
  <img src="/Screenshots/Screenshot_1582252452.png" width="270" height="450"/>
</p>

## Installation
Git clone the repository, import project in Android Studio, and run on an android device.

## Architecture Overview
This application makes use of the MVVM architectural pattern. 
Our project follows: Activity -> ViewModel -> Repository -> Remote Data Source -> Retrofit -> Webservice.
LiveData is retrieved from the Remote Data Source into our repository, then it trickles down all the way to our ViewModel. The activities do not hold data.

TO DO: Adding a local database cache using Room Persistence Library.

## External Dependencies

- [Recipes API](https://recipesapi.herokuapp.com/api/search): This is our core API, used to retrieve all the information about recipes.
- [Retrofit](https://square.github.io/retrofit/): Used to setup a REST API.
- [Gson](https://github.com/google/gson): A Java serialization/deserialization library to convert Java Objects into JSON and back
- [Glide](https://github.com/bumptech/glide): Library to display our recipe/food images.
- [CircleImageView](https://github.com/hdodenhof/CircleImageView): Library used to make circular image views.

## Tests
- Test: Select a category by clicking on it. Be able to view the many recipes in a category.
- Test: Select a Recipe manually by clicking on it. Be able to view the details of a recipe and its ingredients.
- Test: Be able to go back from list of recipes to categories by pressing BACK button or the toolbar menu -> Categories.
- Test: Search for a recipe by typing in a keyword in the search bar up top.



## Credits
Credits to CodingWithMitch for the help in learning to develop an android application using MVVM architecture and Retrofit.

## License
© [Yussef Saidi](https://yussefsaidi.me/)
