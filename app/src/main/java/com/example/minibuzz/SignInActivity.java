package com.example.minibuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {
    TextView txtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtTitle=(TextView)findViewById(R.id.txtTitle);
        //txtTitle.setShadowLayer(50, 0, 0, Color.WHITE);


    }
}