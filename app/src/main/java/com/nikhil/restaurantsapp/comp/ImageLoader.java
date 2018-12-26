package com.nikhil.restaurantsapp.comp;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageLoader {

    public static void loadImage(ImageView target, String url)
    {
        Picasso.get().load(url).into(target);
    }

}
