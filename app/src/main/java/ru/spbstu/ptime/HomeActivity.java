package ru.spbstu.ptime;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

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


    protected static final String LOG_TAG = "my_tag";
    TabHost.TabSpec tabSpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        // инициализация
        tabHost.setup();

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Вкладка 1");
        tabSpec.setContent(R.id.chrono);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Вкладка 2");
        tabSpec.setContent(R.id.timer);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Вкладка 3");
        tabSpec.setContent(R.id.prog);
        tabHost.addTab(tabSpec);

        // вторая вкладка по умолчанию активна
        tabHost.setCurrentTabByTag("tag1");

        // логгируем переключение вкладок
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                Log.d(LOG_TAG, "tabId = " + tabId);

            }
        });

    }

}