package it.unive.dais.cevid.datadroid.template.DatiFornitori;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.FornitoriHelper;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 18/01/2018.
 */

public class FornitoriActivity extends AppCompatActivity {
    private RecyclerViewAdapter adapter;
    private List<String> fornitori;
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

        FornitoriHelper helper = new FornitoriHelper();
        fornitori = helper.getFornitori();
        adapter = new RecyclerViewAdapter(
                this.getApplicationContext(),
                fornitori
        );
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setTitle(getString(R.string.fornitori_hardcoded));
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
        super.onSupportActionModeStarted(mode);
    }

    /**
     * Lasciata vuota.
     * @see AppCompatCallback#onSupportActionModeFinished(ActionMode)
     * @param mode action mode.
     */
    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        super.onSupportActionModeFinished(mode);
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
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem myMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) myMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String newText) {
        List<String> filteredFornitori = new ArrayList<>();
        for (String fornitore : fornitori) {
            if (fornitore.toLowerCase().contains(newText.toLowerCase()))
                filteredFornitori.add(fornitore);
        }
        adapter.filter(filteredFornitori);
    }
}
