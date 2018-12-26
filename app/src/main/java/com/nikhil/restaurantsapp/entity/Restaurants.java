package com.nikhil.restaurantsapp.entity;

import java.io.Serializable;

public class Restaurants {

    private Restaurant restaurant;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public class Restaurant implements Serializable{

        private long id;
        private String name;
        private String cuisines;
        private String average_cost_for_two;

        private String currency;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }


        public boolean isInclude_bogo_offers() {
            return include_bogo_offers;
        }

        public void setInclude_bogo_offers(boolean include_bogo_offers) {
            this.include_bogo_offers = include_bogo_offers;
        }

        private boolean include_bogo_offers;

        public String getAverage_cost_for_two() {
            return average_cost_for_two;
        }

        public void setAverage_cost_for_two(String average_cost_for_two) {
            this.average_cost_for_two = average_cost_for_two;
        }

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

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getPhotos_url() {
            return photos_url;
        }

        public void setPhotos_url(String photos_url) {
            this.photos_url = photos_url;
        }

        public String getFeatured_image() {
            return featured_image;
        }

        public void setFeatured_image(String featured_image) {
            this.featured_image = featured_image;
        }

        private String thumb;
        private String photos_url;
        private String featured_image;

        private Location location;

        public Location getLocation() {
            return location;
        }

        public String getCuisines() {
            return cuisines;
        }

        public void setCuisines(String cuisines) {
            this.cuisines = cuisines;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public UserRatings getUser_rating() {
            return user_rating;
        }

        public void setUser_rating(UserRatings user_rating) {
            this.user_rating = user_rating;
        }

        private UserRatings user_rating;

    }
}
