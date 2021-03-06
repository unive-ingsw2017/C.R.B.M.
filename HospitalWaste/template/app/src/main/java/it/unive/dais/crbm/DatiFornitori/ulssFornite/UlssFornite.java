package it.unive.dais.crbm.DatiFornitori.ulssFornite;

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

import java.util.List;

import it.unive.dais.crbm.DatabaseUtils.FornitoriHelper;
import it.unive.dais.crbm.R;

/**
 * Created by francescobenvenuto on 18/01/2018.
 */

public class UlssFornite extends Activity implements AppCompatCallback, View.OnClickListener {
    private RecyclerViewAdapter adapter;


    /**
     * Questo metodo viene chiamato quando questa activity parte.
     * l'intent deve essere inviato con anche l'oggetto ULSS in modo da sapere cosa
     * visualizzare
     * @param savedInstanceState stato dell'activity salvato precedentemente (opzionale).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycleview);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intet = getIntent();
        String fornitore = intet.getStringExtra("fornitore");
        FornitoriHelper helper = new FornitoriHelper();

        List<String> ulssNames = helper.getUlssFornite(fornitore);

        adapter = new RecyclerViewAdapter(
                this,
                ulssNames
        );
        recyclerView.setAdapter(adapter);

        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().setTitle(fornitore);

    }


    public void onClick(View view){

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
