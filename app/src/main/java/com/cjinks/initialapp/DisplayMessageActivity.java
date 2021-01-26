package com.cjinks.initialapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.widget.TextView;

import com.cjinks.initialapp.database.AppDatabase;
import com.cjinks.initialapp.database.User;
import com.cjinks.initialapp.database.UserDao;

import java.util.List;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //TextView textView = findViewById(R.id.textView3);
        //textView.setText(message);

        TextView textView = findViewById(R.id.textView3);
        AsyncTask.execute(() -> {
            List<User> users = AppDatabase.getInstance(this).userDao().getAll();
            textView.setText(users.get(0).firstName);
        });
    }
}