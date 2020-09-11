package com.example.todo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todo.Interface.TaskDao;

@Database(entities = {Task.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
