package it.unive.dais.cevid.datadroid.template.DatiAppalti;

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
    private List<Appalto> appalti = Collections.emptyList();
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, List<Appalto> data) {
        this.mInflater = LayoutInflater.from(context);
        this.appalti = data;
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
        Appalto appalto = appalti.get(position);

        holder.importo.setText(appalto.getImporto() + "");
        holder.voceDiSpesa.setText(appalto.getOggetto());
        holder.spesaProCapite.setText("TODO"); //TODO mettere spesa procapite
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return appalti.size();
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
    public Appalto getItem(int id) {
        return appalti.get(id);
    }

}

