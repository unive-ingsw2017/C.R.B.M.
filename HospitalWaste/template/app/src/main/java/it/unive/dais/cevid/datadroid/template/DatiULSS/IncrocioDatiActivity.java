package it.unive.dais.cevid.datadroid.template.DatiULSS;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.DBHelper;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 11/01/2018.
 */

public class IncrocioDatiActivity extends FragmentActivity implements AppCompatCallback, ActionBar.TabListener {
    private it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter adapterVociBilancio;
    private it.unive.dais.cevid.datadroid.template.DatiAppalti.RecyclerViewAdapter adapterAppalti;

    private String criterio;
    private String codiceEnte;
    private String ulssName;
    //TODO fragment per bilanci e appalti e poi per anno
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

        codiceEnte = intent.getStringExtra("codiceEnte");
        ulssName = intent.getStringExtra("ulssName");
        criterio = intent.getStringExtra("criterio");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_one)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_two)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //get the recycler layout(there's two in the same layout)
        RecyclerView recyclerAppalto = (RecyclerView) findViewById(R.id.appalto_incrocio);
        recyclerAppalto.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView recyclerBilanci = (RecyclerView) findViewById(R.id.bilancio_incrocio);
        recyclerBilanci.setLayoutManager(new LinearLayoutManager(this));

        //get the crossed data
        DBHelper helper = DBHelper.getSingleton();
        DBHelper.CrossData datiIncrociati = helper.getDatiIncrociati(codiceEnte, criterio);

        //adapter set stuff
        adapterVociBilancio = new it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter(
                this,
                datiIncrociati.getVociBilancio()
        );
        recyclerBilanci.setAdapter(adapterVociBilancio);

        adapterAppalti = new it.unive.dais.cevid.datadroid.template.DatiAppalti.RecyclerViewAdapter(
                this,
                datiIncrociati.getAppalti()
        );
        recyclerAppalto.setAdapter(adapterAppalti);

        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().setTitle(ulssName);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
