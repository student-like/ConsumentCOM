package com.action_bar.student_like.multipleactivities;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SecondScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.second_layout);
    }

    public void wasmachtdas2(View view) {

        Intent intent2 = new Intent(this,
                MainActivity.class);

        startActivity(intent2);
    }
}
