package it.unive.dais.cevid.datadroid.template.DatiFornitori.ulssFornite;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import it.unive.dais.cevid.datadroid.template.DatiFornitori.ulssFornite.vociPerUlss.VociPerUlssActivity;

/**
 * Created by francescobenvenuto on 18/01/2018.
 */

public class RecyclerViewAdapter extends it.unive.dais.cevid.datadroid.template.DatiFornitori.RecyclerViewAdapter {
    public RecyclerViewAdapter(Context context, List<String> data) {
        super(context, data);
    }

    protected void manageOnClick(View view){
        TextView text = (TextView) view;

        String ulssName = (String) text.getText();
        String fornitore = ((UlssFornite) getContext()).getIntent().getStringExtra("fornitore");

        Intent intent = new Intent(getContext(), VociPerUlssActivity.class);
        intent.putExtra("fornitore", fornitore);
        intent.putExtra("ulss", ulssName);

        getContext().startActivity(intent);
    }
}
