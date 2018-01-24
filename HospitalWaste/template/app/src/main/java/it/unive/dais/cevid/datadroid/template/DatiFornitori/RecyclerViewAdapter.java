package it.unive.dais.cevid.datadroid.template.DatiFornitori;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unive.dais.cevid.datadroid.template.DatiFornitori.ulssFornite.UlssFornite;
import it.unive.dais.cevid.datadroid.template.R;

/**
 * Created by francescobenvenuto on 11/01/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<String> stringList = Collections.emptyList();
    private LayoutInflater mInflater;


    private Context context;
    protected Context getContext(){
        return context;
    }
    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.stringList = data;

        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_textview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text_view_element.setText(stringList.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return stringList.size();
    }

    protected void manageOnClick(View view){
        TextView text = (TextView) view;

        Intent intent = new Intent(context, UlssFornite.class);
        intent.putExtra("fornitore", text.getText());

        context.startActivity(intent);
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView text_view_element;

        public ViewHolder(View itemView) {
            super(itemView);
            text_view_element = (TextView) itemView.findViewById(R.id.text_view_item);

            text_view_element.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            manageOnClick(view);
        }
    }

    public void filter (List<String> filteredFornitori) {
        stringList = new ArrayList<>();
        stringList.addAll(filteredFornitori);
        notifyDataSetChanged();
    }
}

