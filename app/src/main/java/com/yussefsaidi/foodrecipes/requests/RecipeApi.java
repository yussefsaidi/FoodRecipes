package com.yussefsaidi.foodrecipes.requests;

import androidx.lifecycle.LiveData;

import com.yussefsaidi.foodrecipes.requests.responses.ApiResponse;
import com.yussefsaidi.foodrecipes.requests.responses.RecipeResponse;
import com.yussefsaidi.foodrecipes.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    // SEARCH
    @GET("api/search")
    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipe(
            @Query("q") String query,
            @Query("page") String page
    );

    // GET RECIPE REQUEST
    @GET("api/get")
    LiveData<ApiResponse<RecipeResponse>> getRecipe(
            @Query("rId") String recipe_id
    );
}
