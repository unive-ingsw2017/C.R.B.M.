package it.unive.dais.cevid.datadroid.template.DatiDiBilancio.FragmentStuff;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 19/01/2018.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private final String tabOne;
    private final String tabTwo;
    private final String tabThre;

    int mNumOfTabs;
    Bundle fragmentBundle;

    public FragmentAdapter(FragmentManager fm, int NumOfTabs, Bundle data, Context context) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        fragmentBundle = data;

        tabOne = context.getString(R.string.tab_one);
        tabTwo = context.getString(R.string.tab_two);
        tabThre = context.getString(R.string.tab_three);
    }

    @Override
    public Fragment getItem(int position) {
        TabFragment tab = new TabFragment();
        switch (position) {
            case 0:
                fragmentBundle.putString("anno", tabOne);

                tab.setArguments(fragmentBundle);
                return tab;
            case 1:
                fragmentBundle.putString("anno", tabTwo);

                tab.setArguments(fragmentBundle);
                return tab;
            case 2:
                fragmentBundle.putString("anno", tabThre);

                tab.setArguments(fragmentBundle);
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
        for (int i = 0; i < mNumOfTabs; i++) {
            TabFragment tab = (TabFragment) getItem(i);
            tab.onQueryTextChange(query);
        }
    }
}
