package com.nikhil.restaurantsapp.adapters;

import com.nikhil.restaurantsapp.entity.Categories;
import com.nikhil.restaurantsapp.entity.Restaurants;

public interface AdapterInterface {
    void onClick(Restaurants.Restaurant restaurant);
    void onClickSeeAll(Categories.Category category);
}
