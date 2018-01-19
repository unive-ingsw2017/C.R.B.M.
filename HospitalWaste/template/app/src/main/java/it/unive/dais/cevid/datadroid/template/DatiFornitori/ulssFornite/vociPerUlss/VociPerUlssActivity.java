package it.unive.dais.cevid.datadroid.template.DatiFornitori.ulssFornite.vociPerUlss;


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
import android.view.View;
import android.widget.TextView;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.AppaltiHelper;
import it.unive.dais.cevid.datadroid.template.DatabaseUtils.DBHelper;
import it.unive.dais.cevid.datadroid.template.DatiAppalti.RecyclerViewAdapter;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 19/01/2018.
 */

public class VociPerUlssActivity extends Activity implements AppCompatCallback, View.OnClickListener {
    private RecyclerViewAdapter adapter;


    /**
     * Questo metodo viene chiamato quando questa activity parte.
     * l'intent deve essere inviato con anche l'oggetto ULSS in modo da sapere cosa
     * visualizzare
     *
     * @param savedInstanceState stato dell'activity salvato precedentemente (opzionale).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dati_appalti);

        /*
        LayoutInflater inflater = (LayoutInflater)getSystemService(this.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.activity_simple_recycleview, null);
        */

        //get intent data
        Intent intent = getIntent();
        String nomeUlss = intent.getStringExtra("ulss");
        String codiceEnte = DBHelper.getSingleton().getCodiceEnte(nomeUlss);
        String fornitore = intent.getStringExtra("fornitore");


        //recycler view stuff
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.appalto);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppaltiHelper helper = new AppaltiHelper();
        adapter = new RecyclerViewAdapter(//RecyclerViewAdapter of DatiAppalti
                this,
                helper.getAppaltiByFornitore(codiceEnte, fornitore)
        );
        recyclerView.setAdapter(adapter);

        ((TextView)findViewById(R.id.appalto_text)).setText(fornitore);
        // set title on appBar
        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().setTitle(nomeUlss);

    }


    public void onClick(View view) {

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
     *
     * @param mode action mode.
     * @see AppCompatCallback#onSupportActionModeStarted(ActionMode)
     */
    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    /**
     * Lasciata vuota.
     *
     * @param mode action mode.
     * @see AppCompatCallback#onSupportActionModeFinished(ActionMode)
     */
    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    /**
     * Lasciata vuota.
     *
     * @param callback callback passata dal sistema.
     * @see AppCompatCallback#onWindowStartingSupportActionMode(ActionMode.Callback)
     */
    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}
