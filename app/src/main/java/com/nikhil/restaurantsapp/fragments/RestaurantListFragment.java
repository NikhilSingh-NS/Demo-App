package com.nikhil.restaurantsapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikhil.restaurantsapp.MainActivity;
import com.nikhil.restaurantsapp.R;
import com.nikhil.restaurantsapp.adapters.AdapterInterface;
import com.nikhil.restaurantsapp.adapters.RestaurantPagedListAdapter;
import com.nikhil.restaurantsapp.comp.ProgressBarInterface;
import com.nikhil.restaurantsapp.entity.Categories;
import com.nikhil.restaurantsapp.entity.Restaurants;
import com.nikhil.restaurantsapp.entity.Utility;
import com.nikhil.restaurantsapp.viewmodels.RestaurantsPagedViewModel;

public class RestaurantListFragment extends BaseFragment {

    public final static String TAG = RestaurantListFragment.class.getName();

    RestaurantPagedListAdapter restaurantPagedListAdapter;
    RecyclerView recyclerView;
    RestaurantsPagedViewModel restaurantsPagedViewModel;

    PagedList<Restaurants> restaurantsList;
    long cityId;
    Categories.Category category;

    TextView lbl, categoryName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.paged_restaurant_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        lbl = view.findViewById(R.id.lbl);
        categoryName = view.findViewById(R.id.category);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initList();
    }

    private void initList()
    {
        Bundle bundle = getArguments();
        category = (Categories.Category) bundle.getSerializable("CATEGORY");
        cityId = bundle.getLong("CITY_ID");

        categoryName.setText(category.getName());
        restaurantsPagedViewModel = new RestaurantsPagedViewModel(category, cityId, progressBarInterface);

        if(Utility.isInternetConnected(getActivity()))
        {
            showPrg(getString(R.string.pls_wait));
            restaurantsPagedViewModel.getRestaurantLiveData().observe(this, new Observer<PagedList<Restaurants>>() {
                @Override
                public void onChanged(@Nullable PagedList<Restaurants> restaurants) {
                    hidePrg();
                    setHomePageAdapter();
                    restaurantsList = restaurants;
                    restaurantPagedListAdapter.submitList(restaurants);
                }
            });
        }
        else
        {

            if(restaurantsList.size() == 0)
                lbl.setVisibility(View.VISIBLE);
            else
                lbl.setVisibility(View.GONE);
        }
    }

    private void setHomePageAdapter()
    {
        restaurantPagedListAdapter = new RestaurantPagedListAdapter(adapterInterface);
        recyclerView.setAdapter(restaurantPagedListAdapter);
    }

    ProgressBarInterface progressBarInterface = new ProgressBarInterface() {
        @Override
        public void showProgress() {
            showPrg(getString(R.string.pls_wait));
        }

        @Override
        public void hideProgress() {
            hidePrg();
        }
    };

    AdapterInterface adapterInterface = new AdapterInterface() {
        @Override
        public void onClick(Restaurants.Restaurant restaurant) {
            Intent intent = new Intent();
            intent.putExtra("RESTAURANT", restaurant);
            ((MainActivity)getActivity()).switchFragments(MainActivity.FRAGMENT_DETAIL, intent);
        }

        @Override
        public void onClickSeeAll(Categories.Category category) {

        }
    };
}

