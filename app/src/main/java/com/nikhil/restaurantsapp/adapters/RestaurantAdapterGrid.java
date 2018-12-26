package com.nikhil.restaurantsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikhil.restaurantsapp.R;
import com.nikhil.restaurantsapp.entity.Restaurants;
import com.nikhil.restaurantsapp.entity.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantAdapterGrid extends RecyclerView.Adapter<RestaurantAdapterGrid.RestaurantHolder>{

    List<Restaurants> restaurants;
    AdapterInterface mInterface;
    int type;


    public RestaurantAdapterGrid(List<Restaurants> restaurants, AdapterInterface mInterface, int type)
    {
        this.restaurants = restaurants;
        this.mInterface = mInterface;
        this.type = type;
    }

    @Override
    public int getItemViewType(int position)
    {
        return type;
    }

    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i)
        {
            case 1:
                 view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_row_item_two, viewGroup, false);
                return new RestaurantHolder(view);
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_row_item, viewGroup, false);
                return new RestaurantHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantHolder holder, int i) {
        Restaurants.Restaurant restaurant = restaurants.get(i).getRestaurant();

        holder.textView.setText(restaurant.getName());
        if(Utility.isValidStr(restaurant.getThumb()))
            Picasso.get().load(restaurant.getThumb()).placeholder(R.drawable.ic_restaurant_black_24dp).into(holder.imageView);

        holder.imageView.setTag(restaurant);
        holder.imageView.setOnClickListener(clickListener);

        holder.mView.setTag(restaurant);
        holder.mView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }


    public class RestaurantHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        View mView;

        public RestaurantHolder(View view)
        {
            super(view);
            imageView = view.findViewById(R.id.image);
            textView = view.findViewById(R.id.name);
            mView = view;
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Restaurants.Restaurant restaurant = (Restaurants.Restaurant) view.getTag();
            mInterface.onClick(restaurant);
        }
    };
}
