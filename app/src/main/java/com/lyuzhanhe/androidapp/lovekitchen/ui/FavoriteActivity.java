package com.lyuzhanhe.androidapp.lovekitchen.ui;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lyuzhanhe.androidapp.lovekitchen.MainViewModel;
import com.lyuzhanhe.androidapp.lovekitchen.R;
import com.lyuzhanhe.androidapp.lovekitchen.Recipe;
import com.lyuzhanhe.androidapp.lovekitchen.database.RecipeEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler{

    private static final String TAG = FavoriteActivity.class.getSimpleName();

    @BindView(R.id.recyclerview_favorite) RecyclerView mRecyclerView;
    @BindView(R.id.fav_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.fav_loading_indicator) ProgressBar mLoadingIndicator;

    RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        loadPageFavorite();

    }

    public void loadPageFavorite() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<RecipeEntry>>() {
            @Override
            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
                Log.d(TAG, "Receiving database update from LiveData");
                ArrayList<Recipe> favorite_data = new ArrayList<>();
                for (int i = 0; i < recipeEntries.size(); i++) {
                    RecipeEntry preFavorite = recipeEntries.get(i);
                    Recipe favorite = new Recipe();
                    favorite.setLink(preFavorite.getLink());
                    favorite.setIngredients(preFavorite.getIngredients());
                    favorite.setLabels(preFavorite.getLabels());
                    favorite.setId(preFavorite.getId());
                    favorite.setImage(preFavorite.getImage());
                    favorite.setReadyInMinutes(preFavorite.getReadyInMinutes());
                    favorite.setTitle(preFavorite.getTitle());
                    favorite_data.add(favorite);
                }
                mRecipeAdapter.setData(favorite_data);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(Recipe data, ImageView iv) {
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this, iv, "transition_photo").toBundle();
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("Recipe", data);
        intentToStartDetailActivity.putExtras(bundle2);
        this.startActivity(intentToStartDetailActivity, bundle);
    }
}
