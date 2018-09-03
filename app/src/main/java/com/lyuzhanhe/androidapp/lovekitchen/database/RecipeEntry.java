package com.lyuzhanhe.androidapp.lovekitchen.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Index;

@Entity(tableName = "recipe", indices = @Index(value = {"id"}, unique = true))
public class RecipeEntry {

    @PrimaryKey
    private int id;
    private String title;
    private String image;
    private String readyInMinutes;
    private String link;
    private String labels;
    private String ingredients;

    public RecipeEntry(int id, String title, String image, String readyInMinutes, String link, String labels, String ingredients){
        this.id = id;
        this.title = title;
        this.image = image;
        this.readyInMinutes = readyInMinutes;
        this.link = link;
        this.labels = labels;
        this.ingredients = ingredients;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setReadyInMinutes(String readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getReadyInMinutes() {
        return readyInMinutes;
    }

    public String getLink() {
        return link;
    }

    public String getLabels() {
        return labels;
    }

    public String getIngredients() {
        return ingredients;
    }
}
