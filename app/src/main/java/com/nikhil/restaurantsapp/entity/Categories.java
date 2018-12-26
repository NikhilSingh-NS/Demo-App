package com.nikhil.restaurantsapp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Categories {

    @SerializedName("categories")
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public class Category implements Serializable{

        private long id;
        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
