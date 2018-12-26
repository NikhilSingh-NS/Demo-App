package com.nikhil.restaurantsapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikhil.restaurantsapp.MainActivity;
import com.nikhil.restaurantsapp.R;
import com.nikhil.restaurantsapp.adapters.AdapterInterface;
import com.nikhil.restaurantsapp.adapters.HomePageAdapter;
import com.nikhil.restaurantsapp.entity.Categories;
import com.nikhil.restaurantsapp.entity.Constant;
import com.nikhil.restaurantsapp.entity.Restaurants;
import com.nikhil.restaurantsapp.entity.Utility;
import com.nikhil.restaurantsapp.viewmodels.CategoryViewModel;
import com.nikhil.restaurantsapp.viewmodels.RestaurantsViewModel;
import com.nikhil.restaurantsapp.viewmodelsfactory.RestaurantsViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomePageFragment extends BaseFragment{

    public static final String TAG = HomePageFragment.class.getName();

    RecyclerView recyclerView;
    TextView lbl;
    CategoryViewModel categoryViewModel;
    RestaurantsViewModel restaurantsViewModel;
    HomePageAdapter homePageAdapter;
    List<Categories.Category> categoriesList;
    HashMap<Long,List<Restaurants>> restaurantHashMap;
    long cityId = 1L;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home_page, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        lbl = view.findViewById(R.id.lbl);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        categoriesList = new ArrayList<>();
        restaurantHashMap = new HashMap<>();
        setHomePageAdapter();
        initialiseViewModels();
        initList();
    }

    private void initList()
    {
        if(Utility.isInternetConnected(getActivity()))
        {
                showPrg(getString(R.string.pls_wait));
                categoryViewModel.getCategories().observe(this, categoriesObservers);
        }
        else
        {
            if(categoriesList.size() == 0)
                lbl.setVisibility(View.VISIBLE);
            else
                lbl.setVisibility(View.GONE);
        }
    }

    private void setHomePageAdapter()
    {
        homePageAdapter = new HomePageAdapter(categoriesList, clickInterface, restaurantHashMap);
        recyclerView.setAdapter(homePageAdapter);
    }


    private void initialiseViewModels()
    {
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        restaurantsViewModel = ViewModelProviders.of(HomePageFragment.this, new RestaurantsViewModelFactory(cityId)).get(RestaurantsViewModel.class);
    }

    Observer<List<Categories>> categoriesObservers = new Observer<List<Categories>>() {
        @Override
        public void onChanged(@Nullable final List<Categories> categories) {
            categoriesList.clear();
            restaurantHashMap.clear();
            boolean isLast = false;
            int size = categories.size();
            for (int i=0;i<size;i++)
            {
                Categories.Category category = categories.get(i).getCategory();
                categoriesList.add(category);
                if(i == size-1)
                    isLast = true;
                restaurantsViewModel.getRestaurantList(0, category.getId()).observe(HomePageFragment.this, new ListObserver(category, isLast));
            }
        }
    };

    class ListObserver implements Observer<List<Restaurants>>
    {
        Categories.Category category;
        boolean isLast;

        public ListObserver(Categories.Category category, boolean isLast)
        {
            this.category = category;
            this.isLast = isLast;
        }

        @Override
        public void onChanged(@Nullable List<Restaurants> restaurants) {

            if(restaurants.size() < Constant.ROW_COUNT)
                categoriesList.remove(category);
            else
                restaurantHashMap.put(category.getId(), restaurants);

            if(isLast) {
                hidePrg();
                homePageAdapter.notifyDataSetChanged();
            }
        }
    }

    AdapterInterface clickInterface = new AdapterInterface() {
        @Override
        public void onClick(Restaurants.Restaurant restaurant) {
            Log.d("fatal", "restaurant_name:" + restaurant.getName());
            Intent intent = new Intent();
            intent.putExtra("RESTAURANT", restaurant);
            ((MainActivity)getActivity()).switchFragments(MainActivity.FRAGMENT_DETAIL, intent);
        }

        @Override
        public void onClickSeeAll(Categories.Category category) {
            Intent intent = new Intent();
            intent.putExtra("CATEGORY", category);
            intent.putExtra("CITY_ID", cityId);

            ((MainActivity)getActivity()).switchFragments(MainActivity.FRAGMENT_LIST, intent);
        }
    };
}
