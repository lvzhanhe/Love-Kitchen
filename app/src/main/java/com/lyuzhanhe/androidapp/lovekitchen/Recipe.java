package com.lyuzhanhe.androidapp.lovekitchen;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class Recipe implements Parcelable{
    private Integer id;
    private String title;
    private String image;
    private String readyInMinutes;
    private String link;
    private String labels;
    private String ingredients;

    public Recipe() {}

    protected Recipe(Parcel in) {
        id = in.readInt();
        title = in.readString();
        image = in.readString();
        readyInMinutes = in.readString();
        link = in.readString();
        labels = in.readString();
        ingredients = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(readyInMinutes);
        dest.writeString(link);
        dest.writeString(labels);
        dest.writeString(ingredients);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

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
