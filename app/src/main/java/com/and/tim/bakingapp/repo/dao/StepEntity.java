package com.and.tim.bakingapp.repo.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.and.tim.bakingapp.base.Marker;
import com.and.tim.bakingapp.model.Step;

@Entity(tableName = "steps")
public class StepEntity implements Marker{

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
            return description.substring(2).trim();
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
}
