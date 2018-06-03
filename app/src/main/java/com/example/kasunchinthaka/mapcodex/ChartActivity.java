package com.example.kasunchinthaka.mapcodex;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
    String charrtData,hu,wa,te;
    int aa1,aa2,aa3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);


        Bundle bundle = getIntent().getExtras();
        String charrtData = bundle.getString("chartData");


        try {
            JSONObject arr = new JSONObject(charrtData);

                 hu = arr.getString("#HUMIDITY");

            Float hu2 = Float.valueOf(hu);
             aa1 = Math.round(hu2);

                wa = arr.getString("#WATERLEVEL");
            Float wa2 = Float.valueOf(wa);
            aa2 = Math.round(wa2);

                te = arr.getString("#TEMP");
            Float te2 = Float.valueOf(te);
            aa3 = Math.round(te2);

//            Toast.makeText(this, aa1 +" "+ aa2+" "+aa3, Toast.LENGTH_SHORT).show();


            } catch (JSONException e1) {
            e1.printStackTrace();
            Toast.makeText(this, "NOT WORKED", Toast.LENGTH_SHORT).show();
        }

        BarChart chart = (BarChart) findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("CODEX");
        chart.animateXY(2000, 2000);
        chart.invalidate();

    }
    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(aa1, 0);
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(aa2, 1);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(aa3, 2);
        valueSet1.add(v1e3);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "ECO MASTER");
        barDataSet1.setColor(Color.rgb(0, 155, 0));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("HUMIDITY");
        xAxis.add("WATERLEVEL");
        xAxis.add("TEMP");
        return xAxis;
    }

    public void onBackPressed(){
        Intent intent = new Intent(this,ChartActivity.class);
        startActivity(intent);

    }
}
