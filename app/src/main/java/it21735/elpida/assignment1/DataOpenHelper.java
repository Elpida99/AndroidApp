package it21735.elpida.assignment1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DataHelper";
    private static final int DB_VERSION = 1;
    public static final String TABLE = "DATA";
    public static final String KEY_ID = "ID";
    public static final String KEY_FNAME = "FNAME";
    public static final String KEY_LNAME ="LNAME";
    public static final String KEY_AGE = "AGE";

    private static final String CREATE_QUERY = "CREATE TABLE "+TABLE+" ("+KEY_ID+" INT, "+KEY_FNAME+" TEXT, "+KEY_LNAME+" TEXT, "+KEY_AGE+" INT);";


    public DataOpenHelper(@Nullable Context context) {
        super(context,DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}



