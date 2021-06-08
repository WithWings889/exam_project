package com.example.exam_progect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.card_cities).setOnClickListener(v ->
        {
            Intent intent = new Intent(MainActivity.this, CitiesActivity.class);
            intent.putExtra("flag", 0);
            startActivity(intent);
        });

        findViewById(R.id.card_closest_and_fethest).setOnClickListener( v -> {
            Intent intent = new Intent(MainActivity.this, CitiesActivity.class);
            intent.putExtra("flag", 2);
            startActivity(intent);
        });

        findViewById(R.id.card_big_vities_near_kyiv).setOnClickListener( v -> {
            Intent intent = new Intent(MainActivity.this, CitiesActivity.class);
            intent.putExtra("flag", 1);
            startActivity(intent);
        });

        findViewById(R.id.card_crypto).setOnClickListener(v ->
        {
            startActivity(new Intent(MainActivity.this, CryptoActivity.class));
        });

        findViewById(R.id.card_contacts).setOnClickListener(v ->
        {
            startActivity(new Intent(MainActivity.this, ContactsActivity.class));
        });

        findViewById(R.id.card_developer).setOnClickListener(v ->
        {
            startActivity(new Intent(MainActivity.this, InfoActivity.class));
        });

        findViewById(R.id.card_manual).setOnClickListener(v ->
        {
            startActivity(new Intent(MainActivity.this, ManualActivity.class));
        });
    }
}