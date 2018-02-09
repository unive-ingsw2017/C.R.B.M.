package it.unive.dais.crbm.ConfrontoMultiplo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unive.dais.crbm.DatabaseUtils.BilancioHelper;
import it.unive.dais.crbm.R;

/**
 * Created by francescobenvenuto on 15/01/2018.
 */

public class ConfrontoMultiploActivity extends FragmentActivity implements AppCompatCallback {
    Map<String, String> ullsNameCodiceEnteMap;
    int n_pages;

    private String queryConfrontoSearch = null;
    private ConfrontoMultiploAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confronta_multiplo_fragment);

        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);


        Intent intent = getIntent();
        List<String> dati = new LinkedList<>();
        ullsNameCodiceEnteMap = (Map<String, String>) intent.getSerializableExtra("map");
        queryConfrontoSearch = intent.getStringExtra("query");

        BilancioHelper helper = new BilancioHelper();
        List<BilancioHelper.DatiConfrontoContainer> confrontoData = helper.
                    getConfrontoMultiploDati(ullsNameCodiceEnteMap.keySet(), 2016, queryConfrontoSearch);



        final ViewPager viewPager = (ViewPager) findViewById(R.id.confronta_pager);
        adapter = new ConfrontoMultiploAdapter(
                getSupportFragmentManager(),
                confrontoData,
                ullsNameCodiceEnteMap
                );
        viewPager.setAdapter(adapter);

        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().setTitle("Confronto Multiplo");



    }

    //from the DatiConfrontoContainer generate a list containing all the table data in a list
    private String[][] genTableData(List<BilancioHelper.DatiConfrontoContainer> data){
        String[][] tableData = new String[data.size()][];
        int index = 0;

        for(BilancioHelper.DatiConfrontoContainer dataContainer: data) {

            ArrayList<String> row = new ArrayList();
            row.add(dataContainer.getVoceBilancio());

            for (String codiceEnte : ullsNameCodiceEnteMap.keySet()) {
                row.add(dataContainer.getImporto(codiceEnte) +"");
            }

            tableData[index] = row.toArray(new String[row.size()]);
            index++;
        }
        return tableData;
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

