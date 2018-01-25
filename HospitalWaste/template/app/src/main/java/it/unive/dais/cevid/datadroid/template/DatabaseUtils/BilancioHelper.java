package it.unive.dais.cevid.datadroid.template.DatabaseUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.Bilancio;

/**
 * Created by francescobenvenuto on 18/01/2018.
 */

public class BilancioHelper {
    private static DBHelper dbHelper;

    public BilancioHelper() {
        if (dbHelper == null) {
            dbHelper = DBHelper.getSingleton();
        }
    }


    /**
     * genera una lista di DatiConfrontoContainer che rappresentano per ogni voce di bilancio in comune tra tutte
     * le ULSS quanto ognuna ha speso per quello specifico dato
     */
    public List<DatiConfrontoContainer> getConfrontoMultiploDati(Collection<String> codiciEnte, int anno) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        FactoryDatiConfronto factory = new FactoryDatiConfronto();

        String query = "SELECT descrizione_codice, importo " +
                "FROM Bilancio " +
                "WHERE codice_ente = ? and anno = ? and importo != 0";

        for (String codiceEnte : codiciEnte) {
            Cursor cursorBilancio = db.rawQuery(query, new String[]{codiceEnte, anno + ""});
            cursorBilancio.moveToFirst();

            while (!cursorBilancio.isAfterLast()) {
                String voceBilancio = cursorBilancio.getString(0);
                Double importo = Double.parseDouble(cursorBilancio.getString(1));

                DatiConfrontoContainer tempContainer = factory.getDataContainer(voceBilancio);
                tempContainer.putData(codiceEnte, importo);

                cursorBilancio.moveToNext();
            }

        }

        db.close();
        return factory.genList(codiciEnte.size());
    }

    private class FactoryDatiConfronto {
        Map<String, DatiConfrontoContainer> factoryMap;

        private FactoryDatiConfronto() {
            factoryMap = new HashMap<>();
        }

        private DatiConfrontoContainer getDataContainer(String voceBilancio) {
            if (!factoryMap.containsKey(voceBilancio)) {
                factoryMap.put(voceBilancio, new DatiConfrontoContainer(voceBilancio));
            }
            return factoryMap.get(voceBilancio);
        }

        //get all the DatiConfrontoContainer that have at least sizeRequired ULSS
        private List<DatiConfrontoContainer> genList(int sizeRequired) {
            List<DatiConfrontoContainer> result = new LinkedList();

            for (String voceDiBilancio : factoryMap.keySet()) {

                DatiConfrontoContainer tempData = factoryMap.get(voceDiBilancio);
                if (tempData.size() == sizeRequired) {
                    result.add(tempData);
                }
            }
            return result;
        }
    }

    public class DatiConfrontoContainer {
        String voceBilancio;
        Map<String, Double> ulssImporto;

        private int size() {
            return ulssImporto.size();
        }

        private DatiConfrontoContainer(String voceBilancio) {
            this.voceBilancio = voceBilancio;

            ulssImporto = new HashMap<>();
        }

        private void putData(String codiceEnte, Double importo) {
            ulssImporto.put(codiceEnte, importo);
        }

        public Double getImporto(String codiceEnte) {
            return ulssImporto.get(codiceEnte);
        }

        public String getVoceBilancio() {
            return voceBilancio;
        }
    }

    public List<Bilancio> getVociBilancio(String codiceEnte) {
        return getVociBilancio(codiceEnte, "");
    }

    public List<Bilancio> getVociBilancio(String codiceEnte, String anno) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Bilancio> vociBilancio = new LinkedList();

        // if annoSet = 0 then al the year will be take
        String annoSet = "0";
        if(anno.isEmpty()){
            annoSet = "1";
        }
        String query = "SELECT * from Bilancio where codice_ente = ? and importo != 0 and (anno = ? or ?) ORDER BY importo DESC";
        Cursor cur = db.rawQuery(query, new String[]{codiceEnte, anno, annoSet});
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            vociBilancio.add(
                    new Bilancio(
                            cur.getString(0),
                            cur.getString(1),
                            Integer.parseInt(cur.getString(2)),
                            cur.getString(3),
                            Double.parseDouble(cur.getString(4))
                    )
            );
        }
        cur.close();
        return vociBilancio;
    }

    public List<String> getDescrizioneCodici () {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<String> descrizioneSpesa = new LinkedList<>();

        String query = "SELECT DISTINCT descrizione_codice from Bilancio";
        Cursor cur = db.rawQuery(query, null);
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            descrizioneSpesa.add(cur.getString(0));
        }
        cur.close();
        return descrizioneSpesa;
    }

    public List<String> filteredULSS (List<String> descrizioni) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<String> codiciEnti = new LinkedList<>();
        String joinString = joinString(descrizioni);

        String query = "SELECT DISTINCT codice_ente from Bilancio WHERE descrizione_codice IN (" + joinString + ") AND importo != 0;";
        Cursor cur = db.rawQuery(query, null);

        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            codiciEnti.add(cur.getString(0));
        }
        cur.close();
        return codiciEnti;
    }

    private String joinString (List<String> listString) {
        String out = new String();
        for (String s : listString)
            out = "'" + s + "', ";
        return out.substring(0, out.length() - 2);
    }

}
