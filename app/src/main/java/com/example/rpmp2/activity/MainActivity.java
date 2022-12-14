package com.example.rpmp2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.rpmp2.R;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.database_button).setOnClickListener(view -> startActivity(new Intent(this, DatabaseActivity.class)));
        findViewById(R.id.contacts_button).setOnClickListener(view -> startActivity(new Intent(this, ContactsActivity.class)));
        findViewById(R.id.geoservice_button).setOnClickListener(view -> startActivity(new Intent(this, GeoserviceActivity.class)));
    }
}