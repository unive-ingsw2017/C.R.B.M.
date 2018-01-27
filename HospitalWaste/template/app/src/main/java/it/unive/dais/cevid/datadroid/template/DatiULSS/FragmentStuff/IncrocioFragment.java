package it.unive.dais.cevid.datadroid.template.DatiULSS.FragmentStuff;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.BilancioHelper;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by gianmarcocallegher on 27/01/18.
 */

public class IncrocioFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dati_di_bilancio, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bilancio);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        BilancioHelper helper = new BilancioHelper();

        /*vociBilancio = new ArrayList<>();
        vociBilancio.addAll(helper.getVociBilancio(codiceEnte, anno));*/

        /*adapter = new it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter(
                this.getContext(),
                helper.getVociBilancio(codiceEnte, anno)
        );
        recyclerView.setAdapter(adapter);*/

        return view;
    }
}
