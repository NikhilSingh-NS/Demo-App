package com.nikhil.restaurantsapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.nikhil.restaurantsapp.entity.Restaurants;
import com.nikhil.restaurantsapp.networks.APIRepo;

import java.util.List;

import static com.nikhil.restaurantsapp.entity.Constant.ROW_COUNT;
import static com.nikhil.restaurantsapp.entity.Constant.TYPE_CITY;

public class RestaurantsViewModel extends ViewModel {

    private LiveData<List<Restaurants>> restaurantList;
    private APIRepo apiRepo;

    public RestaurantsViewModel() {
        apiRepo = new APIRepo();
    }

    public LiveData<List<Restaurants>> getRestaurantListFromApi(long cityId, long categoryId,int start) {
        return apiRepo.getRestaurantsFromAPI(cityId, TYPE_CITY, start, ROW_COUNT, categoryId);
    }



}
