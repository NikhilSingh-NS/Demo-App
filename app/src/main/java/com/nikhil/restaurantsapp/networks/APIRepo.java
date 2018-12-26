package com.nikhil.restaurantsapp.networks;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.nikhil.restaurantsapp.comp.CApp;
import com.nikhil.restaurantsapp.entity.Categories;
import com.nikhil.restaurantsapp.entity.Restaurants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIRepo {

    private APIInterface apiInterface;
    private long totalPages;

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public APIRepo()
    {
        apiInterface = CApp.getAPIClient().create(APIInterface.class);
    }

    public LiveData<List<Categories>> getCategoryFromAPI()
    {
        final MutableLiveData<List<Categories>> mutableLiveData = new MutableLiveData<>();
        apiInterface.getCategories().enqueue(new Callback<ServerResponses>() {
            @Override
            public void onResponse(Call<ServerResponses> call, Response<ServerResponses> response) {

                if(response.isSuccessful())
                {
                    mutableLiveData.setValue(response.body().getCategories());
                }
            }

            @Override
            public void onFailure(Call<ServerResponses> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public List<Restaurants> getRestaurantsFromAPIAsPaging(long entityId, String entityType, long start, long categoryId)
    {
        List<Restaurants> restaurantsList = new ArrayList<>();
        Call<ServerResponses> callingEntity = apiInterface.searchRestaurantsAsPaging(entityId, entityType, start, categoryId);
        try
        {
            Response<ServerResponses> response = callingEntity.execute();
            restaurantsList.addAll(response.body().getRestaurants());
            totalPages = response.body().getResults_found();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return restaurantsList;
    }

    public LiveData<List<Restaurants>> getRestaurantsFromAPI(long entityId, String entityType, int start, int count, long categoryId)
    {
        final MutableLiveData<List<Restaurants>> mutableLiveData = new MutableLiveData<>();
        apiInterface.searchRestaurants(entityId, entityType, start, count, categoryId).enqueue(new Callback<ServerResponses>() {
            @Override
            public void onResponse(Call<ServerResponses> call, Response<ServerResponses> response) {

                if(response.isSuccessful())
                {
                    mutableLiveData.setValue(response.body().getRestaurants());

                }
            }

            @Override
            public void onFailure(Call<ServerResponses> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

}
