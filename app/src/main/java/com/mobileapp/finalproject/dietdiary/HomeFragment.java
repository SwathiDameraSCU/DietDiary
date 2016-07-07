package com.mobileapp.finalproject.dietdiary;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mobileapp.finalproject.dietdiary.module.HistoryDataVO;
import com.mobileapp.finalproject.dietdiary.module.HistoryDatabaseHelper;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Aasawari on 5/17/2015.
 */

public class HomeFragment extends Fragment {
    private TableLayout tableLayout;
    private HistoryDatabaseHelper db;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.historyText);
        tableLayout = (TableLayout) rootView.findViewById(R.id.table);

        db = new HistoryDatabaseHelper(getActivity().getApplicationContext());
        db.createTestData();
        displayData();

        return rootView;
    }

    public void displayData() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(new Date());

        List<HistoryDataVO> list = db.getPreviousDayHistory();

        //To display history on home screen
        for (int i = 0; i < list.size(); i++) {
            if (!dateStr.equals(list.get(i).getDateStr())) {
                TableRow tr = new TableRow(getActivity().getApplicationContext());

                TextView tv1 = new TextView(getActivity().getApplicationContext());
                tv1.setText("" + list.get(i).getDateStr());
                tv1.setBackgroundResource(R.drawable.border_shape);
                tv1.setTextSize(16);
                tv1.setGravity(View.TEXT_ALIGNMENT_CENTER);

                TextView tv2 = new TextView(getActivity().getApplicationContext());
                tv2.setText("" + list.get(i).getStepCount());
                tv2.setBackgroundResource(R.drawable.border_shape);
                tv2.setTextSize(16);
                tv2.setGravity(View.TEXT_ALIGNMENT_CENTER);

                TextView tv3 = new TextView(getActivity().getApplicationContext());

                tv3.setText("" + (getFormattedCalories(list.get(i).getStepCount())));
                tv3.setBackgroundResource(R.drawable.border_shape);
                tv3.setTextSize(16);
                tv3.setGravity(View.TEXT_ALIGNMENT_CENTER);

                tr.addView(tv1);
                tr.addView(tv2);
                tr.addView(tv3);
                tableLayout.addView(tr);
            }
        }
    }

    private String getFormattedCalories(int steps) {
        DecimalFormat dfCalories = new DecimalFormat("#.#");
        return dfCalories.format((steps) * 0.045);
    }
}
