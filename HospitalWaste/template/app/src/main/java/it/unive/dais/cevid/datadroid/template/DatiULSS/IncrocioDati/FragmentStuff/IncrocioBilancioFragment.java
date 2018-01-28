package it.unive.dais.cevid.datadroid.template.DatiULSS.IncrocioDati.FragmentStuff;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by gianmarcocallegher on 27/01/18.
 */

public class IncrocioBilancioFragment extends Fragment {
    private List bilanciList = Collections.EMPTY_LIST;

    public void onCreate(Bundle fragmentBundle) {
        super.onCreate(fragmentBundle);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bilanciList = bundle.getParcelableArrayList("voci_bilancio");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.incrocio_bilancio, container, false);

        RecyclerView recyclerAppalto = (RecyclerView) view.findViewById(R.id.bilancio);
        recyclerAppalto.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        RecyclerViewAdapter adapterAppalti = new RecyclerViewAdapter(
                getContext(),
                bilanciList
        );
        recyclerAppalto.setAdapter(adapterAppalti);

        return view;
    }
}
