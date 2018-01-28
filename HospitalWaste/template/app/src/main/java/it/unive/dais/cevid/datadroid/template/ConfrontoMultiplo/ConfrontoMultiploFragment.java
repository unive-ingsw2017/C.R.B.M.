package it.unive.dais.cevid.datadroid.template.ConfrontoMultiplo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.BilancioHelper;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.RecyclerViewAdapter;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by Aure on 26/01/2018.
 */

public class ConfrontoMultiploFragment extends android.support.v4.app.Fragment {
    private RecycleViewConfrontoMultiplo adapter;

    private String [] bilanci;
    private double [] importi;
    private String nomeUlss;
    //private List<Bilancio> vociBilancio;

    public void onCreate(Bundle fragmentBundle) {
        super.onCreate(fragmentBundle);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bilanci = bundle.getStringArray("bilanci");
            nomeUlss = bundle.getString("nomeUlss");
            importi = bundle.getDoubleArray("importi");
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

        List<String> lBilanci = new ArrayList<>();
        List<Double> lImporti = new ArrayList<>();
        for (int i = 0; i < bilanci.length ; i++) {
            lBilanci.add(bilanci[i]);
            lImporti.add(importi[i]);
        }


        adapter = new RecycleViewConfrontoMultiplo(this.getContext(), lBilanci,lImporti, nomeUlss);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            bilanci = savedInstanceState.getStringArray("bilanci");
            nomeUlss = savedInstanceState.getString("nomeUlss");
            importi = savedInstanceState.getDoubleArray("importi");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("bilanci", bilanci);
        outState.putString("nomeUlss", nomeUlss);
        outState.putDoubleArray("importi", importi);

    }
}

