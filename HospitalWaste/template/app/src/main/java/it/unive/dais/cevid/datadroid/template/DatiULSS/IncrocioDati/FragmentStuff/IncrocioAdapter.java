package it.unive.dais.cevid.datadroid.template.DatiULSS.IncrocioDati.FragmentStuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.DBHelper;

/**
 * Created by gianmarcocallegher on 27/01/18.
 */

public class IncrocioAdapter extends FragmentPagerAdapter {
    private final DBHelper.CrossData datiIncrociati;
    private Bundle bundle;

    public IncrocioAdapter(FragmentManager fm, DBHelper.CrossData datiIncrociati) {
        super(fm);
        this.datiIncrociati = datiIncrociati;
    }

    @Override
    public Fragment getItem(int position) {
        bundle = new Bundle();

        switch (position) {
            case 0:
                bundle.putParcelableArrayList("voci_bilancio", datiIncrociati.getVociBilancio());

                Fragment bilancioFragment = new IncrocioBilancioFragment();
                bilancioFragment.setArguments(bundle);
                return bilancioFragment;
            case 1:
                bundle.putParcelableArrayList("appalti", datiIncrociati.getAppalti());

                Fragment appaltoFragment = new IncrocioAppaltiFragment();
                appaltoFragment.setArguments(bundle);
                return appaltoFragment;
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
                return "Bilancio";
            case 1:
                return "Appalti";
            default:
                return null;
        }
    }
}
