package it.unive.dais.cevid.datadroid.template.DatiDiBilancio.FragmentStuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.BilancioHelper;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.Bilancio;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 19/01/2018.
 */

public class TabFragment extends Fragment {
    private RecyclerViewAdapter adapter;

    private String anno;
    private String codiceEnte;
    private BilancioHelper helper;
    //private List<Bilancio> vociBilancio;

    public void onCreate(Bundle fragmentBundle) {
        super.onCreate(fragmentBundle);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            anno = bundle.getString("anno");
            codiceEnte = bundle.getString("codice_ente");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dati_di_bilancio, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bilancio);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        helper = new BilancioHelper();

        /*vociBilancio = new ArrayList<>();
        vociBilancio.addAll(helper.getVociBilancio(codiceEnte, anno));*/

        adapter = new it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter(
                this.getContext(),
                helper.getVociBilancio(codiceEnte, anno)
        );
        recyclerView.setAdapter(adapter);



        recyclerView.setAdapter(adapter);
        return view;
    }

    public void onQueryTextChange (String query) {
        List<Bilancio> vociBilancioFiltered = new ArrayList<>();

        vociBilancioFiltered.add(new Bilancio("a", "b", 1, "c", 1.0));
        /*for (Bilancio voceBilancio : helper.getVociBilancio(codiceEnte, anno)) {
            String descrizione = voceBilancio.getDescrizioneCodice().toLowerCase();
            if (descrizione.contains(query))
                vociBilancioFiltered.add(voceBilancio);
        }*/

        adapter.setFilter(vociBilancioFiltered);
    }

}
