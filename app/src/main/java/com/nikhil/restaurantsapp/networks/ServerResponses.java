package com.nikhil.restaurantsapp.networks;

import com.nikhil.restaurantsapp.entity.Categories;
import com.nikhil.restaurantsapp.entity.Location;
import com.nikhil.restaurantsapp.entity.Restaurants;

import java.util.List;

public class ServerResponses {

    private List<Categories> categories;

    public List<Categories> getCategories() {
        return categories;
    }

    private List<Restaurants> restaurants;

    private List<Location> location_suggestions;

    private long results_found;

    public List<Location> getLocation_suggestions() {
        return location_suggestions;
    }

    public void setLocation_suggestions(List<Location> location_suggestions) {
        this.location_suggestions = location_suggestions;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public long getResults_found() {
        return results_found;
    }

    public void setResults_found(long results_found) {
        this.results_found = results_found;
    }

    public long getResults_start() {
        return results_start;
    }

    public void setResults_start(long results_start) {
        this.results_start = results_start;
    }

    public long getResults_shown() {
        return results_shown;
    }

    public void setResults_shown(long results_shown) {
        this.results_shown = results_shown;
    }

    public List<Restaurants> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurants> restaurants) {
        this.restaurants = restaurants;
    }

    private long results_start;
    private long results_shown;

}
