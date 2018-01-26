package it.unive.dais.cevid.datadroid.template.DatiDiBilancio.FragmentStuff;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 19/01/2018.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private final String tabOne;
    private final String tabTwo;
    private final String tabThree;
    private final String codiceEnte;

    int mNumOfTabs;

    List<Fragment> registeredFragments = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm, int NumOfTabs, String codiceEnte, Context context) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.codiceEnte = codiceEnte;

        tabOne = context.getString(R.string.tab_one);
        tabTwo = context.getString(R.string.tab_two);
        tabThree = context.getString(R.string.tab_three);
    }

    @Override
    public Fragment getItem(int position) {
        TabFragment tab = new TabFragment();
        Bundle tabBundle = new Bundle();
        tabBundle.putString("codice_ente", codiceEnte);

        switch (position) {
            case 0:
                tabBundle.putString("anno", tabOne);

                tab.setArguments(tabBundle);
                return tab;
            case 1:
                tabBundle.putString("anno", tabTwo);

                tab.setArguments(tabBundle);
                return tab;
            case 2:
                tabBundle.putString("anno", tabThree);

                tab.setArguments(tabBundle);
                return tab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
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
