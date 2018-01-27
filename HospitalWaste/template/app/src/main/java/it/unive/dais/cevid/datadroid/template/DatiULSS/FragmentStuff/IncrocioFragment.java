package it.unive.dais.cevid.datadroid.template.DatiULSS.FragmentStuff;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.unive.dais.cevid.datadroid.template.DatabaseUtils.BilancioHelper;
import it.unive.dais.cevid.datadroid.template.DatabaseUtils.DBHelper;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by gianmarcocallegher on 27/01/18.
 */

public class IncrocioFragment extends Fragment {
    private String codiceEnte, criterio;

    public void onCreate(Bundle fragmentBundle) {
        super.onCreate(fragmentBundle);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.codiceEnte = fragmentBundle.getString("codice_ente");
            this.criterio = fragmentBundle.getString("criterio");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get the recycler layout(there's two in the same layout)
        RecyclerView recyclerAppalto = (RecyclerView) findViewById(R.id.appalto);
        recyclerAppalto.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView recyclerVociBilancio = (RecyclerView) findViewById(R.id.voce_bilancio);
        recyclerVociBilancio.setLayoutManager(new LinearLayoutManager(this));

        //get the crossed data
        DBHelper helper = DBHelper.getSingleton();
        DBHelper.CrossData datiIncrociati = helper.getDatiIncrociati(codiceEnte, criterio);

        //adapter set stuff
        RecyclerViewAdapter adapterVociBilancio = new it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter(
                this.getContext(),
                datiIncrociati.getVociBilancio()
        );
        recyclerVociBilancio.setAdapter(adapterVociBilancio);

        RecyclerViewAdapter adapterAppalti = new it.unive.dais.cevid.datadroid.template.DatiAppalti.RecyclerViewAdapter(
                this.getContext(),
                datiIncrociati.getAppalti()
        );
        recyclerAppalto.setAdapter(adapterAppalti);

        return null;
    }
}
