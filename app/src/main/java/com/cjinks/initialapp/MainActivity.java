package com.cjinks.initialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;

import com.cjinks.initialapp.database.AppDatabase;
import com.cjinks.initialapp.database.User;
import com.cjinks.initialapp.database.UserDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AddGoalDialog.NoticeDialogListener {

    public static final String EXTRA_MESSAGE = "com.cjink.initialapp.MESSAGE";
    protected ArrayList<String> goals;
    protected CustomAdapter adapter;
    protected AppDatabase db;

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

        goals = new ArrayList<String>();
        adapter = new CustomAdapter(this, goals);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            DialogFragment frag = new AddGoalDialog();
            frag.show(getSupportFragmentManager(), "addgoalfrag");
        });

        // Create database and insert a new user
        db = AppDatabase.getInstance(this);
        User user = new User(111, "Ciri", "Beeny");

        AsyncTask.execute(() -> {
            db.userDao().insert(user);
        });
     }
    
    public void sendMessage(View view)
    {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, db);
        startActivity(intent);
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String goal) {
        goals.add(goal);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing if they choose not to add a goal
    }
}