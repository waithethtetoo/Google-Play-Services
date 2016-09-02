package com.wtho.googleplayservices.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;
import com.wtho.googleplayservices.R;
import com.wtho.googleplayservices.utils.UserActivityUtils;
import com.wtho.googleplayservices.controllers.ControllerActivityRecognition;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WT on 9/1/2016.
 */
public class ActivityRecognitionFragment extends Fragment {


    @BindView(R.id.tv_activity)
    TextView tv_activity;
    @BindView(R.id.btn_request_activity_update)
    Button bnt_request_activity_update;
    @BindView(R.id.btn_remove_activity_update)
    Button btn_remove_activity_update;

    private ControllerActivityRecognition controllerActivityRecognition;

    public static final String TAG = ActivityRecognitionFragment.class.getSimpleName();

    public ActivityRecognitionFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        controllerActivityRecognition = (ControllerActivityRecognition) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_recognition, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        onTapRemoveActivityUpdate(null);
    }

    @OnClick(R.id.btn_remove_activity_update)
    public void onTapRemoveActivityUpdate(View view) {
        controllerActivityRecognition.removeActivityUpdate();
        bnt_request_activity_update.setEnabled(true);
        btn_remove_activity_update.setEnabled(false);
    }

    @OnClick(R.id.btn_request_activity_update)
    public void onTapRequestActivityUpdate(View view) {
        controllerActivityRecognition.requestActivityUpdate();
        bnt_request_activity_update.setEnabled(false);
        btn_remove_activity_update.setEnabled(true);
    }

    public void setDetectedActivities(ArrayList<DetectedActivity> detectedActivityArrayList) {
        StringBuilder activityTextBuilder = new StringBuilder();
        for (DetectedActivity detectedActivity : detectedActivityArrayList) {
            activityTextBuilder.append(UserActivityUtils.getActivityByType(detectedActivity.getType()) + "-" +
                    detectedActivity.getConfidence() + "% \n");
        }
        tv_activity.setText(activityTextBuilder.toString());
    }
}
