package it.unive.dais.cevid.datadroid.template.DatabaseUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by francescobenvenuto on 18/01/2018.
 */

public class FornitoriHelper {
    private DBHelper dbHelper;

    private List<String> fornitori = Collections.EMPTY_LIST;

    public FornitoriHelper(){
        dbHelper = DBHelper.getSingleton();
    }

    public List<String> getFornitori() {
        if (fornitori != Collections.EMPTY_LIST)
            return fornitori;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT DISTINCT aggiudicatario " +
                "FROM Appalti";

        Cursor cursorFornitori = db.rawQuery(query, new String[]{});
        cursorFornitori.moveToFirst();

        while (!cursorFornitori.isAfterLast()) {
            fornitori.add(cursorFornitori.getString(0));

            cursorFornitori.moveToNext();
        }

        db.close();
        return fornitori;
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
