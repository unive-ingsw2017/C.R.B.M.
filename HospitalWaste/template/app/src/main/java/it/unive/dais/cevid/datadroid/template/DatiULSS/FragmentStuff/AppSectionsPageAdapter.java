package it.unive.dais.cevid.datadroid.template.DatiULSS.FragmentStuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by gianmarcocallegher on 27/01/18.
 */

public class AppSectionsPageAdapter extends FragmentPagerAdapter {
    private String codiceEnte, criterio;
    public AppSectionsPageAdapter(FragmentManager fm, String codiceEnte, String criterio) {
        super(fm);
        this.codiceEnte = codiceEnte;
        this.criterio = criterio;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("codice_ente", codiceEnte);
        bundle.putString("criterio", criterio);
        switch (position) {
            case 0:
                Fragment f0 = new IncrocioBilancioFragment();
                f0.setArguments(bundle);
                return f0;
            case 1:
                Fragment f1 = new IncrocioAppaltiFragment();
                f1.setArguments(bundle);
                return f1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Voci di Bilancio";
            case 1:
                return "Appalti";
            default:
                return null;
        }
    }
}
