package it.unive.dais.crbm.DatiULSS.IncrocioDati.FragmentStuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import it.unive.dais.crbm.DatabaseUtils.DBHelper;


public class IncrocioAdapter extends FragmentPagerAdapter {
    private final DBHelper.CrossData datiIncrociati;
    private int postiLetto;

    private Bundle bundleAppalti = new Bundle();
    private Bundle bundleBilanci = new Bundle();


    public IncrocioAdapter(FragmentManager fm, DBHelper.CrossData datiIncrociati, int postiLetto) {
        super(fm);
        this.datiIncrociati = datiIncrociati;
        this.postiLetto = postiLetto;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return getBilanciFragment();
            case 1:
                return getAppaltiFragment();
            default:
                return null;
        }
    }

    private Fragment getBilanciFragment() {
        bundleBilanci.putParcelableArrayList("voci_bilancio", datiIncrociati.getVociBilancio());
        bundleBilanci.putInt("posti_letto", postiLetto);

        IncrocioBilancioFragment bilanciFragment = new IncrocioBilancioFragment();
        bilanciFragment.setArguments(bundleBilanci);
        return bilanciFragment;
    }
    private Fragment getAppaltiFragment() {
        bundleAppalti.putParcelableArrayList("appalti", datiIncrociati.getAppalti());

        IncrocioAppaltiFragment appaltiFragment = new IncrocioAppaltiFragment();
        appaltiFragment.setArguments(bundleAppalti);
        return appaltiFragment;
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
