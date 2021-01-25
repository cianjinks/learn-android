package com.cjinks.initialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AddGoalDialog.NoticeDialogListener {

    public static final String EXTRA_MESSAGE = "com.cjink.initialapp.MESSAGE";
    protected ArrayList<String> data;
    protected CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        RecyclerView recyclerView = (RecyclerView)  findViewById(R.id.recyclerview);
        //GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        generateData();
        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            DialogFragment frag = new AddGoalDialog();
            frag.show(getSupportFragmentManager(), "addgoalfrag");
            //Random rand = new Random();
            //data.add("This box says " + Math.abs(rand.nextInt() % 10));
            //adapter.notifyDataSetChanged();
        });

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
        int dataPoints = 3;
        data = new ArrayList<String>();

        Random rand = new Random();
        for(int i = 0; i < dataPoints; i++)
        {
            data.add("This box says " + Math.abs(rand.nextInt() % 10));
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        data.add("test");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing if they choose not to add a goal
    }
}