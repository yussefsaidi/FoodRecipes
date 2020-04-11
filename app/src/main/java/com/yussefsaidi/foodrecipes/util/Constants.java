package com.yussefsaidi.foodrecipes.util;

public class Constants {

    public static final String BASE_URL = "https://recipesapi.herokuapp.com/";

    public static final int CONNECTION_TIMEOUT = 10; // 10 seconds
    public static final int READ_TIMEOUT = 2; // 2 seconds
    public static final int WRITE_TIMEOUT = 2; // 2 seconds

    public static final int RECIPE_REFRESH_TIME = 60 * 60 * 24 * 30; // 30 days in seconds

    public static final String[] DEFAULT_SEARCH_CATEGORIES =
            {"Barbecue", "Breakfast", "Chicken", "Beef", "Brunch", "Dinner", "Wine", "Italian"};

    public static final String[] DEFAULT_SEARCH_CATEGORY_IMAGES =
            {
                    "barbecue",
                    "breakfast",
                    "chicken",
                    "beef",
                    "brunch",
                    "dinner",
                    "wine",
                    "italian"
            };
}
