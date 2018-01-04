package it.unive.dais.cevid.datadroid.template.DatabaseUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by gianmarcocallegher on 04/01/18.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HospitalWaste.db";

    private HashMap hp;
    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }


    public int insertFromFile(SQLiteDatabase db, Context context) throws IOException {
        // Reseting Counter
        int result = 0;

        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(R.raw.HospitalWasteInit);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();
            db.execSQL(insertStmt);
            result++;
        }
        insertReader.close();

        // returning number of inserted rows
        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            this.insertFromFile(db, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /// TODO: DROP TABLE
        onCreate(db);
    }

    public boolean initDB(String name, String path) {
        // TODO: Inizializzare ULSS (opzionale dati sul file)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        db.insert("ULSS", null, contentValues);
        return true;
    }

    // TODO: Inserire metodi query
}
