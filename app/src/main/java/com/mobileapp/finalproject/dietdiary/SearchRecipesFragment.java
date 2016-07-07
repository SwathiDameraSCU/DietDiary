package com.mobileapp.finalproject.dietdiary;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Swathi on 5/15/2015.
 */

public class SearchRecipesFragment extends ListFragment {
    private MultiChoiceSpinner spinner;
    private TextView recipeMsg;
    private DatabaseHelper datasource;
    private String mFromColumns[];
    private String mSelection;
    private String mOrderBy;
    private HashMap<String, String> procMap = new HashMap();
    private HashMap<String, String> nameMap = new HashMap();
    private HashMap<String, String> ingredientMap = new HashMap();
    Set<String> favRecipeList = new HashSet<String>();
    Set<String> favIngredientList = new HashSet<String>();
    Set<String> favProcedureList = new HashSet<String>();
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private String[] ingds;
    private CursorAdapter mAdapter;
    private String mSelectionCalories;
    String recipeName;
    String ingredient;
    String procedure;
    Set<String> s = new HashSet<String>(Arrays.asList("No Favourites Selected"));
    List<String> nameList;

    public SearchRecipesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_searchrecipes, container, false);

        prefs = getActivity().getApplicationContext().getSharedPreferences("Options", 0);
        editor = prefs.edit();
        ingds = getResources().getStringArray(R.array.list_of_ingredients);

        recipeMsg = (TextView) rootView.findViewById(R.id.recipeMsg);

        spinner = (MultiChoiceSpinner) rootView.findViewById(R.id.selectedIngd);
        spinner.setItems(ingds);

        Button button = (Button) rootView.findViewById(R.id.search_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datasource = new DatabaseHelper(getActivity());
                SQLiteDatabase db = datasource.getWritableDatabase();
                mFromColumns = new String[]{
                        DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_DESC, DatabaseHelper.COLUMN_COUNT, DatabaseHelper.COLUMN_PROCEDURE
                };
                mSelection = "";
                mSelectionCalories = "";
                mOrderBy = DatabaseHelper.COLUMN_COUNT;

                List<String> selectedIngds = spinner.getSelectedStrings();
                StringBuilder sb = new StringBuilder();
                StringBuilder sbLikeCond = new StringBuilder();
                if (selectedIngds != null && selectedIngds.size() > 0) {
                    for (int i = 0; i < selectedIngds.size(); i++) {
                        sbLikeCond.append(DatabaseHelper.COLUMN_DESC + " like " + "'%" + selectedIngds.get(i) + "%'");

                        if (i < selectedIngds.size() - 1) {
                            sbLikeCond.append(" OR ");
                        }
                    }
                    sb.append(sbLikeCond);
                    mSelection = "(" + sb.toString() + ")";
                    Float calories = prefs.getFloat("calCnt", 0);
                    if (calories > 0) {
                        mSelectionCalories = " AND " + DatabaseHelper.COLUMN_COUNT + "<" + 250;
                        mSelection = mSelection.concat(mSelectionCalories);
                    }
                } else if (selectedIngds.size() == 0 && mSelection.isEmpty()) {
                    Toast.makeText(getActivity(), "Please select atleast one ingredient.", Toast.LENGTH_LONG).show();
                    return;
                }
                Cursor cursor = db.query(DatabaseHelper.TABLE_RECIPES, mFromColumns, mSelection, null, null, null, mOrderBy);
                if (cursor == null || cursor.getCount() <= 0) {
                    recipeMsg.setText("Sorry! There are no recipes matching your search criteria.\nPlease change the ingredients and search again.");
                    recipeMsg.setTextColor(Color.RED);
                    recipeMsg.setTextSize(18);
                    setListAdapter(null);
                } else {
                    mAdapter = new RecipeAdapter(getActivity(), cursor);
                    setListAdapter(mAdapter);
                    getRecipeProcedure(cursor);
                    recipeMsg.clearComposingText();
                    recipeMsg.setText("Healthy Recipes for you..");
                    recipeMsg.setTypeface(Typeface.SERIF, Typeface.BOLD);
                    recipeMsg.setTextColor(Color.parseColor("#ff649f1f"));
                    recipeMsg.setTextSize(22);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);
        String procedure = procMap.get(Integer.toString(pos));
        String recipeName = nameMap.get(Integer.toString(pos));

        Intent intent = new Intent(getActivity().getApplicationContext(), RecipeProcedureFragment.class);
        intent.putExtra("recpName", recipeName);
        intent.putExtra("recpProc", procedure);

        startActivity(intent);

    }

    public void getRecipeProcedure(Cursor cursor) {
        int i = 0;
        while (cursor.moveToNext()) {
            String procedure = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROCEDURE));
            String recipeName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            String ingredient = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESC));
            procMap.put(Integer.toString(i), procedure);
            nameMap.put(Integer.toString(i), recipeName);
            ingredientMap.put(Integer.toString(i), ingredient);
            i++;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long arg3) {
                Toast.makeText(getActivity(), "Item " + pos + " was long clicked", Toast.LENGTH_SHORT).show();
                recipeName = nameMap.get(Integer.toString(pos));
                ingredient = ingredientMap.get(Integer.toString(pos));
                procedure = procMap.get(Integer.toString(pos));
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Liked the recipe??!!");
                alert.setMessage("Do you want the recipe to be added to the Favorites?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //adding all the seleted items to the list

                        Set<String> set = prefs.getStringSet("fItem", s);
                        nameList = new ArrayList<String>(set);
                        if (nameList.size() > 0) {
                            for (int i = 0; i < nameList.size(); i++) {
                                if (!nameList.get(i).equals("No Favourites Selected")) {
                                    favRecipeList.add(nameList.get(i));
                                }
                            }
                        }
                        favRecipeList.add(recipeName);
                        editor.putStringSet("fItem", favRecipeList);
                        favIngredientList.add(ingredient);
                        editor.putStringSet("fIngredientItem", favIngredientList);
                        favProcedureList.add(procedure);
                        editor.putStringSet("fProcedureItem", favProcedureList);
                        editor.commit();
                        Toast.makeText(getActivity().getApplicationContext(), "Recipe added to Favourites!!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });
    }
}
