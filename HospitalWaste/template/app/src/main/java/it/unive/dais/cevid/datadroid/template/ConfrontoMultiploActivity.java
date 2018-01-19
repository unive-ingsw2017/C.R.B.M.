package it.unive.dais.cevid.datadroid.template;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import it.unive.dais.cevid.datadroid.template.DatabaseUtils.BilanciHelper;

/**
 * Created by francescobenvenuto on 15/01/2018.
 */

public class ConfrontoMultiploActivity extends Activity implements AppCompatCallback {
    Map<String, String> ullsNameCodiceEnteMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO fare il confronto veramente questo è solo per mostrare cosa è stato selezionato
        super.onCreate(savedInstanceState);

        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);


        Intent intent = getIntent();
        List<String> dati = new LinkedList();
        ullsNameCodiceEnteMap = (Map<String, String>) intent.getSerializableExtra("map");

        BilanciHelper helper = new BilanciHelper();
        List<BilanciHelper.DatiConfrontoContainer> confrontoData = helper.
                getConfrontoMultiploDati(ullsNameCodiceEnteMap.keySet(), 2016);


        TableView<String[]> table = new TableView(getApplicationContext());
        TableDataAdapter<String[]> myDataAdapter =
                new SimpleTableDataAdapter(this.getApplicationContext(), genTableData(confrontoData));

        List<String> header = new LinkedList();
        header.add("voce di bilancio");
        header.addAll(ullsNameCodiceEnteMap.values());

        //TODO check if the creation is right
        TableHeaderAdapter myHeaderAdapter =
                new SimpleTableHeaderAdapter(this.getApplicationContext(),
                        Arrays.copyOf(header.toArray(), header.size(), String[].class)
                );

        table.setDataAdapter(myDataAdapter);
        table.setHeaderAdapter(myHeaderAdapter);
        setContentView(table);
        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().setTitle("Confronto Multiplo");
    }

    //from the DatiConfrontoContainer generate a list containing all the table data in a list
    private String[][] genTableData(List<BilanciHelper.DatiConfrontoContainer> data){
        String[][] tableData = new String[data.size()][];
        int index = 0;

        for(BilanciHelper.DatiConfrontoContainer dataContainer: data) {

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
