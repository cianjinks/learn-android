package com.cjinks.initialapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String databaseName = "user_db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, databaseName).fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract UserDao userDao();
}
