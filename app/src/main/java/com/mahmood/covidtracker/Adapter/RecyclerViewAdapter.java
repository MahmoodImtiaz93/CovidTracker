package com.mahmood.covidtracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmood.covidtracker.Models.ModelClass;
import com.mahmood.covidtracker.R;

import java.text.NumberFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    int m=1;
    Context context;
    List<ModelClass> countrylist;

    public RecyclerViewAdapter(Context context, List<ModelClass> countrylist) {
        this.context = context;
        this.countrylist = countrylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ModelClass modelClass = countrylist.get(position);
        if (m==1){
            holder.cases.setText(NumberFormat.getInstance()
                    .format(Integer.parseInt(modelClass.getCases())));
        } else if (m==2){
            holder.cases.setText(NumberFormat.getInstance()
                    .format(Integer.parseInt(modelClass.getRecovered())));
        } else if (m==3){
            holder.cases.setText(NumberFormat.getInstance()
                    .format(Integer.parseInt(modelClass.getDeaths())));
        }else {
            holder.cases.setText(NumberFormat.getInstance()
                    .format(Integer.parseInt(modelClass.getActive())));
        }
        holder.county.setText(modelClass.getCountry());

    }

    @Override
    public int getItemCount() {
        return countrylist.size();
    }

    public void filter(String charText){
        if (charText.equals("Cases")){
            m=1;
        }else if (charText.equals("Deaths")){
            m=2;
        } else if (charText.equals("Recovered")){
            m=3;
        }else {
            m=4;
        }
        notifyDataSetChanged();
    }
}
