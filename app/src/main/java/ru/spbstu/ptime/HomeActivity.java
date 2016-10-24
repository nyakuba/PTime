package ru.spbstu.ptime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button btnChrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnChrono = (Button) findViewById(R.id.btnChrono);
    }

    public void onChronoClick (View view){
        Intent intent = new Intent(this, ChronoActivity.class);
        startActivity(intent);
    }
}
