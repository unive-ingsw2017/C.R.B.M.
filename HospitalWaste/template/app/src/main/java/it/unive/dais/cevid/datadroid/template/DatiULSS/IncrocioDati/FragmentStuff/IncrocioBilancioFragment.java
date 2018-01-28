package it.unive.dais.cevid.datadroid.template.DatiULSS.IncrocioDati.FragmentStuff;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.Bilancio;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter;
import it.unive.dais.cevid.datadroid.template.R;


public class IncrocioBilancioFragment extends Fragment {
    private List<Bilancio> vociBilancioList = Collections.EMPTY_LIST;
    private int postiLetto;

    public void onCreate(Bundle fragmentBundle) {
        super.onCreate(fragmentBundle);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            vociBilancioList = bundle.getParcelableArrayList("voci_bilancio");
            postiLetto = bundle.getInt("posti_letto");

            bundle.remove("voci_bilancio");
            bundle.remove("posti_letto");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.incrocio_bilancio, container, false);

        TextView importoView = (TextView) view.findViewById(R.id.somma_voci_bilancio);

        RecyclerView recyclerAppalto = (RecyclerView) view.findViewById(R.id.bilancio);
        recyclerAppalto.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        RecyclerViewAdapter adapterBilancio = new RecyclerViewAdapter(
                getContext(),
                vociBilancioList,
                postiLetto
        );
        recyclerAppalto.setAdapter(adapterBilancio);

        if(vociBilancioList.isEmpty()){
            TextView prefixBilanci = (TextView) view.findViewById(R.id.prefix_somma_bilanci);
            prefixBilanci.setText("Non vi sono voci di Bilancio");
        }
        else{
            importoView.setText(getTotaleImportoBilanci().toString() + "€");
        }


        return view;
    }

    private Double getTotaleImportoBilanci() {
        double importoTotale = 0;
        for (Bilancio bilancio : vociBilancioList) {
            importoTotale += bilancio.getImporto();
        }

        return new BigDecimal(importoTotale).setScale(2 , BigDecimal.ROUND_UP).doubleValue();
    }
}
