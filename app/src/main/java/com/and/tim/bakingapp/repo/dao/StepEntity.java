package com.and.tim.bakingapp.repo.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.and.tim.bakingapp.base.Marker;
import com.and.tim.bakingapp.model.Step;

@Entity(tableName = "steps")
public class StepEntity implements Marker, Parcelable{

    @PrimaryKey(autoGenerate = true)
    public int _id;
    @ColumnInfo(name = "id")
    public int stepId;

    private String shortDescription;

    private String description;

    private String videoURL;

    private String thumbnailURL;

    private Integer recipeId;

    public StepEntity() {
    }

    public StepEntity(Step s, int recipeId) {
        stepId = s.getId();
        shortDescription = s.getShortDescription();
        description = s.getDescription();
        videoURL = s.getVideoURL();
        thumbnailURL = s.getThumbnailURL();
        this.recipeId = recipeId;
    }

    protected StepEntity(Parcel in) {
        _id = in.readInt();
        stepId = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
        if (in.readByte() == 0) {
            recipeId = null;
        } else {
            recipeId = in.readInt();
        }
    }

    public static final Creator<StepEntity> CREATOR = new Creator<StepEntity>() {
        @Override
        public StepEntity createFromParcel(Parcel in) {
            return new StepEntity(in);
        }

        @Override
        public StepEntity[] newArray(int size) {
            return new StepEntity[size];
        }
    };

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        if (stepId == 0)
            return description;
        else
            return description.substring(3).trim();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(stepId);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
        if (recipeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(recipeId);
        }
    }
}
