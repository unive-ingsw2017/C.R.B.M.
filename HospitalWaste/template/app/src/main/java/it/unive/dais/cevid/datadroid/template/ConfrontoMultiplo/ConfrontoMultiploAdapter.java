package it.unive.dais.cevid.datadroid.template.ConfrontoMultiplo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatDelegate;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.BilancioHelper;
import it.unive.dais.cevid.datadroid.template.DatiDiBilancio.FragmentStuff.TabFragment;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by Aure on 26/01/2018.
 */

public class ConfrontoMultiploAdapter extends FragmentStatePagerAdapter {
    List<BilancioHelper.DatiConfrontoContainer> data;
    Map<String, String> ullsNameCodiceEnteMap;

    int n_pages;
    List<Fragment> registeredFragments = new ArrayList<>();

    public ConfrontoMultiploAdapter(FragmentManager fm, List<BilancioHelper.DatiConfrontoContainer> data,
                                    Map<String, String> ullsNameCodiceEnteMap) {
        super(fm);
        this.data = data;
        this.ullsNameCodiceEnteMap=ullsNameCodiceEnteMap;
        n_pages = data.size();

    }

    @Override
    public Fragment getItem(int position) {
        ConfrontoMultiploFragment tab = new ConfrontoMultiploFragment();
        Bundle tabBundle = new Bundle();
        String [] bilanci = new String[data.size()];
        double [] importi = new double[data.size()];
        if ( position >=0 && position < n_pages){

            int i=0;
            String nomeUlss=null;
            for(String s:ullsNameCodiceEnteMap.keySet()){
                if( i==position)
                    nomeUlss= s;
                i++;
            }

            tabBundle.putString("nomeUlss", nomeUlss);

            i=0;
            for(BilancioHelper.DatiConfrontoContainer dcc: data){
                bilanci[i]=dcc.getVoceBilancio();
                importi[i]=dcc.getImporto(ullsNameCodiceEnteMap.get(nomeUlss));
                i++;
            }

            tabBundle.putStringArray("bilanci", bilanci);
            tabBundle.putDoubleArray("importi", importi);
            tab.setArguments(tabBundle);
            return tab;
        }
        else
            return null;

    }

    @Override
    public int getCount() {
        return n_pages;
    }

    public void onQueryTextChange(String query) {
        for (Fragment f : registeredFragments) {
            ((TabFragment) f).onQueryTextChange(query);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.add(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
