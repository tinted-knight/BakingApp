package com.and.tim.bakingapp.repo.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.and.tim.bakingapp.model.Step;

@Entity(tableName = "steps")
public class StepEntity {

    @PrimaryKey(autoGenerate = true)
    public int _id;
    @ColumnInfo(name = "id")
    public int stepId;

    public String shortDescription;

    public String description;

    public String videoURL;

    public String thumbnailURL;

    public Integer recipeId;

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

//    public Integer getStepId() {
//        return stepId;
//    }
//
//    public void setStepId(Integer id) {
//        this.stepId = id;
//    }
//
//    public int get_id() {
//        return _id;
//    }
//
//    public void set_id(int _id) {
//        this._id = _id;
//    }
}
