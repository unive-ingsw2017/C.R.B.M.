package it.unive.dais.cevid.datadroid.template.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.datadroid.lib.parser.AppaltiParser;
import it.unive.dais.cevid.datadroid.lib.parser.CsvRowParser;
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
    private static final int MAX_DAYS = 15;
    
    private static DBHelper instance = null;
    private Context context;


    //Singleton
    private DBHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, 3);
        this.context = context.getApplicationContext();

        // check if the DB is old
        updateDatabase();
    }

    private void updateDatabase() {
        SQLiteDatabase db = null;
        try{
            db = SQLiteDatabase.openDatabase(DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
            db.close();

            if(databaseIsOld()){
                deleteDatabase();
            }
        }
        catch(SQLiteException e){ //the database doesn't exist
            Log.d("Database", "the couldn't be old because not exist yet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean databaseIsOld() throws IOException {
        DataInputStream dataInputStream = new DataInputStream(context.openFileInput("db_creation_time.txt"));

        long lastTime = dataInputStream.readLong();
        long currentTime = System.currentTimeMillis();

        dataInputStream.close();
        // cannot use LocalDate because the API level TODO migliorare
        long diffTime = (currentTime - lastTime)/ (1000 * 60 * 60 * 24);// diff time in days
        if(diffTime > MAX_DAYS){
            return true;
        }
        return false;


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

            if(!insertStmt.equals("")) {//TODO cambiare sta ....
                db.execSQL(insertStmt);
            }
        }
        insertReader.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            this.insertFromFile(db);

            //to insert vodi di appalto for the ULSS
            List<ULSS> ulssList = getULSS(db);
            for(ULSS ulss: ulssList) {
                SoldipubbliciParser soldipubbliciParser = new SoldipubbliciParser("SAN", ulss.getCodiceEnte());
                soldipubbliciParser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                List<SoldipubbliciParser.Data> soldipubbliciList = new ArrayList<>(soldipubbliciParser.getAsyncTask().get());

                insertVociBilancio(db, soldipubbliciList);
            }

            // to insert the appalti for the ULSS
            Map<String,List<URL>> appalti = takeUrlFromFile();
            for (String codiceEnte : appalti.keySet()) {
                List<URL> urlList = appalti.get(codiceEnte);
                AppaltiParser appaltiParser = new AppaltiParser(urlList);
                appaltiParser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                List<AppaltiParser.Data> appaltiList = new ArrayList<>(appaltiParser.getAsyncTask().get());
                insertAppalti(db, appaltiList, codiceEnte);
            }

            // serialize date of creation object to a file
            Date currentDate = new Date();

            DataOutputStream dataOutputStream = new DataOutputStream(context.openFileOutput("db_creation_time.txt", Context.MODE_PRIVATE));
            dataOutputStream.writeLong(currentDate.getTime());
            dataOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Map<String,List<URL>> takeUrlFromFile() throws InterruptedException, ExecutionException{
        HashMap<String,List<URL>> map = new HashMap<>();
        InputStream is = context.getResources().openRawResource(R.raw.link_appalti);
        CsvRowParser csvRowParser = new CsvRowParser(new InputStreamReader(is), true, ";");
        List<CsvRowParser.Row> rows = csvRowParser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        for (final CsvRowParser.Row r : rows) {
            String codiceEnte = r.get("ULSS");
            try {
                if (map.containsKey(codiceEnte)) {
                    map.get(codiceEnte).add(new URL(r.get("link")));
                } else {
                    List<URL> l = new LinkedList<>();
                    l.add(new URL(r.get("link")));
                    map.put(codiceEnte, l);
                }
            }
            catch (MalformedURLException e){
                e.printStackTrace();
                Log.e("URLError",r.get("link"));
            }
        }
        return map;
    }


    public List<ULSS> getULSS() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ULSS", null);
        List<ULSS> ulssList = fetchULSS(cursor);
        db.close();
        return ulssList;
    }

    private List<ULSS> getULSS(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM ULSS", null);
        return fetchULSS(cursor);
    }

    private List fetchULSS (Cursor cursor) {
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
            cursor.moveToNext();

        }
        cursor.close();
        return ulssList;
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

    public void deleteDatabase(){
        context.deleteDatabase(DATABASE_NAME); // remove the database
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

    private void insertVociBilancio(SQLiteDatabase db, List<SoldipubbliciParser.Data> vociBilancio) {
        List<Bilancio> l = new ArrayList<>();

        for (SoldipubbliciParser.Data voceBilancio : vociBilancio) {
            l.addAll(createVociBilancio(voceBilancio));
        }

        ContentValues values = new ContentValues();
        for (Bilancio bilancio : l) {
            values.put("codice_siope", bilancio.getCodiceSiope());
            values.put("codice_ente", bilancio.getCodiceEnte());
            values.put("anno", bilancio.getAnno());
            values.put("descrizione_codice", bilancio.getDescrizioneCodice());
            values.put("importo", bilancio.getImporto());

            db.insert("Bilancio", null, values);
        }
    }

    /**
     * inserisce nel database db gli appalti associandoli al codice dell'ente corrispondente
     * @param db
     * @param appalti
     * @param codice_ente
     */
    private void insertAppalti(SQLiteDatabase db, List<AppaltiParser.Data> appalti, String codice_ente) {
        List<Appalto> l = new ArrayList<>();

        for (AppaltiParser.Data appalto : appalti) {
            l.add(new Appalto(
                    appalto.cig,
                    appalto.oggetto,
                    appalto.sceltac,
                    appalto.codiceFiscaleAgg,
                    appalto.aggiudicatario,
                    Double.parseDouble(appalto.importo),
                    codice_ente
            ));
        }

        ContentValues values = new ContentValues();

        for (Appalto appalto : l) {
                values.put("cig", appalto.getCig());
                values.put("oggetto", appalto.getOggetto());
                values.put("scelta_contraente",appalto.getSceltaContraente());
                values.put("codice_fiscale_aggiudicatario", appalto.getCodice_fiscale_aggiudicatario());
                values.put("aggiudicatario", appalto.getAggiudicatario());
                values.put("importo", appalto.getImporto());
                values.put("codice_ente", appalto.getCodiceEnte());
            try{
                db.insertOrThrow("Appalti", null, values);
            }
            catch (android.database.sqlite.SQLiteConstraintException e){
                Cursor cursor = db.rawQuery("SELECT importo FROM Appalti WHERE cig=? AND oggetto=? AND codice_fiscale_aggiudicatario=?",new String[]{appalto.getCig(),appalto.getOggetto(),appalto.getCodice_fiscale_aggiudicatario()});
                cursor.moveToFirst();
                double old_importo = cursor.getDouble(0);
                cursor.close();
                db.rawQuery("UPDATE Appalti SET importo=? WHERE cig=? AND oggetto=? AND codice_fiscale_aggiudicatario=? ;",new String[]{new Double(old_importo+appalto.getImporto()).toString(),appalto.getCig(),appalto.getOggetto(),appalto.getCodice_fiscale_aggiudicatario()});
            }
        }
    }

    // TODO: Inserire metodi query
}
