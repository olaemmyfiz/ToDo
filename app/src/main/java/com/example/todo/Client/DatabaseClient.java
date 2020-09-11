package com.example.todo.Client;

import android.content.Context;

import androidx.room.Room;

import com.example.todo.AppDatabase;

public class DatabaseClient {
    private Context context;
    private static DatabaseClient instance;

    //app database object
    private AppDatabase appDatabase;

    public DatabaseClient(Context context) {
        this.context = context;

        //creating the app database with Room database builder
        //name of Database is Todo.

        appDatabase = Room.databaseBuilder(context,AppDatabase.class,"MyToDos").build();
    }

    public static synchronized DatabaseClient getInstance(Context context){
        if(instance == null){
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public AppDatabase getAppDatabase(){

        return appDatabase;
    }

}
