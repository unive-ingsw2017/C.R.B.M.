package it.unive.dais.cevid.datadroid.template.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.datadroid.lib.parser.AppaltiParser;
import it.unive.dais.cevid.datadroid.lib.parser.SoldipubbliciParser;
import it.unive.dais.cevid.datadroid.template.R;
import it.unive.dais.cevid.datadroid.template.ULSS_stuff.Appalto;
import it.unive.dais.cevid.datadroid.template.ULSS_stuff.Bilancio;
import it.unive.dais.cevid.datadroid.template.ULSS_stuff.ULSS;

/**
 * Created by gianmarcocallegher on 04/01/18.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HospitalWaste.db";

    private static DBHelper instance = null;
    private Context context;


    //Singleton
    private DBHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, 1);
        this.context = context.getApplicationContext();
    }

    public static DBHelper getSingleton(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public static DBHelper getSingleton() {
        if (instance == null) {
            throw new IllegalArgumentException();
        }
        return instance;
    }

    private void insertFromFile(SQLiteDatabase db) throws IOException {
        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(R.raw.hospital_waste_init);
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
            List<ULSS> ulssList = getULSS();

            for(ULSS ulss: ulssList) {
                SoldipubbliciParser parserSoldiPubblici = new SoldipubbliciParser("SAN", ulss.getCodiceEnte());
                List<SoldipubbliciParser.Data> l = new ArrayList<>(parserSoldiPubblici.getAsyncTask().get());
                Log.e("AAA", "BBB");
                insertVociBilancio(l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public List<ULSS> getULSS() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ULSS", null);
        List<ULSS> ulssList = new LinkedList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ulssList.add(
                    new ULSS(
                            cursor.getString(1),
                            cursor.getString(0),
                            cursor.getString(5),
                            new LatLng(
                                    cursor.getDouble(2),
                                    cursor.getDouble(3)
                            ),
                            cursor.getInt(4)
                    )
            );

        }
        cursor.close();
        return ulssList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables();
        onCreate(db);
    }

    private void deleteTables() {
        List<String> tables = new LinkedList();
        tables.add("ULSS");
        tables.add("Bilancio");
        tables.add("Appalti");

        SQLiteDatabase db = getWritableDatabase();


        // call DROP TABLE on every table name
        for (String table : tables) {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }

        db.close();
    }

    /**
     * prende un istanza della classe SoldipubbliciParser.Data e crea un array di Bilancio
     * ed utilizza un altro metodo per inserire oggetti di tipo Bilancio
     *
     * @param bilancioData
     */
    private List createVociBilancio(SoldipubbliciParser.Data bilancioData) {
        List<Bilancio> vociBilancio = new LinkedList();
        Bilancio bilancio_2013 = new Bilancio(
                bilancioData.codice_siope,
                bilancioData.cod_ente,
                2013,
                bilancioData.descrizione_codice,
                Double.parseDouble(bilancioData.importo_2013)
        );

        vociBilancio.add(bilancio_2013);

        vociBilancio.add(
                bilancio_2013.generateNewBilancio(
                        2014,
                        Double.parseDouble(bilancioData.importo_2014)
                )
        );
        vociBilancio.add(
                bilancio_2013.generateNewBilancio(
                        2015,
                        Double.parseDouble(bilancioData.importo_2015)
                )
        );
        vociBilancio.add(
                bilancio_2013.generateNewBilancio(
                        2016,
                        Double.parseDouble(bilancioData.importo_2016)
                )
        );
        vociBilancio.add(
                bilancio_2013.generateNewBilancio(
                        2017,
                        Double.parseDouble(bilancioData.importo_2017)
                )
        );

        return vociBilancio;
    }

    public void insertVociBilancio(List<SoldipubbliciParser.Data> vociBilancio) {
        List<Bilancio> l = new ArrayList<>();

        for (SoldipubbliciParser.Data voceBilancio : vociBilancio) {
            l.addAll(createVociBilancio(voceBilancio));
        }

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        for (Bilancio bilancio : l) {
            values.put("codice_siope", bilancio.getCodiceSiope());
            values.put("codice_ente", bilancio.getCodiceEnte());
            values.put("anno", bilancio.getAnno());
            values.put("descrizione_codice", bilancio.getDescrizioneCodice());
            values.put("importo", bilancio.getImporto());

            db.insert("Bilancio", null, values);
        }

        db.close();

    }

    /**
     * prende un appalto e l'ulss a cui si riferisce e crea un oggetto Appalto per inserire l'informazione
     * nella relativa tabella nel database
     * @param db
     * @param appaltoData
     * @param ulss
     */
    public void insertAppalto(SQLiteDatabase db, AppaltiParser.Data appaltoData, ULSS ulss) {
        Appalto appalto = new Appalto(
                appaltoData.cig,
                appaltoData.oggetto,
                appaltoData.codiceFiscaleAgg,
                appaltoData.aggiudicatario,
                Double.parseDouble(appaltoData.importo),
                ulss.getCodiceEnte()
                );

        insertAppalto(appalto);
    }

    private void insertAppalto(Appalto appalto) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("cig", appalto.getCig());
        values.put("oggetto", appalto.getOggetto());
        values.put("codice_fiscale_aggiudicatario", appalto.getCodice_fiscale_aggiudicatario());
        values.put("aggiudicatario", appalto.getAggiudicatario());
        values.put("importo", appalto.getImporto());
        values.put("codice_ente", appalto.getCodiceEnte());

        db.insert("Bilancio", null, values);

        db.close();

    }


    // TODO: Inserire metodi query
}
