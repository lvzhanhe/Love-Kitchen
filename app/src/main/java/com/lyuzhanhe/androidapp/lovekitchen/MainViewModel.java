package com.lyuzhanhe.androidapp.lovekitchen;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.lyuzhanhe.androidapp.lovekitchen.database.RecipeEntry;
import com.lyuzhanhe.androidapp.lovekitchen.database.AppDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<RecipeEntry>> recipes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        recipes = database.recipeDao().loadAllRecipes();
    }

    public LiveData<List<RecipeEntry>> getRecipes() {
        return recipes;
    }
}
