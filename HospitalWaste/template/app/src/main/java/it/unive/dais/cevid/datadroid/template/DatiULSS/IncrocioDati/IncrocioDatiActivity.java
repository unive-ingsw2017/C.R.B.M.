package it.unive.dais.cevid.datadroid.template.DatiULSS.IncrocioDati;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.DBHelper;
import it.unive.dais.cevid.datadroid.template.DatiULSS.IncrocioDati.FragmentStuff.IncrocioAdapter;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 11/01/2018.
 */

public class IncrocioDatiActivity extends FragmentActivity implements AppCompatCallback {
    private android.support.v4.view.ViewPager mViewPager;
    private TabLayout tabLayout;

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
        setContentView(R.layout.activity_incrocio_dati);

        Intent intent = getIntent();

        String codiceEnte = intent.getStringExtra("codiceEnte");
        String ulssName = intent.getStringExtra("ulssName");
        String criterio = intent.getStringExtra("criterio");

        mViewPager = (ViewPager) findViewById(R.id.pager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        DBHelper helper = DBHelper.getSingleton();
        DBHelper.CrossData datiIncrocio = helper.getDatiIncrociati(codiceEnte, criterio);

        IncrocioAdapter incrocioAdapter = new IncrocioAdapter(
                getSupportFragmentManager(),
                datiIncrocio
        );
        mViewPager.setAdapter(incrocioAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));



        tabLayout.addTab(tabLayout.newTab().setText(incrocioAdapter.getPageTitle(0)));
        tabLayout.addTab(tabLayout.newTab().setText(incrocioAdapter.getPageTitle(1)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        mViewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );

        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().

                setTitle(ulssName);

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
