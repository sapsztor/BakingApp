package com.dwbi.bakingapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
    
public class RecipeService {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    //private static final String BASE_URL = "http://192.168.62.54:3000/";
    
    private static Retrofit.Builder builder =
        new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    
    static Retrofit retrofit = builder.build();
    
    public static <S> S createRecipeService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }
}
    

