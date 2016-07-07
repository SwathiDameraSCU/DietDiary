package com.mobileapp.finalproject.dietdiary;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Swathi on 5/21/2015.
 */

public class RecipeProcedureFragment extends ActionBarActivity {
    TextView textViewHeading;
    TextView textViewRecipeName;
    TextView ingredientsLabel;
    TextView textIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe_procedure);
        textViewHeading = (TextView) findViewById(R.id.recpProc);
        textViewRecipeName = (TextView) findViewById(R.id.recpName);
        ingredientsLabel = (TextView) findViewById(R.id.ingrInfo);
        textIngredients = (TextView) findViewById(R.id.recpIngrd);
        Bundle bundle = getIntent().getExtras();
        textViewHeading.setText(bundle.getString("recpProc"));
        textViewRecipeName.setText(bundle.getString("recpName"));
        if (bundle.getString("fromFragment") != null && "favourites".equals(bundle.getString("fromFragment"))) {
            ingredientsLabel.setText("Ingredients");
            textIngredients.setText(bundle.getString("recpIngrd"));
        } else {
            ingredientsLabel.setVisibility(View.INVISIBLE);
            textIngredients.setVisibility(View.INVISIBLE);
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006600")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_web_search:
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                startActivity(intent);
                break;
            case R.id.action_uninstall:
                Uri packageURI = Uri.parse("package:com.mobileapp.finalproject.dietdiary");
                Intent intent1 = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI);
                startActivity(intent1);
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
