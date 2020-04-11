package com.yussefsaidi.foodrecipes.viewmodels;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yussefsaidi.foodrecipes.models.Recipe;
import com.yussefsaidi.foodrecipes.repositories.RecipeRepository;
import com.yussefsaidi.foodrecipes.util.Resource;


public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = RecipeRepository.getInstance(application);
    }

    public LiveData<Resource<Recipe>> searchRecipeApi(String recipeId){
        return recipeRepository.searchRecipeApi(recipeId);
    }



}





















