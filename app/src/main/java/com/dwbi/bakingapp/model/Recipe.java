package com.dwbi.bakingapp.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
    
    private Integer id;
    private String name;
    private List<Ingredient> ingredients = null;
    private List<Step> steps = null;
    private String servings;
    private String image;
    
    public String getName() { return name;}
    public List<Ingredient> getIngredients() {return ingredients;}
    public List<Step> getSteps() {return steps;}
    public String getServings() { return servings;}
    public String getImage() { return image;}
    
    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<Ingredient>();
            in.readList(ingredients, Ingredient.class.getClassLoader());
        } else {
            ingredients = null;
        }
        if (in.readByte() == 0x01) {
            steps = new ArrayList<Step>();
            in.readList(steps, Step.class.getClassLoader());
        } else {
            steps = null;
        }
        servings = in.readString();
        image = in.readString();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(steps);
        }
        dest.writeString(servings);
        dest.writeString(image);
    }
    
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }
        
        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    

}