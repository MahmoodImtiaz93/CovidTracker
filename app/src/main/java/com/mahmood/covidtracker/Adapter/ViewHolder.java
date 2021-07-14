package com.mahmood.covidtracker.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmood.covidtracker.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView cases,county;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        cases=itemView.findViewById(R.id.countryCase);
        county=itemView.findViewById(R.id.countryName);
    }
}
