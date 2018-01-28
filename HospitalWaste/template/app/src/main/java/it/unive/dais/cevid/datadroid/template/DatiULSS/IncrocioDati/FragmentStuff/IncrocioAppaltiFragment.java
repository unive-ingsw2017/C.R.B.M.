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

import it.unive.dais.cevid.datadroid.template.DatiAppalti.Appalto;
import it.unive.dais.cevid.datadroid.template.DatiAppalti.RecyclerViewAdapter;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by gianmarcocallegher on 27/01/18.
 */

public class IncrocioAppaltiFragment extends Fragment {
    private List<Appalto> appaltiList = Collections.EMPTY_LIST;

    public void onCreate(Bundle fragmentBundle) {
        super.onCreate(fragmentBundle);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            appaltiList = bundle.getParcelableArrayList("appalti");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.incrocio_appalti, container, false);

        TextView importoView = (TextView) view.findViewById(R.id.somma_appalti);

        RecyclerView recyclerAppalto = (RecyclerView) view.findViewById(R.id.appalto);
        recyclerAppalto.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        RecyclerViewAdapter adapterAppalti = new RecyclerViewAdapter(
                getContext(),
                appaltiList
        );
        recyclerAppalto.setAdapter(adapterAppalti);

        if(appaltiList.isEmpty()){
            TextView prefixView = (TextView) view.findViewById(R.id.somma_appalti);
            prefixView.setText("Non vi sono Appalti");
        }
        else{
            importoView.setText(getAppaltiImporto().toString() + "€");
        }

        return view;
    }

    private Double getAppaltiImporto() {
        double importoTotale = 0;
        for (Appalto appalto : appaltiList) {
            importoTotale += appalto.getImporto();
        }

        return new BigDecimal(importoTotale).setScale(2 , BigDecimal.ROUND_UP).doubleValue();
    }
}
