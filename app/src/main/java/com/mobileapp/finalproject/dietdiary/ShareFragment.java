package com.mobileapp.finalproject.dietdiary;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aasawari on 5/29/2015.
 */
public class ShareFragment extends Fragment {

    public ShareFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_share, container, false);

        Intent myIntent = new Intent(getActivity(), ShareActivity.class);
        getActivity().startActivity(myIntent);

        return rootView;
    }

}
