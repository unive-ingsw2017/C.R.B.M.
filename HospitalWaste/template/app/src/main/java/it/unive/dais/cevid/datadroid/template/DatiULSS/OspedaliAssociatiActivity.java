package it.unive.dais.cevid.datadroid.template.DatiULSS;

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

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.DBHelper;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 12/01/2018.
 */

public class OspedaliAssociatiActivity extends Activity implements AppCompatCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ospedali_associati);
        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);

        //((EditText)findViewById(R.id.ospedali_text)).setKeyListener(null);

        Intent intent = getIntent();
        String codiceEnte = intent.getStringExtra("codice_ente");
        String ulssName = intent.getStringExtra("ULSS name");

        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().setTitle(ulssName);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.listview_row, DBHelper.getSingleton().getOspedali(codiceEnte));

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