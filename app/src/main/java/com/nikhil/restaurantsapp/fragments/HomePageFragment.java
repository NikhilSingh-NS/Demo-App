package com.nikhil.restaurantsapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nikhil.restaurantsapp.MainActivity;
import com.nikhil.restaurantsapp.R;
import com.nikhil.restaurantsapp.adapters.AdapterInterface;
import com.nikhil.restaurantsapp.adapters.HomePageAdapter;
import com.nikhil.restaurantsapp.comp.RecyclerViewMargin;
import com.nikhil.restaurantsapp.entity.Categories;
import com.nikhil.restaurantsapp.entity.Constant;
import com.nikhil.restaurantsapp.entity.Location;
import com.nikhil.restaurantsapp.entity.Restaurants;
import com.nikhil.restaurantsapp.entity.Utility;
import com.nikhil.restaurantsapp.viewmodels.CategoryViewModel;
import com.nikhil.restaurantsapp.viewmodels.LocationViewModel;
import com.nikhil.restaurantsapp.viewmodels.RestaurantsViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomePageFragment extends BaseFragment{

    public static final String TAG = HomePageFragment.class.getName();

    RecyclerView recyclerView;
    TextView lbl;
    CategoryViewModel categoryViewModel;
    RestaurantsViewModel restaurantsViewModel;
    LocationViewModel locationViewModel;
    HomePageAdapter homePageAdapter;
    List<Categories.Category> categoriesList;
    HashMap<Long,List<Restaurants>> restaurantHashMap;

    ImageView search;
    EditText query;
    long cityId;
    String currentCity;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        categoriesList = new ArrayList<>();
        restaurantHashMap = new HashMap<>();
        currentCity = Constant.DEFAULT_CITY;
        showPrg(getString(R.string.pls_wait));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home_page, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        query = view.findViewById(R.id.query);
        search = view.findViewById(R.id.search);

        lbl = view.findViewById(R.id.lbl);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        RecyclerViewMargin decoration = new RecyclerViewMargin(0, 0, getResources().getDimensionPixelSize(R.dimen.size_4), 0);
        recyclerView.addItemDecoration(decoration);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRestaurants();
            }
        });

        query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadRestaurants();
                    return true;
                }
                return false;
            }
        });

        setHomePageAdapter();
        initList();
    }

    private void initList()
    {
        if(Utility.isInternetConnected(getActivity()))
        {
            locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
            categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
            restaurantsViewModel = ViewModelProviders.of(this).get(RestaurantsViewModel.class);

            query.setText(currentCity);
            loadRestaurants();
        }
        else
        {
            if(categoriesList.size() == 0)
                lbl.setVisibility(View.VISIBLE);
            else
                lbl.setVisibility(View.GONE);
            hidePrg();
        }
    }

    private void setHomePageAdapter()
    {
        homePageAdapter = new HomePageAdapter(categoriesList, clickInterface, restaurantHashMap);
        recyclerView.setAdapter(homePageAdapter);
    }

    private void loadRestaurants()
    {
        query.clearFocus();
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(query.getWindowToken(), 0);

        if(Utility.isInternetConnected(getActivity()))
        {
            String city = query.getText().toString();
            locationViewModel.getLocationSuggestionsFromApi(city).observe(this, locationObserver);

        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }
    }

    Observer<List<Location>> locationObserver = new Observer<List<Location>>() {
        @Override
        public void onChanged(@Nullable List<Location> locations) {
            if(locations.size() > 0) {
                Location location = locations.get(0);
                query.setText(location.getCity_name());
                currentCity = location.getCity_name();
                if(cityId != location.getCity_id())
                {
                    cityId = location.getCity_id();
                    Log.d("fatal", "cityId : " + cityId);
                    showPrg(getString(R.string.pls_wait));
                    categoryViewModel.getCategoryFromAPI().observe(HomePageFragment.this, categoriesObservers);
                }
            }
            else
            {
                Toast.makeText(getActivity(), getString(R.string.try_search_better), Toast.LENGTH_SHORT).show();
            }
        }
    };

    Observer<List<Categories>> categoriesObservers = new Observer<List<Categories>>() {
        @Override
        public void onChanged(@Nullable final List<Categories> categories) {
            categoriesList.clear();
            restaurantHashMap.clear();
            boolean isLast = false;
            int size = categories.size();
            Log.d("fatal", "size:" + size);
            for (int i=0;i<size;i++)
            {
                Categories.Category category = categories.get(i).getCategory();
                categoriesList.add(category);
                if(i == size-1)
                    isLast = true;
                restaurantsViewModel.getRestaurantListFromApi(cityId, category.getId(), i+2).observe(HomePageFragment.this, new ListObserver(category, isLast));
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
