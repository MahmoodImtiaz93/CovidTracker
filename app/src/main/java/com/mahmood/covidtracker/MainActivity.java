package com.mahmood.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.mahmood.covidtracker.Adapter.RecyclerViewAdapter;
import com.mahmood.covidtracker.Models.ModelClass;
import com.mahmood.covidtracker.Retrofit.RetrofitClient;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<ModelClass> modelClassList;
    private List<ModelClass> modelClassListTwoRecyclerView;
    private RecyclerView recyclerView;

    CountryCodePicker countryCodePicker;
    PieChart pieChart;
    Spinner spinner;
     RecyclerViewAdapter adapter;
    String country;
    String[] types= {"Cases","Deaths","Recovered","Active"};
    TextView mfilter;
    TextView mTodayTotal,mTotal,mTodayActive,mRecovered,mTodayRecovered,mDeaths,
            mActive,mTodayDeaths;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();


        mActive = findViewById(R.id.activeCase);
        mTodayActive = findViewById(R.id.todayactive);
        mDeaths = findViewById(R.id.deathCase);
        mTodayDeaths = findViewById(R.id.todaydeths);
        mRecovered = findViewById(R.id.recoveredCase);
        mTodayRecovered = findViewById(R.id.todayrecovered);
        mTotal = findViewById(R.id.totalcase);
        mTodayTotal = findViewById(R.id.todaytotal);
        mfilter = findViewById(R.id.filter);

        countryCodePicker = findViewById(R.id.countryCodeHolder);
        pieChart = findViewById(R.id.piChart);
        spinner = findViewById(R.id.spinner);
        recyclerView = findViewById(R.id.recyclerView);

        modelClassList = new ArrayList<>();
        modelClassListTwoRecyclerView = new ArrayList<>();

        //Spinner
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

        //Fetching Data to RecyclerView
        RetrofitClient.getApiInterface().getCountryData().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) {
                modelClassListTwoRecyclerView.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {

            }
        });

        adapter = new RecyclerViewAdapter(getApplicationContext(),modelClassListTwoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);



        //CountryPicker & Graph

        countryCodePicker.setAutoDetectedCountry(true);
        country=countryCodePicker.getSelectedCountryName();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country =countryCodePicker.getSelectedCountryName(); //at this point, we can get country name selected by the user
                fetchData();
                
            }
        });



    }

    private void fetchData() {
        RetrofitClient.getApiInterface().getCountryData().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) {
                modelClassList.addAll(response.body());
                for (int position=0;position<modelClassList.size();position++){
                    if (modelClassList.get(position).getCountry().equals(country)){
                        mActive.setText(modelClassList.get(position).getActive());

                        mTodayDeaths.setText(modelClassList.get(position).getTodayDeaths() );

                        mTodayRecovered.setText(modelClassList.get(position).getTodayRecovered());

                        mTodayTotal.setText(modelClassList.get(position).getTodayCases());

                        mDeaths.setText(modelClassList.get(position).getDeaths());

                        mRecovered.setText(modelClassList.get(position).getRecovered());


                        int active,total,recovered,deaths;

                        active = Integer.parseInt(modelClassList.get(position).getCases());
                        total = Integer.parseInt(modelClassList.get(position).getActive());
                        recovered = Integer.parseInt(modelClassList.get(position).getRecovered());
                        deaths = Integer.parseInt(modelClassList.get(position).getDeaths());
                        updateGraph(active,total,recovered,deaths);

                    }

                }

            }

            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {

            }
        });

    }

    private void updateGraph(int active, int total, int recovered, int deaths) {
        pieChart.clearChart();

        pieChart.addPieSlice(new PieModel("Confirm",total, Color.parseColor("#FFB701")));
        pieChart.addPieSlice(new PieModel("Active",active, Color.parseColor("#FF4caf50")));
        pieChart.addPieSlice(new PieModel("Recovered",recovered, Color.parseColor("#38ACCD")));
        pieChart.addPieSlice(new PieModel("Deaths",deaths, Color.parseColor("#F55c47")));
        pieChart.startAnimation();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item=types[position];
        mfilter.setText(item);
        adapter.filter(item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}