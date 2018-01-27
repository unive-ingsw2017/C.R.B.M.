package it.unive.dais.cevid.datadroid.template.DatiULSS.FragmentStuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.unive.dais.cevid.datadroid.template.DatabaseUtils.DBHelper;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by gianmarcocallegher on 27/01/18.
 */

public class IncrocioAppaltiFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.activity_incrocio_dati, container, false);

        RecyclerView recyclerAppalto = (RecyclerView) view.findViewById(R.id.appalto);
        recyclerAppalto.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //get the crossed data
        DBHelper helper = DBHelper.getSingleton();
        DBHelper.CrossData datiIncrociati = helper.getDatiIncrociati(codiceEnte, criterio);

        /*RecyclerViewAdapter adapterAppalti = new it.unive.dais.cevid.datadroid.template.DatiAppalti.RecyclerViewAdapter(
                this,
                datiIncrociati.getAppalti()
        );*/
        //recyclerAppalto.setAdapter(adapterAppalti);

        return null;
    }
}
