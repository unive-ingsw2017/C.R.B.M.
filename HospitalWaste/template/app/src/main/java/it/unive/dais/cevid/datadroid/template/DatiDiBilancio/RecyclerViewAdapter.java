package it.unive.dais.cevid.datadroid.template.DatiDiBilancio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 11/01/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Bilancio> bilanci = Collections.emptyList();
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, List<Bilancio> data) {
        this.mInflater = LayoutInflater.from(context);
        this.bilanci = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bilancio bilancio = bilanci.get(position);

        holder.importo.setText(bilancio.getImporto() + "");
        holder.voceDiSpesa.setText(bilancio.getDescrizioneCodice());
        holder.spesaProCapite.setText("TODO"); //TODO mettere spesa procapite
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return bilanci.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView voceDiSpesa;
        public TextView importo;
        public TextView spesaProCapite;

        public ViewHolder(View itemView) {
            super(itemView);
            voceDiSpesa = (TextView) itemView.findViewById(R.id.voce_di_spesa);
            importo = (TextView) itemView.findViewById(R.id.importo);
            spesaProCapite = (TextView) itemView.findViewById(R.id.spesa_pro_capite);
        }

        @Override
        public void onClick(View view) {

        }
    }

    // convenience method for getting data at click position
    public Bilancio getItem(int id) {
        return bilanci.get(id);
    }

}

