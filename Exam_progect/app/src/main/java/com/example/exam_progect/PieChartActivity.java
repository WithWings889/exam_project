package com.example.exam_progect;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PieChartActivity extends AppCompatActivity {
    private PieChart pieChart;
    private String stringData;

    final String vowelEnglish = "aeiou";
    final String consonantEnglish = "bcdfghjklmnpqrstvwxyz";
    final String vowelRussian = "аеёиоуыэюя";
    final String consonantRussian = "бвгджзйклмнпрстфхцчшщъь";
    final String vowelUkrainian = "аеєиіїоуюя";
    final String consonantUkrainian = "бвгґджзйклмнпрстфхцчшщь";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        pieChart = findViewById(R.id.pieChart1);

        stringData = getIntent().getExtras().getString("data");

        EditText text = new EditText(this);
        text.setText(stringData);

        if(!stringData.isEmpty()){
            loadPieChart();
            setupPieChart();
        }
    }

    private ArrayList<Integer> analiseData(){

        int vowelCount = 0, consonantCount = 0, symbolsCount = 0;
        ArrayList<Integer> result = new ArrayList<>();
        String currentVowel, currentConsonant;

        if(stringData.length() == 0)
            return result;

        if(vowelEnglish.indexOf(stringData.charAt(0)) != -1 || consonantEnglish.indexOf(stringData.charAt(0)) != -1)
        {
            currentConsonant = consonantEnglish;
            currentVowel = vowelEnglish;
        }
        else if(vowelRussian.indexOf(stringData.charAt(0)) != -1 || consonantRussian.indexOf(stringData.charAt(0)) != -1)
        {
            currentConsonant = consonantRussian;
            currentVowel = vowelRussian;
        }
        else
        {
            currentConsonant = consonantUkrainian;
            currentVowel = vowelUkrainian;
        }

        for(int i = 0; i < stringData.length(); ++i)
        {
            if(currentConsonant.indexOf(stringData.charAt(i)) != -1) consonantCount++;
            else if(currentVowel.indexOf(stringData.charAt(i)) != -1)  vowelCount++;
            else symbolsCount++;
        }

        result.add(vowelCount);
        result.add(consonantCount);
        result.add(symbolsCount);
        return result;
    }

    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setCenterText("Статистика");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);
    }


    private void loadPieChart(){

        ArrayList<Integer> stringData = analiseData();
        int sum = stringData.get(0) + stringData.get(1) + stringData.get(2);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float)stringData.get(0)/(float)sum, "Vowel"));
        entries.add(new PieEntry((float)stringData.get(1)/(float)sum, "Consonant"));
        entries.add(new PieEntry((float)stringData.get(2)/(float)sum, "Symbol"));

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Category");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        //data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}