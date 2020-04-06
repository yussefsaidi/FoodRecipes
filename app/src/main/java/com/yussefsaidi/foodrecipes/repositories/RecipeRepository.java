package com.yussefsaidi.foodrecipes.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.yussefsaidi.foodrecipes.AppExecutors;
import com.yussefsaidi.foodrecipes.models.Recipe;
import com.yussefsaidi.foodrecipes.persistence.RecipeDao;
import com.yussefsaidi.foodrecipes.persistence.RecipeDatabase;
import com.yussefsaidi.foodrecipes.requests.ServiceGenerator;
import com.yussefsaidi.foodrecipes.requests.responses.ApiResponse;
import com.yussefsaidi.foodrecipes.requests.responses.RecipeSearchResponse;
import com.yussefsaidi.foodrecipes.util.NetworkBoundResource;
import com.yussefsaidi.foodrecipes.util.Resource;

import java.util.List;

public class RecipeRepository {

    private static final String TAG = "RecipeRepository";

    private static RecipeRepository instance;
    private RecipeDao recipeDao;

    public static RecipeRepository getInstance(Context context){
        if(instance == null){
            instance = new RecipeRepository(context);
        }
        return instance;
    }

    public RecipeRepository(Context context) {
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao();
    }

    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber){

        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()){
            @Override
            protected void saveCallResult(@NonNull RecipeSearchResponse item) {
                if(item.getRecipes() != null){ // recipe list will be null if the API server is down

                    Recipe[] recipes = new Recipe[item.getRecipes().size()];

                    int index = 0;
                    for(long rowid: recipeDao.insertRecipes((Recipe[]) (item.getRecipes().toArray(recipes)))){
                        if(rowid == -1){
                            Log.d(TAG, "saveCallResult: CONFLICT... This recipe is already in the cache");
                            // If the recipe already exists... I don't want to set the ingredients or timestamp
                            // because they will be erased
                            recipeDao.updateRecipe(
                                    recipes[index].getRecipe_id(),
                                    recipes[index].getTitle(),
                                    recipes[index].getPublisher(),
                                    recipes[index].getImage_url(),
                                    recipes[index].getSocial_rank()
                            );
                        }
                        index++;
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.searchRecipes(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return ServiceGenerator.getRecipeApi()
                        .searchRecipe(
                                query,
                                String.valueOf(pageNumber)
                        );
            }
        }.getAsLiveData();
    }
}
