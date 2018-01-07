package it.unive.dais.cevid.datadroid.template.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

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

    /**
     * prende un istanza della classe SoldipubbliciParser.Data e crea un array di Bilancio
     * ed utilizza un altro metodo per inserire oggetti di tipo Bilancio
     *
     * @param db
     * @param bilancioData
     */
    public void insertBilancio(SQLiteDatabase db, SoldipubbliciParser.Data bilancioData) {
        List<Bilancio> bilanci = new LinkedList();
        Bilancio bilancio_2013 = new Bilancio(
                bilancioData.codice_siope,
                bilancioData.cod_ente,
                2013,
                bilancioData.descrizione_codice,
                Double.parseDouble(bilancioData.importo_2013)
        );

        bilanci.add(bilancio_2013);

        bilanci.add(
                bilancio_2013.generateNewBilancio(
                        2014,
                        Double.parseDouble(bilancioData.importo_2014)
                )
        );
        bilanci.add(
                bilancio_2013.generateNewBilancio(
                        2015,
                        Double.parseDouble(bilancioData.importo_2015)
                )
        );
        bilanci.add(
                bilancio_2013.generateNewBilancio(
                        2016,
                        Double.parseDouble(bilancioData.importo_2016)
                )
        );
        bilanci.add(
                bilancio_2013.generateNewBilancio(
                        2017,
                        Double.parseDouble(bilancioData.importo_2017)
                )
        );

        insertBilanci(db, bilanci);
    }

    private void insertBilanci(SQLiteDatabase db, List<Bilancio> bilanci) {
        ContentValues values = new ContentValues();

        for (Bilancio bilancio : bilanci) {
            values.put("codice_siope", bilancio.getCodiceSiope());
            values.put("codice_ente", bilancio.getCodiceEnte());
            values.put("anno", bilancio.getAnno());
            values.put("descrizione_codice", bilancio.getDescrizioneCodice());
            values.put("importo", bilancio.getImporto());

            db.insert("Bilancio", null, values);
        }

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
                appaltoData.aggiudicatario,
                appaltoData.codiceFiscaleAgg,
                appaltoData.proponente,
                Double.parseDouble(appaltoData.importo),
                ulss.getCodiceEnte()
                );

        insertAppalto(db, appalto);
    }

    private void insertAppalto(SQLiteDatabase db, Appalto appalto) {
        ContentValues values = new ContentValues();

        values.put("cig", appalto.getCig());
        values.put("oggetto", appalto.getOggetto());
        values.put("aggiudicatario", appalto.getAggiudicatario());
        values.put("codice_fiscale", appalto.getCodiceFiscale());
        values.put("ragione_sociale", appalto.getRagioneSociale());
        values.put("importo", appalto.getImporto());
        values.put("codice_ente", appalto.getCodiceEnte());

        db.insert("Bilancio", null, values);

    }


    // TODO: Inserire metodi query
}
