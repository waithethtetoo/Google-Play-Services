package com.wtho.googleplayservices.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.wtho.googleplayservices.utils.FAUtils;
import com.wtho.googleplayservices.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WT on 9/1/2016.
 */
public class LocationFragment extends Fragment {
    public static final String TAG = LocationFragment.class.getSimpleName();
    private ControllerLocationScreen mLocationController;

    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.tv_likely_places)
    TextView tv_likely_places;
    private Location mLocation;

    public LocationFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLocationController = (ControllerLocationScreen) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mLocation != null) {
            setLocation(mLocation);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocation = null;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
        if (tv_location != null) {
            tv_location.setText(location.getLatitude() + "," + location.getLongitude());

        }
    }

    @OnClick(R.id.btn_get_current_place_name)
    public void onTapGetCurrentPlaceName(View view) {
        FAUtils.getObjInstance().logAppEvent(FAUtils.ACTION_TAP_GET_CURRENT_PLACE_NAME);
        mLocationController.onTapGetCurrentPlaceName();
    }

    public void setLikelyNamesForCurrentPlace(PlaceLikelihoodBuffer likelyPlaces) {
        StringBuffer stringBuffer = new StringBuffer();
        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
            stringBuffer.append(String.format("Place '%s' has likelihood: %g \n",
                    placeLikelihood.getPlace().getName(),
                    placeLikelihood.getLikelihood()));
        }
        if (!TextUtils.isEmpty(stringBuffer)) {
            tv_likely_places.setText(stringBuffer.toString());
        } else {
            tv_likely_places.setText("There is no likely places for current location yet.");
        }
        likelyPlaces.release();
    }

    public interface ControllerLocationScreen {
        void onTapGetCurrentPlaceName();
    }
}
