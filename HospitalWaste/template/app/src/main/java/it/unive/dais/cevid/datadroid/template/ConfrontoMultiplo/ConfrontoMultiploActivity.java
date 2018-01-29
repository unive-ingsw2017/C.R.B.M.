package it.unive.dais.cevid.datadroid.template.ConfrontoMultiplo;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;

import android.view.MenuItem;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import it.unive.dais.cevid.datadroid.template.DatabaseUtils.BilancioHelper;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.FragmentStuff.FragmentAdapter;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.FragmentStuff.TabFragment;
import it.unive.dais.cevid.datadroid.template.R;

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
        /*viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

        /*TableView<String[]> table = new TableView(getApplicationContext());
        TableDataAdapter<String[]> myDataAdapter =
                new SimpleTableDataAdapter(this.getApplicationContext(), genTableData(confrontoData));


        List<String> header = new LinkedList();
        header.add("Voce di Bilancio");
        header.addAll(ullsNameCodiceEnteMap.values());

        //TODO check if the creation is right
        TableHeaderAdapter myHeaderAdapter =
                new SimpleTableHeaderAdapter(this.getApplicationContext(),
                        Arrays.copyOf(header.toArray(), header.size(), String[].class)
                );
        TableColumnDpWidthModel tc = new TableColumnDpWidthModel(getApplicationContext(), header.size(), 100);
        tc.setColumnWidth(0,180);
        table.setDataAdapter(myDataAdapter);
        table.setHeaderAdapter(myHeaderAdapter);
        table.setColumnModel(tc);
        setContentView(table);*/
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

