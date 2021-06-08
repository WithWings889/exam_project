package com.example.exam_progect;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CitiesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_data;

    DatabaseHelper myDB;
    ArrayList<String> city_id, city_number, city_distance, city_name;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        city_id = new ArrayList<>();
        city_name = new ArrayList<>();
        city_number = new ArrayList<>();
        city_distance = new ArrayList<>();

        myDB = new DatabaseHelper(getBaseContext());

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitiesActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        int flag = getIntent().getExtras().getInt("flag");
        citiesChooser(flag);
    }

    private void citiesChooser(int flag){
        if(flag == 0){
            Cursor result = myDB.getAllData();
            parseData(result);
        }
        else if(flag == 1){
            Cursor result = myDB.numberOfCitizensAndDistance();
            parseData(result);
        }
        else{
            Cursor result = myDB.maxAndMinDistance();
            parseData(result);
        }
    }


    public void parseData(Cursor result) {
        if (result != null && result.getCount() >0 && result.moveToFirst()) {
            city_id.clear();
            city_name.clear();
            city_number.clear();
            city_distance.clear();

            for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
                city_id.add(result.getString(0));
                city_name.add(result.getString(1));
                city_number.add(result.getString(2));
                city_distance.add(result.getString(3));
            }

            customAdapter = new CustomAdapter(CitiesActivity.this, this, city_id, city_name, city_number,
                    city_distance);
            recyclerView.setAdapter(customAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(CitiesActivity.this));

            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        } else {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }
    }
}
