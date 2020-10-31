package com.ucab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mConducteur, mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConducteur = (Button) findViewById(R.id.conducteur);
        mClient = (Button) findViewById(R.id.client);

        mConducteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginConducteur.class);
                startActivity(intent);
                finish();
                return;
            }
        });


        mClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginClient.class);
                startActivity(intent);
                finish();
                return;
            }
        });

    }
}