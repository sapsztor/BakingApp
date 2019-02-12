package com.dwbi.bakingapp.network;

import com.dwbi.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeAPI {
    
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
