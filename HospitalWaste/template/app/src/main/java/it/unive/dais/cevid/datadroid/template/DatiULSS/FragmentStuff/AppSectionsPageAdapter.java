package it.unive.dais.cevid.datadroid.template.DatiULSS.FragmentStuff;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by gianmarcocallegher on 27/01/18.
 */

public class AppSectionsPageAdapter extends FragmentPagerAdapter {
    public AppSectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment f0 = new Fragment();
                return f0;
            case 1:
                Fragment f1 = new Fragment();
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
