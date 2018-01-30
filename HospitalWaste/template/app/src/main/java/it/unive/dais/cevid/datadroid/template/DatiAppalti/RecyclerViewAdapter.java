package it.unive.dais.cevid.datadroid.template.DatiAppalti;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
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
        View view = mInflater.inflate(R.layout.recyclerview_row_appalti, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Appalto appalto = appalti.get(position);
        String s = appalto.getSceltaContraente();

        holder.importo.setText(String.format("Importo appalto: %.2f €", appalto.getImporto()));
        holder.voceDiSpesa.setText(appalto.getOggetto());
        holder.sceltaContraente.setText(s);

        if (s.toLowerCase().contains("08") ||
                s.toLowerCase().contains("16") ||
                s.toLowerCase().contains("19"))
            holder.sceltaContraente.setTextColor(Color.RED);
        if (s.toLowerCase().contains("04"))
            holder.sceltaContraente.setTextColor(Color.YELLOW);
        if (s.toLowerCase().contains("06"))
            holder.sceltaContraente.setTextColor(Color.rgb(255, 128, 0));
        else
            holder.sceltaContraente.setTextColor(Color.GREEN);
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
        public TextView sceltaContraente;

        public ViewHolder(View itemView) {
            super(itemView);
            voceDiSpesa = (TextView) itemView.findViewById(R.id.voce_di_spesa);
            importo = (TextView) itemView.findViewById(R.id.importo);
            sceltaContraente = (TextView) itemView.findViewById(R.id.scelta_contraente);
        }

        @Override
        public void onClick(View view) {

        }
    }

    // convenience method for getting data at click position
    public Appalto getItem(int id) {
        return appalti.get(id);
    }

    public void filter (List<Appalto> filteredFornitori) {
        appalti = new ArrayList<>();
        appalti.addAll(filteredFornitori);
        notifyDataSetChanged();
    }

}

