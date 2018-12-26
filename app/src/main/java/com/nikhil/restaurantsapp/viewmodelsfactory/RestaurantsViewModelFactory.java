package com.nikhil.restaurantsapp.viewmodelsfactory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.nikhil.restaurantsapp.viewmodels.RestaurantsViewModel;

public class RestaurantsViewModelFactory implements ViewModelProvider.Factory{

    private long cityId;

    public RestaurantsViewModelFactory(long cityId)
    {
        this.cityId = cityId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RestaurantsViewModel(cityId);
    }
}
