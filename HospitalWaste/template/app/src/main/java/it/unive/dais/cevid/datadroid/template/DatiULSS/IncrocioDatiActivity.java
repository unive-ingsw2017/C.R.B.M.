package it.unive.dais.cevid.datadroid.template.DatiULSS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.DBHelper;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 11/01/2018.
 */

public class IncrocioDatiActivity extends Activity implements AppCompatCallback {

    private RecyclerViewAdapter adapterVociBilancio;
    /**
     * Questo metodo viene chiamato quando questa activity parte.
     * l'intent deve essere inviato con anche l'oggetto ULSS in modo da sapere cosa
     * visualizzare
     * @param savedInstanceState stato dell'activity salvato precedentemente (opzionale).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incrocio_dati);

        Intent intent = getIntent();
        String codiceEnte = intent.getStringExtra("codice_ente");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.bilancio);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterVociBilancio = new RecyclerViewAdapter(
                this,
                DBHelper.getSingleton().getVociBilancio(codiceEnte)
        );
        recyclerView.setAdapter(adapterVociBilancio);

        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().setTitle(intent.getStringExtra("ULSS name"));
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