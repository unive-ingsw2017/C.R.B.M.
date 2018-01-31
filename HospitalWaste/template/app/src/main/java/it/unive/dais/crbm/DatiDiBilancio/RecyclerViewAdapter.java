package it.unive.dais.crbm.DatiDiBilancio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import it.unive.dais.crbm.R;

/**
 * Created by francescobenvenuto on 11/01/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Bilancio> vociBilancio = Collections.emptyList();
    private LayoutInflater mInflater;
    private int postiLetto;

    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, List<Bilancio> data, int postiLetto) {
        this.mInflater = LayoutInflater.from(context);
        this.vociBilancio = data;
        this.postiLetto = postiLetto;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_row_bilancio, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bilancio bilancio = vociBilancio.get(position);

        holder.importo.setText(String.format("Importo voce di Bilancio: %.2f €", bilancio.getImporto()));
        holder.voceDiSpesa.setText(bilancio.getDescrizioneCodice());
        if (postiLetto != 0)
            holder.spesaProCapite.setText(String.format("Importo / Posti letto: %.2f €", bilancio.getImporto() / postiLetto));
        else
            holder.spesaProCapite.setText("N.D.");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return vociBilancio.size();
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
        return vociBilancio.get(id);
    }

    public void setFilter (List<Bilancio> vociBilancioFiltered) {
        vociBilancio = vociBilancioFiltered;
        notifyDataSetChanged();
    }
}

