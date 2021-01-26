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
import com.cjinks.initialapp.Database.AppDatabase;
import com.cjinks.initialapp.Database.Goal;
import com.cjinks.initialapp.Database.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        try {
            testWriteGoalJSON();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Testing reading json goals
        try {
            ArrayList<Goal> goal = testReadGoalJSON();
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

        Goal goal2 = new Goal("Workouts", "Tracking my workouts", 0, 10);
        goal2.addProgressDataPoint(LocalDate.parse("2021-01-03"), 1);
        goal2.addProgressDataPoint(LocalDate.parse("2021-01-04"), 7);
        goal2.addProgressDataPoint(LocalDate.parse("2021-01-05"), 0);
        goal2.addProgressDataPoint(LocalDate.parse("2021-01-06"), 8);
        goal2.addProgressDataPoint(LocalDate.parse("2021-01-07"), 8);
        goal2.addProgressDataPoint(LocalDate.parse("2021-01-19"), 4);

        ArrayList<Goal> goals = new ArrayList<>();
        goals.add(goal);
        goals.add(goal2);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(goals);

        try (FileOutputStream fos = this.openFileOutput(GOAL_DATA_FILE, Context.MODE_PRIVATE)) {
            fos.write(json.getBytes());
        }
    }

    public ArrayList<Goal> testReadGoalJSON() throws FileNotFoundException {
        String jsonString = "{}";
        try (FileInputStream fis = this.openFileInput(GOAL_DATA_FILE)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            jsonString = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        // Using https://www.baeldung.com/gson-list for list deserialization type
        return gson.fromJson(jsonString, new TypeToken<ArrayList<Goal>>() {}.getType());
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