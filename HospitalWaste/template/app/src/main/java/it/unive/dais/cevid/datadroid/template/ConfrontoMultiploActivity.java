package it.unive.dais.cevid.datadroid.template;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by francescobenvenuto on 15/01/2018.
 */

public class ConfrontoMultiploActivity extends Activity implements AppCompatCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO fare il confronto veramente questo è solo per mostrare cosa è stato selezionato
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ospedali_associati); // è solo per visualizzare le robe premute
        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);

        //((EditText)findViewById(R.id.ospedali_text)).setKeyListener(null);

        Intent intent = getIntent();


        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().setTitle("Confronto Multiplo");

        List<String> dati = new LinkedList();
        Map<String, String> ullsNameCodiceEnteMap = (Map<String, String>) intent.getSerializableExtra("map");
        for(String key: ullsNameCodiceEnteMap.keySet()){
            dati.add(key + "\t" + ullsNameCodiceEnteMap.get(key));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                R.layout.listview_row, dati);

        ListView listView = (ListView) findViewById(R.id.ospedali_list);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Lasciata vuota.
     * @see AppCompatCallback#onSupportActionModeStarted(ActionMode)
     * @param mode action mode.
     */
    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    /**
     * Lasciata vuota.
     * @see AppCompatCallback#onSupportActionModeFinished(ActionMode)
     * @param mode action mode.
     */
    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    /**
     * Lasciata vuota.
     * @see AppCompatCallback#onWindowStartingSupportActionMode(ActionMode.Callback)
     * @param callback callback passata dal sistema.
     */
    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

}
