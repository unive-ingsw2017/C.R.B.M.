package it.unive.dais.cevid.datadroid.template.ConfrontoMultiplo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.BilancioHelper;

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
        n_pages = ullsNameCodiceEnteMap.size();

    }

    @Override
    public Fragment getItem(int position) {
        ConfrontoMultiploFragment tab = new ConfrontoMultiploFragment();
        Bundle tabBundle = new Bundle();
        String [] vociBilancio = new String[data.size()];
        double [] importi = new double[data.size()];
        if ( position >=0 && position < n_pages){

            int i=0;
            String codiceUlss=null;
            for(String s:ullsNameCodiceEnteMap.keySet()){
                if( i==position)
                    codiceUlss= s;
                i++;
            }
            tabBundle.putString("nomeUlss", ullsNameCodiceEnteMap.get(codiceUlss));

            i=0;
            for(BilancioHelper.DatiConfrontoContainer dcc: data){
                vociBilancio[i]=dcc.getVoceBilancio();
                importi[i]=dcc.getImporto(codiceUlss);
                i++;
            }

            tabBundle.putStringArray("vociBilancio", vociBilancio);
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

    /*public void onQueryTextChange(String query) {
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
    }*/
}
