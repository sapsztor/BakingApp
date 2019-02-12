package com.dwbi.bakingapp.ui;

public final class PlaceHolderImages {
    
    final static String[] assetImages = {
        "file:///android_asset/baking-cooking-eggs-46170.jpg",
        "file:///android_asset/pexels-photo.jpg",
        "file:///android_asset/pexels-photo-271082.jpeg",
        "file:///android_asset/pexels-photo-271458.jpeg",
        "file:///android_asset/pexels-photo-273850.jpeg"
    };
    static int imageIndex = 0;
    
    public static String getPlaceHolderImagePath(int position){
        return assetImages[position % assetImages.length];
    }
}
