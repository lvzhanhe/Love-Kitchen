package com.lyuzhanhe.androidapp.lovekitchen.ui;
import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lyuzhanhe.androidapp.lovekitchen.R;

import java.util.ArrayList;
import java.util.List;
import com.lyuzhanhe.androidapp.lovekitchen.MainViewModel;
import com.lyuzhanhe.androidapp.lovekitchen.Recipe;
import com.lyuzhanhe.androidapp.lovekitchen.database.RecipeEntry;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.katso.livebutton.LiveButton;


public class WelcomeActivity extends AppCompatActivity {

    String query = "chicken";
    @BindView(R.id.search_button) LiveButton mButton;
    @BindView(R.id.edit_text_query) EditText edit_text_query;
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                query = edit_text_query.getText().toString();
                Intent intentToStartDetailActivity = new Intent(WelcomeActivity.this, MainActivity.class);
                intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, query);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle();
                startActivity(intentToStartDetailActivity, bundle);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_my_favorite) {
            Intent intentToStartDetailActivity = new Intent(WelcomeActivity.this, FavoriteActivity.class);
            intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, query);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle();
            startActivity(intentToStartDetailActivity, bundle);
            return true;
        }

        if (id == R.id.action_preference) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
