package it.unive.dais.crbm.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import Parser.SoldiPubbliciBilancioData;
import Parser.XMLAppaltiParser;
import it.unive.dais.crbm.DatiAppalti.Appalto;
import it.unive.dais.crbm.DatiDiBilancio.Bilancio;
import it.unive.dais.crbm.DatiULSS.ULSS;
import it.unive.dais.crbm.R;

/**
 * Created by gianmarcocallegher on 04/01/18.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HospitalWaste.db";
    private static final int MAX_DAYS = 15;
    private static final int DATABSE_VERSION = 10;
    private static final int ANNO_APPALTI = 2016;

    private static DBHelper instance = null;
    private Context context;

    private List<ULSS> ulss = Collections.EMPTY_LIST;
    private Map<String, List<String>> ospedali = Collections.EMPTY_MAP;

    //Singleton
    private DBHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABSE_VERSION);
        this.context = context.getApplicationContext();
        // check if the DB is old
        updateDatabase();
    }

    public static int getDatabseVersion() {
        return DATABSE_VERSION;
    }

    private void updateDatabase() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (dbFile.exists() && isOldDatabase()) {
            deleteDatabase();
        }

    }


    private long getCreationTimestamp() {
        long lastTime = 0L;
        try {
            DataInputStream dataInputStream = new DataInputStream(context.openFileInput("db_creation_time.txt"));
            lastTime = dataInputStream.readLong();

            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lastTime;
    }

    public String getCreationDateString() {
        String result = "N.D.";

        Calendar cal = getTimestampCal(getCreationTimestamp());
        result = DateFormat.format("dd-MM-yyyy HH:mm", cal).toString();

        return result;
    }

    private Calendar getTimestampCal(long timestamp) {
        Calendar timeStampCal = Calendar.getInstance(Locale.ITALIAN);
        timeStampCal.setTimeInMillis(timestamp);

        return timeStampCal;
    }

    private boolean isOldDatabase() {
        long creationTime = getCreationTimestamp();
        long currentTime = System.currentTimeMillis();

        // cannot use LocalDate because of the API level
        long diffTimeinDays = (currentTime - creationTime) / (24 * 60 * 60 * 1000); // get the diff in days
        if (diffTimeinDays > MAX_DAYS) {
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
            throw new IllegalStateException();
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

            if (!insertStmt.equals("")) {
                db.execSQL(insertStmt);
            }
        }
        insertReader.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            this.insertFromFile(db);

            //to insert voci di appalto for the ULSS
            List<ULSS> ulssList = getULSS(db);
            for (ULSS ulss : ulssList) {
                SoldiPubbliciBilancioData soldiPubbliciBilancioData = new SoldiPubbliciBilancioData("SAN", ulss.getCodiceEnte());
                List<Bilancio> vociBilancio = soldiPubbliciBilancioData.getBilancio();

                insertVociBilancio(db, vociBilancio);
            }

            // to insert the appalti for the ULSS
            Map<String, List<URL>> appalti = takeUrlFromFile();
            for (String codiceEnte : appalti.keySet()) {
                List<URL> urlList = appalti.get(codiceEnte);

                XMLAppaltiParser appaltiParser = XMLAppaltiParser.getInstance();
                List<Appalto> partialResult = appaltiParser.parseAppalti(urlList, codiceEnte);
                insertAppalti(db, partialResult, codiceEnte);
            }

            // serialize date of creation object to a file
            Date currentDate = new Date();

            //TODO check if the file is open in append mode or like erase everything and write
            DataOutputStream dataOutputStream = new DataOutputStream(context.openFileOutput("db_creation_time.txt", Context.MODE_PRIVATE));
            dataOutputStream.writeLong(currentDate.getTime());
            dataOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Map<String, List<URL>> takeUrlFromFile() throws InterruptedException, ExecutionException, IOException {
        HashMap<String, List<URL>> map = new HashMap<>();

        InputStreamReader reader = new InputStreamReader(
                context.getResources().openRawResource(R.raw.link_appalti)
        );

        CSVParser csvParser = new CSVParser(
                reader,
                CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim()
                        .withQuote('\'')
        );

        for (CSVRecord row : csvParser.getRecords()) {
            String codiceEnte = row.get("ulss");
            try {
                if (map.containsKey(codiceEnte)) {
                    map.get(codiceEnte).add(new URL(row.get("link")));
                } else {
                    List<URL> l = new LinkedList<>();
                    l.add(new URL(row.get("link")));
                    map.put(codiceEnte, l);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("URLError", row.get("link"));
            }
        }
        return map;
    }


    public List<ULSS> getULSS() {
        if (ulss != Collections.EMPTY_LIST) {
            return ulss;
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ULSS", null);
        ulss = fetchULSS(cursor);
        db.close();

        return ulss;
    }

    public String getCodiceEnte(String ulssName) {
        for (ULSS ulss : getULSS()) {
            if (ulss.getDescrizione().equals(ulssName)) {
                return ulss.getCodiceEnte();
            }
        }
        return "";// ulssname is wrong
    }

    private List<ULSS> getULSS(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM ULSS", null);
        return fetchULSS(cursor);
    }

    private List fetchULSS(Cursor cursor) {
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

    public void deleteDatabase() {
        context.deleteDatabase(DATABASE_NAME); // remove the database
    }

    private void insertVociBilancio (SQLiteDatabase db, List<Bilancio> vociBilancio) {
        ContentValues values = new ContentValues();
        for (Bilancio bilancio : vociBilancio) {
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
     *
     * @param db
     * @param appalti
     * @param codice_ente
     */
    private void insertAppalti(SQLiteDatabase db, List<Appalto> appalti, String codice_ente) {
        ContentValues values = new ContentValues();

        for (Appalto appalto : appalti) {
            if (!appalto.getAggiudicatario().equals("Dati assenti o mal formattati")) {
                values.put("cig", appalto.getCig());
                values.put("oggetto", appalto.getOggetto());
                values.put("scelta_contraente", appalto.getSceltaContraente());
                values.put("codice_fiscale_aggiudicatario", appalto.getCodice_fiscale_aggiudicatario());
                values.put("aggiudicatario", appalto.getAggiudicatario());
                values.put("importo", appalto.getImporto());
                values.put("codice_ente", appalto.getCodiceEnte());
                try {
                    db.insertOrThrow("Appalti", null, values);
                } catch (android.database.sqlite.SQLiteConstraintException e) {

                    String query = "SELECT importo FROM Appalti WHERE cig=? AND oggetto=? AND codice_fiscale_aggiudicatario=? ;";
                    Cursor cursor = db.rawQuery(query, new String[]{appalto.getCig(), appalto.getOggetto(), appalto.getCodice_fiscale_aggiudicatario()});
                    cursor.moveToFirst();
                    double old_importo = cursor.getDouble(0);
                    cursor.close();

                    String updateQuery = "UPDATE Appalti SET importo=? WHERE cig=? AND oggetto=? AND codice_fiscale_aggiudicatario=? ;";
                    db.rawQuery(updateQuery, new String[]{new Double(old_importo + appalto.getImporto()).toString(), appalto.getCig(), appalto.getOggetto(), appalto.getCodice_fiscale_aggiudicatario()});
                }
            }
        }
    }


    //get the string of the ospedali_associati and split to have a list of ospedali_associati
    public List<String> getOspedali(String codiceEnte) {
        if (ospedali.containsKey(codiceEnte))
            return ospedali.get(codiceEnte);
        ospedali = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();
        List<String> ospedaliList = Collections.emptyList();

        String query = "SELECT ospedali_associati from ULSS where codice_ente = ?";
        Cursor cur = db.rawQuery(query, new String[]{codiceEnte});
        cur.moveToFirst();

        if (!cur.isAfterLast()) {
            ospedaliList = Arrays.asList(cur.getString(0).split(";"));
        }

        ospedali.put(codiceEnte, ospedaliList);
        cur.close();
        return ospedali.get(codiceEnte);
    }

    public CrossData getDatiIncrociati(String codiceEnte, String keyword) {
        return getDatiIncrociati(codiceEnte, keyword, ANNO_APPALTI);
    }

    public CrossData getDatiIncrociati(String codiceEnte, String keyword, int anno) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Appalto> appalti = new ArrayList<>();
        ArrayList<Bilancio> bilancio = new ArrayList<>();


        String queryBilancio = "SELECT * from Bilancio where codice_ente = ? AND importo != 0 AND descrizione_codice LIKE '%' || ? || '%' AND anno = ?";
        String queryAppalto = "SELECT * from Appalti where codice_ente = ? AND importo != 0 AND oggetto LIKE '%' || ? || '%'";

        Cursor cursorBilancio = db.rawQuery(queryBilancio, new String[]{codiceEnte, keyword, String.valueOf(anno)});
        Cursor cursorAppalti = db.rawQuery(queryAppalto, new String[]{codiceEnte, keyword});

        cursorBilancio.moveToFirst();
        cursorAppalti.moveToFirst();

        while (!cursorBilancio.isAfterLast()) {
            bilancio.add(
                    new Bilancio(
                            cursorBilancio.getString(0),
                            cursorBilancio.getString(1),
                            Integer.parseInt(cursorBilancio.getString(2)),
                            cursorBilancio.getString(3),
                            Double.parseDouble(cursorBilancio.getString(4))
                    )
            );
            cursorBilancio.moveToNext();
        }

        while (!cursorAppalti.isAfterLast()) {
            appalti.add(
                    new Appalto(cursorAppalti.getString(0),
                            cursorAppalti.getString(1),
                            cursorAppalti.getString(2),
                            cursorAppalti.getString(3),
                            cursorAppalti.getString(4),
                            Double.parseDouble(cursorAppalti.getString(5)),
                            cursorAppalti.getString(6)
                    )
            );
            cursorAppalti.moveToNext();
        }

        return new CrossData(appalti, bilancio);
    }

    public class CrossData {
        private ArrayList<Appalto> appalti;
        private ArrayList<Bilancio> vociBilancio;

        public CrossData(ArrayList<Appalto> appalti, ArrayList<Bilancio> vociBilancio) {
            this.appalti = appalti;
            this.vociBilancio = vociBilancio;
        }

        public ArrayList<Appalto> getAppalti() {
            return appalti;
        }

        public ArrayList<Bilancio> getVociBilancio() {
            return vociBilancio;
        }
    }

    public int getPostiLetto(String codiceEnte) {
        SQLiteDatabase db = getReadableDatabase();
        int postiLetto = 0;

        String query = "SELECT posti_letto from ULSS where codice_ente = ?";
        Cursor cur = db.rawQuery(query, new String[]{codiceEnte});
        cur.moveToFirst();

        if (!cur.isAfterLast()) {
            postiLetto = Integer.parseInt(cur.getString(0));
        }

        cur.close();

        return postiLetto;
    }

}
