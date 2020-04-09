package com.yussefsaidi.foodrecipes.requests;

import com.yussefsaidi.foodrecipes.util.Constants;
import com.yussefsaidi.foodrecipes.util.LiveDataCallAdapter;
import com.yussefsaidi.foodrecipes.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.yussefsaidi.foodrecipes.util.Constants.CONNECTION_TIMEOUT;
import static com.yussefsaidi.foodrecipes.util.Constants.READ_TIMEOUT;
import static com.yussefsaidi.foodrecipes.util.Constants.WRITE_TIMEOUT;

public class ServiceGenerator {

    private static OkHttpClient client = new OkHttpClient.Builder()

            // Establish connection to server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

            // Time between each byte read from the server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

            // Time between each byte sent to the server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            .retryOnConnectionFailure(false)

            .build();

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    public static RecipeApi getRecipeApi(){
        return recipeApi;
    }
}
