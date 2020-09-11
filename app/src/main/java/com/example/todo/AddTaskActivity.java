package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todo.Client.DatabaseClient;

public class AddTaskActivity extends AppCompatActivity {

    EditText editTextTask, editTextDesc, editTextFinishedBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTask = findViewById(R.id.editTextTask);

        editTextDesc = findViewById(R.id.editTextDesc);

        editTextFinishedBy = findViewById(R.id.editTextFinishBy);

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

    }

    private void saveTask() {

        final String sTask = editTextTask.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sFinishBy = editTextFinishedBy.getText().toString().trim();

        if(sTask.isEmpty()){
            editTextTask.setError("Enter Task");
            editTextTask.requestFocus();
            return;
        }

        if(sDesc.isEmpty()){
            editTextDesc.setError("Enter Description");
            editTextDesc.requestFocus();
            return;
        }

        if(sFinishBy.isEmpty()){
            editTextFinishedBy.setError("Finished by required ");
            editTextFinishedBy.requestFocus();
            return;
        }

        class SaveTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {


                //creating a task
                Task task = new Task();
                task.setTask(sTask);
                task.setDesc(sDesc);
                task.setFinsihedBy(sFinishBy);
                task.setFinished(false);

                //adding to database

                DatabaseClient.getInstance(AddTaskActivity.this).getAppDatabase().taskDao().insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();

    }

}
