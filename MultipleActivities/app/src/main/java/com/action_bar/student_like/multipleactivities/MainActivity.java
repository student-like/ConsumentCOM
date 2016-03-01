package com.action_bar.student_like.multipleactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void wasmachtdas1(View view) {

        Intent intent = new Intent(this,
                SecondScreen.class);

        startActivity(intent);
    }
}
