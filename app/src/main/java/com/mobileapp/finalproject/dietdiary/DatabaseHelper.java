package com.mobileapp.finalproject.dietdiary;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swathi on 5/15/2015.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //Recipes table Name
    public static final String TABLE_RECIPES = "recipes";

    //Recipes table Column Names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "rName";
    public static final String COLUMN_DESC = "rDesc";
    public static final String COLUMN_COUNT = "rCalCount";
    public static final String COLUMN_PROCEDURE = "rProc";

    //DatabaseName
    private static final String DATABASE_NAME = "dietDiary.db";
    // Database version
    private static final int DATABASE_VERSION = 1;

    private final Context fContext;
    // Create Recipes Table
    String DATABASE_CREATE = "CREATE TABLE " +
            TABLE_RECIPES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_DESC + " TEXT, "
            + COLUMN_COUNT + " INTEGER, "
            + COLUMN_PROCEDURE + " TEXT )";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fContext = context;
    }

    /**
     * Inserting bulk recipes into recipes table *
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);

    }

    private String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuffer stringBuffer = new StringBuffer();
        //int rId = 0;
        sqLiteDatabase.execSQL(DATABASE_CREATE);
        XmlResourceParser _xml = null;
        try {
            //Add default records to animals
            ContentValues _Values = new ContentValues();
            //Get xml resource file
            Resources res = fContext.getResources();
            //Open xml file
            _xml = res.getXml(R.xml.recipe_data);
            _xml.next();

            while (_xml.next() != XmlPullParser.END_TAG) {
                if (_xml.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = _xml.getName();
                if (name.equals("recipe")) {
                    String rName = null, rDesc = null, rProc = null;
                    int rCalCount = 0;
                    // _Values.put( COLUMN_ID, rId++);
                    while (_xml.next() != XmlPullParser.END_TAG) {
                        if (_xml.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        name = _xml.getName();
                        if (name.equals("rName")) {
                            rName = readText(_xml);
                            _Values.put(COLUMN_NAME, rName);
                        } else if (name.equals("rDesc")) {
                            rDesc = readText(_xml);
                            _Values.put(COLUMN_DESC, rDesc);
                        } else if (name.equals("rCalCount")) {
                            String cnt = readText(_xml);
                            rCalCount = Integer.valueOf(cnt);
                            _Values.put(COLUMN_COUNT, rCalCount);
                        } else if (name.equals("rProc")) {
                            rProc = readText(_xml);
                            _Values.put(COLUMN_PROCEDURE, rProc);
                        }
                    }
                    sqLiteDatabase.insert(TABLE_RECIPES, null, _Values);
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Close the xml file
            _xml.close();
            //sqLiteDatabase.close();
        }

    }
}
