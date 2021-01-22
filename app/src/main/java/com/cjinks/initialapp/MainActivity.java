package com.cjinks.initialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.cjink.initialapp.MESSAGE";
    protected String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView)  findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        generateData();
        CustomAdapter adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);

     }
    
    public void sendMessage(View view)
    {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void generateData()
    {
        int dataPoints = 30;
        data = new String[dataPoints];

        Random rand = new Random();
        for(int i = 0; i < dataPoints; i++)
        {
            data[i] = String.valueOf(Math.abs(rand.nextInt() % 10));
        }
    }
}