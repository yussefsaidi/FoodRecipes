package com.yussefsaidi.foodrecipes;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yussefsaidi.foodrecipes.models.Recipe;
import com.yussefsaidi.foodrecipes.util.Resource;
import com.yussefsaidi.foodrecipes.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";

    // UI components
    private ImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;
    private ScrollView mScrollView;

    private RecipeViewModel mRecipeViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);

        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("recipe")){
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
            subscribeObservers(recipe.getRecipe_id());
        }
    }

    private void subscribeObservers(final String recipeId){
        mRecipeViewModel.searchRecipeApi(recipeId).observe(this, new Observer<Resource<Recipe>>() {
            @Override
            public void onChanged(Resource<Recipe> recipeResource) {
                if(recipeResource != null){
                    if(recipeResource.data != null){
                        switch(recipeResource.status){

                            case LOADING:{
                                showProgressBar(true);
                                break;
                            }
                            case ERROR:{
                                Log.e(TAG, "onChanged: status: ERROR, Recipe: " + recipeResource.data.getTitle());
                                Log.e(TAG, "onChanged: ERROR message: " + recipeResource.message);
                                showParent();
                                showProgressBar(false);
                                break;
                            }
                            case SUCCESS:{
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, Recipe: " + recipeResource.data.getTitle());
                                showParent();
                                showProgressBar(false);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }


    private void showParent(){
        mScrollView.setVisibility(View.VISIBLE);
    }
}














