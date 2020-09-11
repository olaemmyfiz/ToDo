package com.example.todo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todo.Client.DatabaseClient;

public class updateTaskActivity extends AppCompatActivity {

    private EditText editTextTask, editTextDesc, editTextFinishBy;
    private CheckBox checkBoxFinished;

    Button Update, Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        editTextTask = findViewById(R.id.editTextTask);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextFinishBy = findViewById(R.id.editTextFinishBy);

        Update = findViewById(R.id.button_update);
        Delete = findViewById(R.id.button_delete);

        checkBoxFinished = findViewById(R.id.checkBoxFinished);

        final Task task = (Task) getIntent().getSerializableExtra("task");

        loadTask(task);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                
                updatetask(task);
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(updateTaskActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask(task);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog ad = builder.create();
                ad.show();
            }
        });

    }

    private void deleteTask(final Task task) {

        class DeleteTask extends AsyncTask<Void, Void, Void>{


            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(updateTaskActivity.this, MainActivity.class));
            }
        }

        DeleteTask deleteTask = new DeleteTask();
        deleteTask.execute();
    }

    private void updatetask(final Task task) {
       final String sTask = editTextTask.getText().toString().trim();
       final String sDesc = editTextDesc.getText().toString().trim();
       final String sFinishedBy =editTextFinishBy.getText().toString().trim();

       if(sTask.isEmpty()){

           editTextTask.setError("Task required");
           editTextTask.requestFocus();
           return;
       }

       if(sDesc.isEmpty()){
           editTextDesc.setError("Desc required");
           editTextDesc.requestFocus();
           return;
       }

       if (sFinishedBy.isEmpty()){
           editTextFinishBy.setError("Finish Required");
           editTextFinishBy.requestFocus();
           return;
       }

       class UpdateTask extends AsyncTask<Void,Void,Void>{

           @SuppressLint("WrongThread")
           @Override
           protected Void doInBackground(Void... voids) {
               task.setTask(sTask);
               task.setDesc(sDesc);
               task.setFinsihedBy(sFinishedBy);
               task.setFinished(checkBoxFinished.isChecked());
               DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                       .taskDao()
                       .update(task);
               return null;

           }

           @Override
           protected void onPostExecute(Void aVoid) {
               super.onPostExecute(aVoid);
               Toast.makeText(getApplicationContext(), "Update", Toast.LENGTH_SHORT).show();
               finish();
               startActivity(new Intent(updateTaskActivity.this,MainActivity.class));
           }
       }

       UpdateTask updateTask = new UpdateTask();
       updateTask.execute();

    }

    private void loadTask(Task task) {

        editTextTask.setText(task.getTask());
        editTextDesc.setText(task.getDesc());
        editTextFinishBy.setText(task.getFinsihedBy());
        checkBoxFinished.setChecked(task.isFinished());
    }
}
