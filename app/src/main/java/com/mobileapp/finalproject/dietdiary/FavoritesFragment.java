package com.mobileapp.finalproject.dietdiary;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Swathi on 5/15/2015.
 */

public class FavoritesFragment extends Fragment {
    SharedPreferences pref;
    Set<String> s = new HashSet<String>(Arrays.asList("No Favourites Selected"));
    TextView text;
    ListView itemList;
    ArrayAdapter<String> adapter;
    List<String> nameList;
    List<String> procList;
    List<String> ingrdList;
    private DatabaseHelper datasource;
    private String mFromColumns[];
    private String mSelection;
    private String procedure = "";
    private String ingredient = "";

    public FavoritesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);
        pref = getActivity().getApplication().getSharedPreferences("Options", 0);
        text = (TextView) rootView.findViewById(R.id.text);
        itemList = (ListView) rootView.findViewById(R.id.itemlist);
        Set<String> set = pref.getStringSet("fItem", s);
        Set<String> procSet = pref.getStringSet("fProcedureItem", s);
        Set<String> ingrdSet = pref.getStringSet("fIngredientItem", s);
        nameList = new ArrayList<String>(set);
        procList = new ArrayList<String>(procSet);
        ingrdList = new ArrayList<String>(ingrdSet);

        if (nameList != null && nameList.size() > 0) {
            adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, nameList);
            itemList.setAdapter(adapter);
        } else {
            text.setText("No Favourites Selected");
        }

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Toast.makeText(getActivity(), "On item click", Toast.LENGTH_SHORT).show();

                int itemClicked = position;

                String recipeName = nameList.get(itemClicked);
                getRecipesProcedureIngredients(recipeName);
                Intent intent = new Intent(getActivity().getApplicationContext(), RecipeProcedureFragment.class);
                intent.putExtra("recpName", recipeName);
                intent.putExtra("recpProc", procedure);
                intent.putExtra("recpIngrd", ingredient);
                intent.putExtra("fromFragment", "favourites");
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void getRecipesProcedureIngredients(String recipeName) {
        datasource = new DatabaseHelper(getActivity());
        SQLiteDatabase db = datasource.getWritableDatabase();
        mFromColumns = new String[]{
                DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_DESC, DatabaseHelper.COLUMN_COUNT, DatabaseHelper.COLUMN_PROCEDURE
        };
        mSelection = " " + DatabaseHelper.COLUMN_NAME + " like " + "'%" + recipeName + "%'";
        Cursor cursor = db.query(DatabaseHelper.TABLE_RECIPES, mFromColumns, mSelection, null, null, null, null);
        while (cursor.moveToNext()) {
            procedure = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROCEDURE));
            ingredient = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESC));
        }
    }
}
