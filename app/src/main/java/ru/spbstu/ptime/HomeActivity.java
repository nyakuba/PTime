package ru.spbstu.ptime;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.spbstu.ptime.constructor.ConstructorActivity;

public class HomeActivity extends Activity {

    Button btnChrono; // объявление кнопки секундомера
    Button toConstructor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnChrono = (Button) findViewById(R.id.btnChrono); // привязка id к кнопке секундомера
        toConstructor = (Button) findViewById(R.id.btnToConstructor);
    }

    public void onChronoClick (View view){ // действие при нажатии на кнопку секундомера
        Intent intent = new Intent(this, ChronoActivity.class); // создание интента для нового класса (activity)
        startActivity(intent); // запуск созданного
    }

    public void onToConstructorClick(View view) {
        Intent intent = new Intent(this, ConstructorActivity.class); // создание интента для нового класса (activity)
        startActivity(intent); // запуск созданного
    }
}
