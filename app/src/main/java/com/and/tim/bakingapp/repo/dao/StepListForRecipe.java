package com.and.tim.bakingapp.repo.dao;

import android.arch.persistence.room.Relation;

import java.util.List;

public class StepListForRecipe {

    public int _id;

    public String name;

    @Relation(parentColumn = "_id", entityColumn = "recipeId")
    public List<StepEntity> steps;

}
