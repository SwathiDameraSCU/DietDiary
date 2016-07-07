package com.mobileapp.finalproject.dietdiary.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Aasawari on 6/2/15.
 */
public class HistoryDatabaseHelper extends SQLiteOpenHelper {
    //Data History table Name
    public static final String TABLE_HISTORY = "data_history";

    //Data History table Column Names
    public static final String COLUMN_DATE = "countDate";
    public static final String COLUMN_STEPCOUNT = "stepCount";
    public static final String COLUMN_TIME = "time";
    //DatabaseName
    private static final String DATABASE_NAME = "dietDiary.db";

    // Database version
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    // Create History Table
    String DATABASE_CREATE = "CREATE TABLE " +
            TABLE_HISTORY + "("
            + COLUMN_DATE + " TEXT, "
            + COLUMN_STEPCOUNT + " INTEGER, "
            + COLUMN_TIME + " INTEGER )";

    public HistoryDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(HistoryDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
        //createTestData();
    }

    public int getCountNum(){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(new Date());

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_STEPCOUNT + " FROM " + TABLE_HISTORY +
                        " where " +
                        COLUMN_DATE+" ='" + dateStr +"'";

        Cursor c = db.rawQuery(query, null);

        if (c != null ) {
            if (c.moveToFirst()) {
                    return c.getInt(0);
            }
        }
        return 0;
    }

    public List<HistoryDataVO> getPreviousDayHistory(){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(new Date());

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_STEPCOUNT + ", "
                + COLUMN_DATE + ", " + COLUMN_TIME + " FROM " + TABLE_HISTORY +
                " ORDER BY " +
                COLUMN_TIME  + " DESC LIMIT 6";

        Cursor c =  db.rawQuery(query, null);
        List<HistoryDataVO> list = new ArrayList<HistoryDataVO>();
        int index =0;
        if(c.moveToFirst()){
            do{
                HistoryDataVO vo = new HistoryDataVO();
                vo.setStepCount(c.getInt(0));
                vo.setDateStr(c.getString(1));
                list.add(index, vo);
                index++;
            }while (c.moveToNext());
        }
        return list;
    }

    public void upsertData(int stepCount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STEPCOUNT, stepCount);

        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(new Date());

        long epoch = System.currentTimeMillis() / 1000;
        String selection = COLUMN_DATE+"='" + dateStr + "'";

        int rowCount = db.update(TABLE_HISTORY, values, selection, null);
        if(rowCount == 0){
            values.put(COLUMN_DATE, dateStr);
            values.put(COLUMN_TIME, (int)epoch);
            db.insert(TABLE_HISTORY,COLUMN_DATE,values);
        }
    }

    public void createTestData(){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STEPCOUNT, 20000);
        values.put(COLUMN_DATE, "2015-06-02");
        values.put(COLUMN_TIME, 5);
        long l = db.insert(TABLE_HISTORY, COLUMN_DATE, values);

        ContentValues values1 = new ContentValues();
        values1.put(COLUMN_STEPCOUNT, 1000);
        values1.put(COLUMN_DATE, "2015-06-01");
        values1.put(COLUMN_TIME, 4);
        l = db.insert(TABLE_HISTORY, COLUMN_DATE, values1);

        ContentValues values2 = new ContentValues();
        values2.put(COLUMN_STEPCOUNT, 1000);
        values2.put(COLUMN_DATE, "2015-05-31");
        values2.put(COLUMN_TIME, 3);
        l = db.insert(TABLE_HISTORY, COLUMN_DATE, values2);

        ContentValues values3 = new ContentValues();
        values3.put(COLUMN_STEPCOUNT, 1000);
        values3.put(COLUMN_DATE, "2015-05-30");
        values3.put(COLUMN_TIME, 2);
        l = db.insert(TABLE_HISTORY, COLUMN_DATE, values3);

        ContentValues values4 = new ContentValues();
        values4.put(COLUMN_STEPCOUNT, 1000);
        values4.put(COLUMN_DATE, "2015-05-29");
        values4.put(COLUMN_TIME, 1);
        l = db.insert(TABLE_HISTORY, COLUMN_DATE, values4);
    }

}
