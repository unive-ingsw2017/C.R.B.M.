package it.unive.dais.cevid.datadroid.template.DatiULSS;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import it.unive.dais.cevid.datadroid.template.DatiAppalti.DatiAppaltiActivity;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.DatiDiBilancioActivity;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by Aure on 10/01/2018.
 */

public class RicercaInDettaglioActivity extends Activity implements AppCompatCallback {

    private String codiceEnte;
    private String ulssName;

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
        codiceEnte = intent.getStringExtra("codiceEnte");
        ulssName = intent.getStringExtra("ULSS name");

        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().setTitle(ulssName);


        /**
         * Prova per collegare il bottone del layout ad un'azione
         */
        Button bilanci = (Button) findViewById(R.id.mostra_bilanci);
        bilanci.setOnClickListener(v -> {
            Intent ricDatiBilancio = new Intent(
                    RicercaInDettaglioActivity.this,
                    DatiDiBilancioActivity.class
            );

            ricDatiBilancio.putExtra("codice_ente", codiceEnte);
            ricDatiBilancio.putExtra("ULSS name", ulssName);
            startActivity(ricDatiBilancio);
        });

        Button appalti = (Button) findViewById(R.id.mostra_appalti);
        appalti.setOnClickListener(v -> {
            Intent ricAppalto = new Intent(
                    RicercaInDettaglioActivity.this,
                    DatiAppaltiActivity.class
            );

            ricAppalto.putExtra("codice_ente", codiceEnte);
            ricAppalto.putExtra("ULSS name", ulssName);
            startActivity(ricAppalto);
        });

        Button incroci = (Button) findViewById(R.id.mostra_incrocio);
        incroci.setOnClickListener(v -> {
            manageIcrocioButton();
        });

        Button ospedali = (Button) findViewById(R.id.mostra_ospedali);
        ospedali.setOnClickListener(v -> {
            Intent ricOspedali = new Intent(
                    RicercaInDettaglioActivity.this,
                    OspedaliAssociatiActivity.class
            );

            ricOspedali.putExtra("codice_ente", codiceEnte);
            ricOspedali.putExtra("ULSS name", ulssName);
            startActivity(ricOspedali);
        });
    }

    private void manageIcrocioButton() {
        // pop up textbox
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Voce di incrocio");
        alert.setMessage("inserisci la parola su cui vuoi incrociare");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Incrocia", (dialog, whichButton) -> {
            Intent ricIncrocio = new Intent(
                    RicercaInDettaglioActivity.this,
                    IncrocioDatiActivity.class
            );

            ricIncrocio.putExtra("codiceEnte", codiceEnte);
            ricIncrocio.putExtra("ulssName", ulssName);
            ricIncrocio.putExtra("criterio", input.getText().toString());//the criteria
            startActivity(ricIncrocio);
        });

        alert.setNegativeButton("Annulla", (dialog, whichButton) -> {
            // Canceled.
        });

        alert.show();
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
