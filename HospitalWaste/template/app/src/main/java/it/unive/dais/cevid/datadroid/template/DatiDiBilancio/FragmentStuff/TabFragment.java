package it.unive.dais.cevid.datadroid.template.DatiDiBilancio.FragmentStuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.BilanciHelper;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 19/01/2018.
 */

public class TabFragment extends Fragment {
    private RecyclerViewAdapter adapter;

    private String anno;
    private String codiceEnte;

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

        BilanciHelper helper = new BilanciHelper();
        adapter = new it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter(
                this.getContext(),
                helper.getVociBilancio(codiceEnte, anno)
        );
        recyclerView.setAdapter(adapter);



        recyclerView.setAdapter(adapter);
        return view;
    }

}
