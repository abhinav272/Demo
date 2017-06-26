package com.android.shopr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.shopr.R;
import com.android.shopr.model.LikelyPlaces;

import java.util.List;

/**
 * Created by Abhinav on 25/06/17.
 */
public class PlacesAdapter extends BaseAdapter {

    private List<LikelyPlaces> places;
    private LayoutInflater inflater;
    private OnPlaceSelectedListener onPlaceSelectedListener;

    public interface OnPlaceSelectedListener{
        void onPlaceSelected(LikelyPlaces likelyPlaces);
    }

    public void setOnPlaceSelectedListener(OnPlaceSelectedListener onPlaceSelectedListener){
        this.onPlaceSelectedListener = onPlaceSelectedListener;
    }

    public PlacesAdapter(Context context, List<LikelyPlaces> places) {
        this.places = places;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public LikelyPlaces getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null ){
            view = inflater.inflate(R.layout.places_text_view, parent, false);
        } else {
            view = convertView;
        }

        ((TextView) view).setText(getItem(position).getPlaceFullName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlaceSelectedListener.onPlaceSelected(getItem(position));
            }
        });

        return view;
    }
}
