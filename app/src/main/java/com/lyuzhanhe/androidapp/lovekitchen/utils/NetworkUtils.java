package com.lyuzhanhe.androidapp.lovekitchen.utils;

import android.net.Uri;
import android.util.Log;

import com.lyuzhanhe.androidapp.lovekitchen.Recipe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String RECIPE_BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search";
    private static final String QUERY_PARAM = "query";
    private static final String NUM_PARAM = "number";
    private static final String DIET_PARAM = "type";
    private static final String VEG_PARAM = "diet";
    private static final String INTOLERANCES = "intolerances";

    private static final String VEG = "vegetarian";

    private static final String RECIPE_DETAIL_BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";

    public static URL main_buildUrl(String query, String mode, Boolean veg, Boolean egg, Boolean gluten, Boolean peanut, String num) {

         Uri.Builder ub = Uri.parse(RECIPE_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, query);
         if (veg) ub.appendQueryParameter(VEG_PARAM, VEG);

         StringBuilder sb = new StringBuilder();
         if (!egg) sb.append("egg"+",");
         if (!gluten) sb.append("gluten"+",");
         if (!peanut) sb.append("peanut"+",");
         if (sb.length() != 0) {
             sb.replace(sb.length()-1,sb.length(),"");
             ub.appendQueryParameter(INTOLERANCES,sb.toString());
         }
         ub.appendQueryParameter(DIET_PARAM, mode)
                 .appendQueryParameter(NUM_PARAM, num);

        Uri builtUri = ub.build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static ArrayList<Recipe> getRecipeFromJson(String JsonStr) {
        ArrayList<Recipe> result = new ArrayList<>();
        try {
            JSONObject recipeJson = new JSONObject(JsonStr);
            JSONArray results = recipeJson.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject j = results.getJSONObject(i);
                Recipe r = new Recipe();
                r.setImage("https://spoonacular.com/recipeImages/" + j.getString("image"));
                r.setId(j.getInt("id"));
                r.setTitle(j.getString("title"));
                r.setReadyInMinutes(j.getString("readyInMinutes"));
                result.add(r);
            }

        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public static URL detail_buildUrl(String id) {
        Uri builtUri = Uri.parse(RECIPE_DETAIL_BASE_URL + id + "/information").buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getLabel(String JsonStr) {
        try {
            JSONObject recipeJson = new JSONObject(JsonStr);

            StringBuilder sb = new StringBuilder();
            if (recipeJson.has("vegetarian")) {
                if (recipeJson.getBoolean("vegetarian") == true) {
                    sb.append("Vegetarian");
                    sb.append(", ");
                }
            }
            if (recipeJson.has("vegan")) {
                if (recipeJson.getBoolean("vegan") == true) {
                    sb.append("Vegan");
                    sb.append(", ");
                }
            }
            if (recipeJson.has("glutenFree")) {
                if (recipeJson.getBoolean("glutenFree") == true) {
                    sb.append("Gluten Free");
                    sb.append(", ");
                }
            }
            if (recipeJson.has("dairyFree")) {
                if (recipeJson.getBoolean("dairyFree") == true) {
                    sb.append("Dairy Free");
                    sb.append(", ");
                }
            }
            if (recipeJson.has("veryHealthy")) {
                if (recipeJson.getBoolean("veryHealthy") == true) {
                    sb.append("Very Healthy");
                    sb.append(", ");
                }
            }
            if (recipeJson.has("cheap")) {
                if (recipeJson.getBoolean("cheap") == true) {
                    sb.append("cheap");
                    sb.append(", ");
                }
            }
            if (recipeJson.has("veryPopular")) {
                if (recipeJson.getBoolean("veryPopular") == true) {
                    sb.append("Very Popular");
                    sb.append(", ");
                }
            }
            if (recipeJson.has("sustainable")) {
                if (recipeJson.getBoolean("sustainable") == true) {
                    sb.append("Sustainable");
                    sb.append(", ");
                }
            }
            if (sb.length() > 1) sb.replace(sb.length()-2,sb.length()-1,"");
            Log.v("getLabel","success");
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getIngredients(String JsonStr) {
        try {
            JSONObject recipeJson = new JSONObject(JsonStr);
            JSONArray extendedIngredients = recipeJson.getJSONArray("extendedIngredients");
            StringBuilder sb1 = new StringBuilder();
            sb1.append("Ingredients:\n\n");
            for (int i = 0; i < extendedIngredients.length(); i++) {
                JSONObject ing = extendedIngredients.getJSONObject(i);
                sb1.append("    ");
                sb1.append(ing.getString("originalString"));
                sb1.append("\n");
            }
            Log.v("getIngredients","success");
            return sb1.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getLink(String JsonStr) {
        try {
            JSONObject recipeJson = new JSONObject(JsonStr);
            Log.v("getLink","success");
            return recipeJson.getString("sourceUrl");
        } catch (Exception e) {
            return null;
        }
    }

}
