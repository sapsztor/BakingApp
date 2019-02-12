package com.dwbi.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    private Integer id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Integer getId(){return id;}
    public String getShortDescription() {return shortDescription;}
    public String getDescription() {return description;}
    public String getVideoURL() {return videoURL;}
    public String getThumbnailURL() {return thumbnailURL;}
    

    protected Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }



    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }
        
        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };


}