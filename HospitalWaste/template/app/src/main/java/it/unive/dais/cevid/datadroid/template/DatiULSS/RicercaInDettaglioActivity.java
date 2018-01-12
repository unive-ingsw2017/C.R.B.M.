package it.unive.dais.cevid.datadroid.template.DatiULSS;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;
import android.widget.Button;

import it.unive.dais.cevid.datadroid.template.DatiAppalti.DatiAppaltiActivity;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.DatiDiBilancioActivity;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by Aure on 10/01/2018.
 */

public class RicercaInDettaglioActivity extends Activity implements AppCompatCallback {

    /**
     * Questo metodo viene chiamato quando questa activity parte.
     *
     * @param savedInstanceState stato dell'activity salvato precedentemente (opzionale).
     */
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ricerca_in_dettaglio_layout);
        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);

        Intent intent = getIntent();
        String codice_ente = intent.getStringExtra("codice_ente");
        String ulssName = intent.getStringExtra("ULSS name");

        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().setTitle(ulssName);



        /**
         * Prova per collegare il bottone del layout ad un'azione
         */
        Button bilanci = (Button)findViewById(R.id.mostra_bilanci);
        bilanci.setOnClickListener(v -> {
            Intent ricDatiBilancio = new Intent(
                    RicercaInDettaglioActivity.this,
                    DatiDiBilancioActivity.class
            );

            ricDatiBilancio.putExtra("codice_ente", codice_ente);
            ricDatiBilancio.putExtra("ULSS name", ulssName);
            startActivity(ricDatiBilancio);
        });

        Button appalti = (Button)findViewById(R.id.mostra_appalti);
            appalti.setOnClickListener(v -> {
                Intent ricAppalto = new Intent(
                        RicercaInDettaglioActivity.this,
                        DatiAppaltiActivity.class
                );

                ricAppalto.putExtra("codice_ente", codice_ente);
                ricAppalto.putExtra("ULSS name", ulssName);
                startActivity(ricAppalto);
            });

        Button incroci = (Button)findViewById(R.id.mostra_incrocio);
            incroci.setOnClickListener(v -> {
                Intent ricAppalto = new Intent(
                    RicercaInDettaglioActivity.this,
                    IncrocioDatiActivity.class
                );

                ricAppalto.putExtra("codice_ente", codice_ente);
                ricAppalto.putExtra("ULSS name", ulssName);
                startActivity(ricAppalto);
            });

        Button ospedali = (Button)findViewById(R.id.mostra_ospedali);
        ospedali.setOnClickListener(v -> {
            Intent ricAppalto = new Intent(
                    RicercaInDettaglioActivity.this,
                    OspedaliAssociatiActivity.class
            );

            ricAppalto.putExtra("codice_ente", codice_ente);
            ricAppalto.putExtra("ULSS name", ulssName);
            startActivity(ricAppalto);
        });
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
