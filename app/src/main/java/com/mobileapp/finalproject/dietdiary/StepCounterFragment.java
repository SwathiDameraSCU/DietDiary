package com.mobileapp.finalproject.dietdiary;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileapp.finalproject.dietdiary.module.HistoryDatabaseHelper;

import java.text.DecimalFormat;

/**
 * Created by Aasawari on 5/18/2015.
 */

public class StepCounterFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView stepCounter;
    private ImageView stepImage;
    private TextView stepCount;
    private TextView calorieCount;
    private TextView mileCount;
    boolean activityRunning;
    private static int noOfSteps = 0;
    private static int countSteps = 0;
    private static int existingCount = 0;
    private String miles;
    private static float calorieCnt;
    private HistoryDatabaseHelper db;
    public StepCounterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stepcounter, container, false);

        initialize(rootView);

        db = new HistoryDatabaseHelper(getActivity().getApplicationContext());
        existingCount = db.getCountNum();
        stepCount.setText("Total Steps: " + countSteps);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        return rootView;
    }

    private void initialize(View rootView) {
        stepCounter = (TextView) rootView.findViewById(R.id.stepCounter);
        stepImage = (ImageView) rootView.findViewById(R.id.stepImage);
        stepCount = (TextView) rootView.findViewById(R.id.totalCount);
        calorieCount = (TextView) rootView.findViewById(R.id.calorieCount);
        mileCount = (TextView) rootView.findViewById(R.id.milesCount);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
              //countSteps = db.getCountNum();
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Options", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            calorieCnt = sharedPreferences.getFloat("calCnt", calorieCnt);
            countSteps = sharedPreferences.getInt("steps", countSteps);
            miles = sharedPreferences.getString("miles", miles);
            noOfSteps = sharedPreferences.getInt("noofsteps", noOfSteps);
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            calorieCount.setText("Calories Burnt: " + calorieCnt);
            stepCount.setText("Total steps: " + countSteps);
            mileCount.setText("Miles: " + miles);
        } else {
            Toast.makeText(getActivity(), "Step Counter Sensor not available!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onStop() {
        db.upsertData(countSteps + existingCount);
        super.onStop();
        activityRunning = false;
        sensorManager.unregisterListener(this);

    }
    @Override
    public void onPause() {
        super.onPause();
        activityRunning = false;
        sensorManager.unregisterListener(this);
        db.upsertData(countSteps + existingCount);
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("Options", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("calCnt", calorieCnt);
        editor.putInt("steps", countSteps);
        editor.putString("miles", miles);
        editor.putInt("noofsteps", noOfSteps);
        editor.commit();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int steps;
        if (activityRunning) {
            if (noOfSteps < 1) {
                noOfSteps = (int) event.values[0];
            }
            countSteps = ((int) event.values[0] - noOfSteps);

            double stepCountForCalc = countSteps;
            stepCount.setText("Total Steps: " + String.valueOf(countSteps));

            DecimalFormat dfCalories = new DecimalFormat("#.#");
            DecimalFormat dfMiles = new DecimalFormat("#.##");

            double milesCovered = (double) (stepCountForCalc * 0.0005);
            miles = dfMiles.format(milesCovered);

            mileCount.setText("Miles: " + miles);

            String calories = dfCalories.format((countSteps) * 0.045);
            //calorieCnt = (float) (countSteps * 0.045);
            calorieCount.setText("Calorie Count: " + calories);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
