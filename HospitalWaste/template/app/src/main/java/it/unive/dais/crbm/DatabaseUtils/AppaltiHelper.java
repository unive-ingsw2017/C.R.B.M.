package it.unive.dais.crbm.DatabaseUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import it.unive.dais.crbm.DatiAppalti.Appalto;

/**
 * Created by francescobenvenuto on 18/01/2018.
 */

public class AppaltiHelper {
    private static DBHelper dbHelper = null;

    public AppaltiHelper() {
        if (dbHelper == null) {
            dbHelper = DBHelper.getSingleton();
        }
    }

    public List<Appalto> getAppalti(String codiceEnte) {
        return getAppaltiByFornitore(codiceEnte, "");
    }


    public List<Appalto> getAppaltiByFornitore(String codiceEnte, String fornitore) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Appalto> appalti = new LinkedList();

        String query = "SELECT * from Appalti where codice_ente = ? and importo != 0 and (aggiudicatario = ? or ?) ORDER BY importo DESC";

        // if fornitoreSet = 0 then aggiudicatario = ? must be true
        String fornitoreSet = "0";
        if(fornitore.isEmpty()){
            fornitoreSet = "1";
        }
        Cursor cur = db.rawQuery(query, new String[]{codiceEnte, fornitore, fornitoreSet});
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            appalti.add(
                    new Appalto(
                            cur.getString(0),
                            cur.getString(1),
                            cur.getString(2),
                            cur.getString(3),
                            cur.getString(4),
                            Double.parseDouble(cur.getString(5)),
                            cur.getString(6)
                    )
            );
        }
        cur.close();
        return appalti;
    }
}
