package com.wtho.googleplayservices.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wtho.googleplayservices.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WT on 9/1/2016.
 */
public class GeofenceFragment extends Fragment {
    public static final String TAG = GeofenceFragment.class.getSimpleName();
    private ControllerGeofenceScreen controller;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        controller = (ControllerGeofenceScreen) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geofence, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_add_geofences)
    public void onTapAddGeofences(View view) {
        controller.onTapAddGeofences();
    }

    @OnClick(R.id.btn_start_place_picker)
    public void onTapStartPlacePicker(View view) {
        controller.onTapStartPlacePicker();
    }

    public interface ControllerGeofenceScreen {
        void onTapAddGeofences();

        void onTapStartPlacePicker();
    }
}
