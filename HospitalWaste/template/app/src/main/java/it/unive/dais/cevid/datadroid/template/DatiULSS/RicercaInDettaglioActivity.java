package it.unive.dais.cevid.datadroid.template.DatiULSS;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import it.unive.dais.cevid.datadroid.template.DatiAppalti.DatiAppaltiActivity;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.DatiDiBilancioActivity;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by Aure on 10/01/2018.
 */

public class RicercaInDettaglioActivity extends Activity implements AppCompatCallback, AdapterView.OnItemClickListener {

    private String codiceEnte;
    private String ulssName;

    private String[] iconNameArray;
    private TypedArray iconTypedArray;

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

        iconNameArray = getResources().getStringArray(R.array.iconNameArray);
        iconTypedArray = getResources().obtainTypedArray(R.array.iconArray);


        Intent intent = getIntent();
        codiceEnte = intent.getStringExtra("codiceEnte");
        ulssName = intent.getStringExtra("ULSS name");

        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar().setTitle(ulssName);

        GridView gridView = (GridView) findViewById(R.id.dashboard_grid);
        gridView.setAdapter(new ImageAdapter(this));
        gridView.setOnItemClickListener(this);
    }

    //stuff to manage the on button click
    private void manageMostraBilanci(){
        Intent ricDatiBilancio = new Intent(
                RicercaInDettaglioActivity.this,
                DatiDiBilancioActivity.class
        );

        ricDatiBilancio.putExtra("codice_ente", codiceEnte);
        ricDatiBilancio.putExtra("ULSS name", ulssName);
        startActivity(ricDatiBilancio);
    }
    private void manageMostraAppalti(){
        Intent ricAppalto = new Intent(
                RicercaInDettaglioActivity.this,
                DatiAppaltiActivity.class
        );

        ricAppalto.putExtra("codice_ente", codiceEnte);
        ricAppalto.putExtra("ULSS name", ulssName);
        startActivity(ricAppalto);
    }
    private void manageMostraOspedali(){
        Intent ricOspedali = new Intent(
                RicercaInDettaglioActivity.this,
                OspedaliAssociatiActivity.class
        );

        ricOspedali.putExtra("codice_ente", codiceEnte);
        ricOspedali.putExtra("ULSS name", ulssName);
        startActivity(ricOspedali);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String nameClicked = iconNameArray[i];

        if(nameClicked.equals(getString(R.string.osp_associati))){
            manageMostraOspedali();
        }
        else if(nameClicked.equals(getString(R.string.dati_appalti))){
            manageMostraAppalti();
        }
        else if(nameClicked.equals(getString(R.string.dati_bilancio))){
            manageMostraBilanci();
        }
        else{ // last case require icrocio functionality
            manageIcrocioButton();
        }
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

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            return iconNameArray.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }




        private class ViewHolder {
            public ImageView icon;
            public TextView text;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        // Create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                v = vi.inflate(R.layout.dashboard_icon, null);
                holder = new ViewHolder();
                holder.text = (TextView) v.findViewById(R.id.dashboard_icon_text);
                holder.icon = (ImageView) v.findViewById(R.id.dashboard_icon_img);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.icon.setImageResource(iconTypedArray.getResourceId(position, -1));
            holder.text.setText(iconNameArray[position]);

            return v;
        }
    }
}
