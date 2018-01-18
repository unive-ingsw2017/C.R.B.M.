package it.unive.dais.cevid.datadroid.template.DatiFornitori.ulssFornite;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import it.unive.dais.cevid.datadroid.template.DatabaseUtils.DBHelper;

/**
 * Created by francescobenvenuto on 18/01/2018.
 */

public class RecyclerViewAdapter extends it.unive.dais.cevid.datadroid.template.DatiFornitori.RecyclerViewAdapter {
    public RecyclerViewAdapter(Context context, List<String> data) {
        super(context, data);
    }

    protected void manageOnClick(View view){
        TextView text = (TextView) view;

        String ulssName = DBHelper.getSingleton().getCodiceEnte((String) text.getText());
        String fornitore = ((UlssFornite) getContext()).getIntent().getStringExtra("fornitore");
        //TODO creare l'intent per l'activity che mostra cosa il fornitore a fornito a quella determinata ulss
    }
}
