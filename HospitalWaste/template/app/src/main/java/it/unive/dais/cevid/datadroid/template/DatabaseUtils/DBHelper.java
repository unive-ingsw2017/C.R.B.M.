package it.unive.dais.cevid.datadroid.template.DatabaseUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import it.unive.dais.cevid.datadroid.template.R;
import it.unive.dais.cevid.datadroid.template.ULSS_stuff.ULSS;

/**
 * Created by gianmarcocallegher on 04/01/18.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HospitalWaste.db";

    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }


    private void insertFromFile(SQLiteDatabase db) throws IOException {
        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(R.raw.HospitalWasteInit);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and there's no other stuff)
        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();
            db.execSQL(insertStmt);
        }
        insertReader.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            this.insertFromFile(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db);
        onCreate(db);
    }

    private void deleteTables(SQLiteDatabase db) {
        List<String> tables = new LinkedList();
        tables.add("ULSS");
        tables.add("Bilancio");
        tables.add("Appalti");


        // call DROP TABLE on every table name
        for (String table : tables) {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }
    }

    public List<ULSS> getAllULSS(SQLiteDatabase db){
        return null;
    }


    // TODO: Inserire metodi query
}
