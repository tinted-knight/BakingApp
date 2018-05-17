package com.and.tim.bakingapp.repo.dao.query_models;

import android.arch.persistence.room.Relation;

import com.and.tim.bakingapp.repo.dao.entities.StepEntity;

import java.util.List;

public class StepListForRecipe {

    public int _id;

    public String name;

    @Relation(parentColumn = "_id", entityColumn = "recipeId")
    public List<StepEntity> steps;

}
