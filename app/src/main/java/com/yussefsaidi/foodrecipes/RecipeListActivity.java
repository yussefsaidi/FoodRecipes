package com.yussefsaidi.foodrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yussefsaidi.foodrecipes.adapters.OnRecipeListener;
import com.yussefsaidi.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.yussefsaidi.foodrecipes.models.Recipe;
import com.yussefsaidi.foodrecipes.util.Resource;
import com.yussefsaidi.foodrecipes.util.Testing;
import com.yussefsaidi.foodrecipes.util.VerticalSpacingItemDecorator;
import com.yussefsaidi.foodrecipes.viewmodels.RecipeListViewModel;
import com.yussefsaidi.foodrecipes.viewmodels.RecipeViewModel;

import java.util.List;


public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";

    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecyclerView = findViewById(R.id.recipe_list);
        mSearchView = findViewById(R.id.search_view);

        mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        initRecyclerView();
        initSearchView();
        setSupportActionBar(findViewById(R.id.toolbar));
        subscribeObservers();
    }

    private void subscribeObservers(){

        mRecipeListViewModel.getRecipes().observe(this, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {
                if(listResource != null) {
                    Log.d(TAG, "onChanged: status: " + listResource.status);

                    if(listResource.data != null){
                        Testing.printRecipes(listResource.data, "data");
                    }
                }
            }
        });

        mRecipeListViewModel.getViewState().observe(this, new Observer<RecipeListViewModel.ViewState>() {
            @Override
            public void onChanged(RecipeListViewModel.ViewState viewState) {
                if(viewState != null){
                    switch (viewState){
                        case RECIPES:
                            //recipes will show automatically from another observer
                            break;
                        case CATEGORIES:
                            displaySearchCategories();
                            break;
                    }
                }
            }


        });
    }

    private void displaySearchCategories() {
        mAdapter.displaySearchCategories();
    }

    private void searchRecipesApi(String query){
        mRecipeListViewModel.searchRecipesApi(query, 1);
    }

    private void initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initSearchView(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                searchRecipesApi(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        searchRecipesApi(category);
    }

}

















