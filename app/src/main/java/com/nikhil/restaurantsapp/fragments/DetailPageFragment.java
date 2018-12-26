package com.nikhil.restaurantsapp.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikhil.restaurantsapp.R;
import com.nikhil.restaurantsapp.entity.Restaurants;
import com.nikhil.restaurantsapp.entity.Utility;
import com.squareup.picasso.Picasso;

public class DetailPageFragment extends BaseFragment {

    public static final String TAG = DetailPageFragment.class.getName();

    private Restaurants.Restaurant restaurant;
    private ImageView imageView;
    private TextView nameText, ratingText, cuisinesText, localityText, addressText, avgCost, bogoOffers, userRatingText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View mView = inflater.inflate(R.layout.restraurant_detail, container, false);
        imageView = mView.findViewById(R.id.image);
        nameText = mView.findViewById(R.id.name);
        ratingText = mView.findViewById(R.id.ratings);
        cuisinesText = mView.findViewById(R.id.cuisines);
        localityText = mView.findViewById(R.id.locality);
        addressText = mView.findViewById(R.id.address);
        avgCost = mView.findViewById(R.id.avg_cost);
        bogoOffers = mView.findViewById(R.id.bogo_txt);
        userRatingText = mView.findViewById(R.id.rating_text);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        init();

        if(Utility.isValidStr(restaurant.getFeatured_image()))
        {
            int height = getActivity().getResources().getDimensionPixelSize(R.dimen.size_85);
            int width = getActivity().getResources().getDimensionPixelSize(R.dimen.size_170);
            Picasso.get().load(restaurant.getFeatured_image()).placeholder(R.drawable.ic_restaurant_black_24dp).resize(width, height).into(imageView);
        }

        nameText.setText(restaurant.getName());
        ratingText.setText(restaurant.getUser_rating().getAggregate_rating());
        Drawable drawable = ratingText.getBackground();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Color.parseColor("#" + restaurant.getUser_rating().getRating_color()));
        ratingText.setBackground(drawable);

        cuisinesText.setText(restaurant.getCuisines());
        localityText.setText(restaurant.getLocation().getLocality_verbose());
        addressText.setText(restaurant.getLocation().getAddress());

        userRatingText.setText(getString(R.string.user_experience, restaurant.getUser_rating().getRating_text()));
        avgCost.setText(getString(R.string.avg_cost, restaurant.getCurrency() + " " + restaurant.getAverage_cost_for_two()));

        if(restaurant.isInclude_bogo_offers())
            bogoOffers.setVisibility(View.VISIBLE);
        else
            bogoOffers.setVisibility(View.GONE);
    }

    private void init()
    {
        Bundle bundle = getArguments();
        restaurant = (Restaurants.Restaurant) bundle.getSerializable("RESTAURANT");
    }
}
