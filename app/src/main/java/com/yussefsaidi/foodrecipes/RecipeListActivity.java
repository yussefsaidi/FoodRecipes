package com.yussefsaidi.foodrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.yussefsaidi.foodrecipes.adapters.OnRecipeListener;
import com.yussefsaidi.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.yussefsaidi.foodrecipes.models.Recipe;
import com.yussefsaidi.foodrecipes.util.Resource;
import com.yussefsaidi.foodrecipes.util.VerticalSpacingItemDecorator;
import com.yussefsaidi.foodrecipes.viewmodels.RecipeListViewModel;
import java.util.List;

import static com.yussefsaidi.foodrecipes.viewmodels.RecipeListViewModel.QUERY_EXHAUSTED;


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

        mRecipeListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RecipeListViewModel.class);

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
                        switch(listResource.status) {
                            case LOADING: {
                                if(mRecipeListViewModel.getPageNumber() > 1){
                                    mAdapter.displayLoading();
                                }
                                else{
                                    mAdapter.displayOnlyLoading();
                                }
                                break;
                            }
                            case ERROR: {
                                Log.e(TAG, "onChanged: cannot refresh the cache.");
                                Log.e(TAG, "onChanged: ERROR message: "+ listResource.message);
                                Log.e(TAG, "onChanged: status: ERROR, #recipes: "+ listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setRecipes(listResource.data);
                                Toast.makeText(RecipeListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();

                                if(listResource.message.equals(QUERY_EXHAUSTED)){
                                    mAdapter.setQueryExhausted();
                                }
                                break;
                            }

                            case SUCCESS: {
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #Recipes: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setRecipes(listResource.data);
                                break;
                            }
                        }
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

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);
        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }



    private void searchRecipesApi(String query){
        mRecyclerView.smoothScrollToPosition(0);
        mRecipeListViewModel.searchRecipesApi(query, 1);
        mSearchView.clearFocus();
    }

    private void initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter(this, initGlide());
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!mRecyclerView.canScrollVertically(1) && mRecipeListViewModel.getViewState().getValue() == RecipeListViewModel.ViewState.RECIPES){
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });


        mRecyclerView.setAdapter(mAdapter);
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

















