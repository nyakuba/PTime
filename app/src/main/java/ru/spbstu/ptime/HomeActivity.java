package ru.spbstu.ptime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button btnChrono; // объявление кнопки секундомера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnChrono = (Button) findViewById(R.id.btnChrono); // привязка id к кнопке секундомера
    }

    public void onChronoClick (View view){ // действие при нажатии на кнопку секундомера
        Intent intent = new Intent(this, ChronoActivity.class); // создание интента для нового класса (activity)
        startActivity(intent); // запуск созданного
    }
}
