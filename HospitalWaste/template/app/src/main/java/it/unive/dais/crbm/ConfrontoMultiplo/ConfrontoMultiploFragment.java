package it.unive.dais.crbm.ConfrontoMultiplo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.unive.dais.crbm.DatabaseUtils.DBHelper;
import it.unive.dais.crbm.R;

/**
 * Created by Aure on 26/01/2018.
 */

public class ConfrontoMultiploFragment extends android.support.v4.app.Fragment {
    private RecycleViewConfrontoMultiplo adapter;

    private String [] vociBilancio;
    private double [] importi;
    private String nomeUlss;
    private String codiceUlss;
    //private List<Bilancio> vociBilancio;

    public void onCreate(Bundle fragmentBundle) {
        super.onCreate(fragmentBundle);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            vociBilancio = bundle.getStringArray("vociBilancio");
            nomeUlss = bundle.getString("nomeUlss");
            importi = bundle.getDoubleArray("importi");
            codiceUlss = bundle.getString("codiceUlss");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.activity_confronta_multiplo, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.confronta_multiplo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        /*EditText et = (EditText) view.findViewById(R.id.confronta_multiplo_text);
        et.setText(nomeUlss);*/
        TabLayout tl = (TabLayout) view.findViewById(R.id.confronta_tab_layout);
        tl.addTab(tl.newTab().setText(nomeUlss),0);

        List<String> lVociBilancio = new ArrayList<>();
        List<Double> lImporti = new ArrayList<>();
        for (int i = 0; i < vociBilancio.length ; i++) {
            lVociBilancio.add(vociBilancio[i]);
            lImporti.add(importi[i]);
        }

        adapter = new RecycleViewConfrontoMultiplo(this.getContext(), lVociBilancio,lImporti, nomeUlss, DBHelper.getSingleton().getPostiLetto(codiceUlss));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            vociBilancio = savedInstanceState.getStringArray("vociBilancio");
            nomeUlss = savedInstanceState.getString("nomeUlss");
            importi = savedInstanceState.getDoubleArray("importi");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("vociBilancio", vociBilancio);
        outState.putString("nomeUlss", nomeUlss);
        outState.putDoubleArray("importi", importi);

    }
}

