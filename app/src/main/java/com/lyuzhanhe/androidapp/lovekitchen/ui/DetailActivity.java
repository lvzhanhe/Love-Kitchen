package com.lyuzhanhe.androidapp.lovekitchen.ui;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lyuzhanhe.androidapp.lovekitchen.BuildConfig;
import com.lyuzhanhe.androidapp.lovekitchen.R;
import com.lyuzhanhe.androidapp.lovekitchen.Recipe;
import com.lyuzhanhe.androidapp.lovekitchen.database.RecipeEntry;
import com.lyuzhanhe.androidapp.lovekitchen.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.transition.Slide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.lyuzhanhe.androidapp.lovekitchen.AppExecutors;
import com.lyuzhanhe.androidapp.lovekitchen.database.AppDatabase;

public class DetailActivity extends Activity {

    Recipe recipe;
    @BindView(R.id.detail_image_view) ImageView detail_image_view;
    @BindView(R.id.name_text_view) TextView name_text_view;
    @BindView(R.id.healthLabels_text_view) TextView healthLabels_text_view;
    @BindView(R.id.ingredientLines_text_view) TextView ingredientLines_text_view;
    @BindView(R.id.detail_button) Button detail_button;
    @BindView(R.id.favorite_button) Button favorite_button;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());

        recipe = (Recipe) getIntent().getExtras().getParcelable("Recipe");
        Picasso.get()
                .load(recipe.getImage())
                .into(detail_image_view);
        name_text_view.setText(recipe.getTitle());

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = NetworkUtils.detail_buildUrl(Integer.toString(recipe.getId())).toString();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        recipe.setLabels(NetworkUtils.getLabel(response));
                        recipe.setIngredients(NetworkUtils.getIngredients(response));
                        recipe.setLink(NetworkUtils.getLink(response));
                        Log.d("SUCCESS","!!!");
                        healthLabels_text_view.setText(recipe.getLabels());
                        ingredientLines_text_view.setText(recipe.getIngredients());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Mashape-Key", BuildConfig.API_KEY);
                params.put("X-Mashape-Host", BuildConfig.API_HOST);
                return params;
            }
        };
        queue.add(postRequest);

        Slide slide = new Slide(Gravity.BOTTOM);
        slide.addTarget(R.id.detail_text);
        slide.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in));
        slide.setDuration(100);
        getWindow().setEnterTransition(slide);

        detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getResources().getBoolean(R.bool.ifPaid)) {
                    Log.v("button:","open link"+recipe.getLink());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getLink()));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(DetailActivity.this,"Use paid version to checkout details~",Toast.LENGTH_SHORT).show();
                }

            }
        });

        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("button:","favorite");
                add_favorite(recipe.getId());
            }
        });
    }

    protected void add_favorite(final int add_movie_id) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            public void run() {
                int id = recipe.getId();
                String title = recipe.getTitle();
                String image = recipe.getImage();
                String readyInMinutes = recipe.getReadyInMinutes();
                String link = recipe.getLink();
                String labels = recipe.getLabels();
                String ingredients = recipe.getIngredients();

                RecipeEntry recipeEntry = new RecipeEntry(id,title,image,readyInMinutes,link,labels,ingredients);
                try {
                    // if the movie does not exist in the database.
                    if (mDb.recipeDao().loadRecipeById(id) == null) {
                        Log.v(".","the recipe does not exist in the database");
                        mDb.recipeDao().insertRecipe(recipeEntry);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), getString(R.string.add_success),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // if the movie exists in the database.
                        Log.v(".","the recipe exists in the database");
                        mDb.recipeDao().deleteRecipe(id);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), getString(R.string.add_fail),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    return;
                }
            }
        });
    }
}
