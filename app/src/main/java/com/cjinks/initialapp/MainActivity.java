package com.cjinks.initialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.cjinks.initialapp.Util.LocalDateAdapter;
import com.cjinks.initialapp.database.AppDatabase;
import com.cjinks.initialapp.database.Goal;
import com.cjinks.initialapp.database.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddGoalDialog.NoticeDialogListener {

    public static final String EXTRA_MESSAGE = "com.cjink.initialapp.MESSAGE";
    public static final String GOAL_DATA_FILE = "goaldata.json";
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

        // Testing writing json goals
//        try {
//            testWriteGoalJSON();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Testing reading json goals
        try {
            Goal goal = testReadGoalJSON();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void sendMessage(View view)
    {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, db);
        startActivity(intent);
    }

    public void testWriteGoalJSON() throws IOException {
        Goal goal = new Goal("Blog Posts", "Tracking my writing of blog posts", 0, 1);
        goal.addProgressDataPoint(LocalDate.parse("2021-01-01"), 1);
        goal.addProgressDataPoint(LocalDate.parse("2021-01-02"), 0);
        goal.addProgressDataPoint(LocalDate.parse("2021-01-03"), 0);
        goal.addProgressDataPoint(LocalDate.parse("2021-01-04"), 0);
        goal.addProgressDataPoint(LocalDate.parse("2021-01-06"), 1);
        goal.addProgressDataPoint(LocalDate.parse("2021-01-23"), 1);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(goal);

        try (FileOutputStream fos = this.openFileOutput(GOAL_DATA_FILE, Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        }
    }

    public Goal testReadGoalJSON() throws FileNotFoundException {
        String jsonString = "{}";
        try (FileInputStream fis = this.openFileInput(GOAL_DATA_FILE)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            jsonString = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        return gson.fromJson(jsonString, Goal.class);
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