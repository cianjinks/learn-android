package com.cjinks.initialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.cjinks.initialapp.Database.AppDatabase;
import com.cjinks.initialapp.Database.User;

import java.util.List;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView textView = findViewById(R.id.textView3);
        textView.setText(message);

        TextView textView2 = findViewById(R.id.textView4);
        AsyncTask.execute(() -> {
            List<User> users = AppDatabase.getInstance(this).userDao().getAll();
            textView2.setText(users.get(0).firstName);
        });
    }
}