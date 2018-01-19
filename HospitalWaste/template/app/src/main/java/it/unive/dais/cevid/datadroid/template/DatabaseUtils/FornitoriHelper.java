package it.unive.dais.cevid.datadroid.template.DatabaseUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by francescobenvenuto on 18/01/2018.
 */

public class FornitoriHelper {
    private DBHelper dbHelper;

    public FornitoriHelper(){
        dbHelper = DBHelper.getSingleton();
    }

    public List<String> getFornitori() {
        List<String> result = new LinkedList();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT DISTINCT aggiudicatario " +
                "FROM Appalti";

        Cursor cursorFornitori = db.rawQuery(query, new String[]{});
        cursorFornitori.moveToFirst();

        while (!cursorFornitori.isAfterLast()) {
            result.add(cursorFornitori.getString(0));

            cursorFornitori.moveToNext();
        }

        db.close();
        return result;
    }

    public List<String> getUlssFornite(String fornitore){
        List<String> result = new LinkedList();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT DISTINCT u.denominazione " +
                "FROM Appalti a join ULSS u " +
                "WHERE a.aggiudicatario = ? and a.codice_ente = u.codice_ente and a.importo != 0";

        Cursor cursorUlss = db.rawQuery(query, new String[]{fornitore});
        cursorUlss.moveToFirst();

        while (!cursorUlss.isAfterLast()) {
            result.add(cursorUlss.getString(0));

            cursorUlss.moveToNext();
        }

        db.close();
        return result;
    }
}
