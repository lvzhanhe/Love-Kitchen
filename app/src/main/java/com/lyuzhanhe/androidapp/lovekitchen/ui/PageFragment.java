package com.lyuzhanhe.androidapp.lovekitchen.ui;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.lyuzhanhe.androidapp.lovekitchen.BuildConfig;

import com.android.volley.AuthFailureError;
import com.lyuzhanhe.androidapp.lovekitchen.R;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lyuzhanhe.androidapp.lovekitchen.Recipe;
import com.lyuzhanhe.androidapp.lovekitchen.utils.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;



public class PageFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler{
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String QUERY = "query";
    public static final String MODE = "mode";

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private String mPage;
    private String query;
    private String mode;

    public static PageFragment newInstance(String page, String q, String m) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, page);
        args.putString(QUERY, q);
        args.putString(MODE, m);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getString(ARG_PAGE);
        query = getArguments().getString(QUERY);
        mode = getArguments().getString(MODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager_list, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_recipe);
        mErrorMessageDisplay = (TextView) view.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        final RecipeAdapter mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        mLoadingIndicator.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = NetworkUtils.main_buildUrl(query, mode, sharedPreferences.getBoolean("veg",false),
                sharedPreferences.getBoolean("egg",true),sharedPreferences.getBoolean("gluten",true),
                sharedPreferences.getBoolean("peanut",true),sharedPreferences.getString("num","25")).toString();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Recipe> result = NetworkUtils.getRecipeFromJson(response);
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        mRecipeAdapter.setData(result);
                        if (result == null) showErrorMessage();
                        else showRecipeDataView();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mRecipeAdapter.setData(null);
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        showErrorMessage();
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

        return view;
    }

    @Override
    public void onClick(Recipe data, ImageView iv) {
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity(), iv, "transition_photo").toBundle();
        Intent intentToStartDetailActivity = new Intent(getContext(), DetailActivity.class);
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("Recipe", data);
        intentToStartDetailActivity.putExtras(bundle2);
        getContext().startActivity(intentToStartDetailActivity, bundle);
    }

    private void showRecipeDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}