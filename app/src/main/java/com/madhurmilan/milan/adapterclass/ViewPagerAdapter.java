package com.madhurmilan.milan.adapterclass;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.kalyankuber.alpha.R;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    private final String [] images ;

    public ViewPagerAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_pager_layout, null);
        ShapeableImageView imageView = (ShapeableImageView) view.findViewById(R.id.viewPagerImage);

        Glide.with(context)
                .load(images[position])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        double imageHeight = width*0.4;
        imageView.getLayoutParams().height= (int) imageHeight;

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);

        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
