package com.nikhil.restaurantsapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.nikhil.restaurantsapp.entity.Restaurants;
import com.nikhil.restaurantsapp.networks.APIRepo;

import java.util.List;

import static com.nikhil.restaurantsapp.entity.Constant.ROW_COUNT;
import static com.nikhil.restaurantsapp.entity.Constant.TYPE_CITY;

public class RestaurantsViewModel extends ViewModel {

    private long cityId;
    LiveData<List<Restaurants>> restaurantList;
    private APIRepo apiRepo;

    public RestaurantsViewModel(long cityId)
    {
        this.cityId = cityId;
        apiRepo = new APIRepo();
    }

    public LiveData<List<Restaurants>> getRestaurantList(int start, long categoryId) {
        return getRestaurantsFromAPI(start, categoryId);
    }

    private LiveData<List<Restaurants>> getRestaurantsFromAPI(int start, long categoryId)
    {
        return apiRepo.getRestaurantsFromAPI(cityId, TYPE_CITY, start, ROW_COUNT, categoryId);
    }
}
