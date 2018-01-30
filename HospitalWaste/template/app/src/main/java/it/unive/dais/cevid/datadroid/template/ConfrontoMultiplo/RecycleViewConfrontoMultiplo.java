package it.unive.dais.cevid.datadroid.template.ConfrontoMultiplo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by Aure on 27/01/2018.
 */

public class RecycleViewConfrontoMultiplo extends RecyclerView.Adapter<RecycleViewConfrontoMultiplo.ViewHolder> {
    private LayoutInflater mInflater;
    private List<String> vociBilancio;
    private List<Double> importi;
    private String nomeUlss;
    private int postiLetto;
    // data is passed into the constructor
    public RecycleViewConfrontoMultiplo(Context context, List<String> vociBilancio, List<Double> importi, String nomeUlss, int postiLetto) {
        this.mInflater = LayoutInflater.from(context);
        this.vociBilancio = vociBilancio;
        this.nomeUlss = nomeUlss;
        this.importi = importi;
        this.postiLetto = postiLetto;
    }

    // inflates the row layout from xml when needed
    @Override
    public RecycleViewConfrontoMultiplo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycle_view_confronto_multiplo, parent, false);
        RecycleViewConfrontoMultiplo.ViewHolder viewHolder = new RecycleViewConfrontoMultiplo.ViewHolder(view);
        return viewHolder;
    }



    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(RecycleViewConfrontoMultiplo.ViewHolder holder, int position) {
        holder.importo.setText(String.format("Importto voce di Bilancio: %.2f €", importi.get(position)));
        holder.voceDiSpesa.setText(vociBilancio.get(position));
        if (postiLetto != 0)
            holder.spesaProCapite.setText(String.format("Importo / Posti letto: %.2f €", importi.get(position) / postiLetto));
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
            voceDiSpesa = (TextView) itemView.findViewById(R.id.confronto_voce_di_spesa);
            importo = (TextView) itemView.findViewById(R.id.confronto_importo);
            spesaProCapite = (TextView) itemView.findViewById(R.id.confronto_spesa_pro_capite);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
