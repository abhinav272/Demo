package com.android.shopr;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.shopr.adapters.PlacesAdapter;
import com.android.shopr.model.LikelyPlaces;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Abhinav on 25/06/17.
 */
public class PlacesActivity extends BaseActivity implements TextWatcher, GoogleApiClient.OnConnectionFailedListener, PlacesAdapter.OnPlaceSelectedListener, View.OnClickListener {

    public static final int PLACE_SELECTED = 22;
    public static final String PLACE_ID = "place_id";
    public static final String PLACE_NAME = "place_name";
    public static final String PLACE_ADDRESS = "place_address";
    private EditText etSearchPlace;
    private ListView lvPlaces;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<LikelyPlaces> resultList;
    private PlacesAdapter placesAdapter;
    private ImageView ivClearPlace;
    private TextView tvDetect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        connectToApiClient();
        initUI();
    }

    private void connectToApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    private void initUI() {
        resultList = new ArrayList<>();
        placesAdapter = new PlacesAdapter(this, resultList);
        etSearchPlace = (EditText) findViewById(R.id.et_search_place);
        lvPlaces = (ListView) findViewById(R.id.lv_places);
        ivClearPlace = (ImageView) findViewById(R.id.iv_clear_place);
        lvPlaces.setAdapter(placesAdapter);
        etSearchPlace.addTextChangedListener(this);
        placesAdapter.setOnPlaceSelectedListener(this);
        ivClearPlace.setOnClickListener(this);
        tvDetect = (TextView) findViewById(R.id.tv_detect_my_location);
        tvDetect.setOnClickListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        Location location = getLocation();
        if (location!=null){
            PendingResult<AutocompletePredictionBuffer> autocompletePredictions = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, editable.toString().trim(),
                    getBounds(new LatLng(location.getLatitude(), location.getLongitude()), 100), getFilters());
            autocompletePredictions.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
                @Override
                public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                    if (autocompletePredictions.getStatus().isSuccess()) {
                        resultList.clear();
                        Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
                        while (iterator.hasNext()) {
                            AutocompletePrediction prediction = iterator.next();
                            LikelyPlaces likelyPlaces = new LikelyPlaces();
                            likelyPlaces.setPlaceFullName((String) prediction.getFullText(null));
                            likelyPlaces.setPlaceName((String) prediction.getPrimaryText(null));
                            likelyPlaces.setPlaceId(prediction.getPlaceId());
                            resultList.add(likelyPlaces);
                        }
                    }
                    else {
                        resultList.add(null);
                    }
                    autocompletePredictions.release();
                    placesAdapter.notifyDataSetChanged();
                }
            });

        } else {
            Log.e("Places: ", "Location not found");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    private LatLngBounds getBounds(LatLng latLng, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(latLng, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(latLng, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }

    private Location getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocationGPS != null) {
                return lastKnownLocationGPS;
            } else {
                Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                return loc;
            }
        } else {
            return null;
        }
    }

    private AutocompleteFilter getFilters() {
        return new AutocompleteFilter.Builder()
                .setCountry("IN").build();
    }

    @Override
    public void onPlaceSelected(LikelyPlaces likelyPlaces) {
        Intent data = new Intent();
        data.putExtra(PLACE_ID, likelyPlaces.getPlaceId());
        data.putExtra(PLACE_NAME, likelyPlaces.getPlaceName());
        data.putExtra(PLACE_ADDRESS, likelyPlaces.getPlaceFullName());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_clear_place:
                etSearchPlace.setText("");
                break;
            case R.id.tv_detect_my_location:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}
