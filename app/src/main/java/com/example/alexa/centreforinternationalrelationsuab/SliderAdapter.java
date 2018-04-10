package com.example.alexa.centreforinternationalrelationsuab;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    private int[] slide_images = {
            R.drawable.university,
            R.drawable.cetate,
            R.drawable.students
    };

    private String[] slide_headings = {
            "waiting for you...",
            "adventure",
            "Learn"
    };

    private String[] slide_descriptions = {
            "&quot;1 Decembrie 1918 &quot; University of Alba Iulia is a public higher education and research institution founded in 1991 in Alba Iulia , Romania.",
            "The university now has five main faculties, each divided into several departments. They are:\nHistory and Philology\nEconomic Sciences\nExact and Engineering Sciences\nLaw and Social Sciences\nOrthodox Theology",
            "This app will help you get trough your Erasmus experience."
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.slide_image);
        TextView slideHeading = view.findViewById(R.id.slide_heading);
        TextView slideDescription = view.findViewById(R.id.slide_description);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descriptions[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
