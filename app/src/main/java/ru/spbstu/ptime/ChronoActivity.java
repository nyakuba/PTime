package ru.spbstu.ptime;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;


public class ChronoActivity extends Activity {
    // объявление новых переменных
    private Chronometer mChr;
    private Button btnStart;
    private Button btnStop;
    private Button btnReset;
    private boolean resume = false; // продолжение отсчета
    private boolean chrLnchd = false; // запущен ли секундомер
    private String currentTime = ""; // строка текущего времени
    private long elapsedTime = 0; // истекшее время секундомера
    final String LOG_TAG = "chronometer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrono);

        Log.d(LOG_TAG, "onCreate");

        // привязка кнопок по id
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnReset = (Button) findViewById(R.id.btnReset);

        mChr = (Chronometer) findViewById(R.id.chronometer); // привязка секундомера по id
        Chronometer.OnChronometerTickListener chronoListr = Chrono(mChr); // создание TickListener и присваивание шрифта

        // установка активности кнопок
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnReset.setEnabled(false);

        mChr.setOnChronometerTickListener(chronoListr); // привязка к созданному TickListener
    }

    public  Chronometer.OnChronometerTickListener Chrono (final Chronometer chr) {
        Typeface dec = Typeface.createFromAsset(getAssets(), getString(R.string.digit_keyboard_font)); // установка пользовательского шрифта
        mChr.setTypeface(dec);
        Chronometer.OnChronometerTickListener tickList = new Chronometer.OnChronometerTickListener() { // создание TickListener для секундомера
            @Override
            public void onChronometerTick(Chronometer arg0) { // что делать на каждый "тик"
                long hour, min, sec, temp;
                if (!resume){ // если не возобновление секундомера
                    // подсчитываем время
                    temp = SystemClock.elapsedRealtime() - chr.getBase();
                    hour = (temp/1000)/3600;
                    min = (temp/1000)/60;
                    sec = (temp/1000)%60;
                    if (hour < 10) {
                        if (min < 10) {
                            if (sec < 10) currentTime = "0" + hour + ":0" + min + ":0" + sec;
                            else currentTime = "0" + hour + ":0" + min + ":" + sec;
                        }
                        else {
                            if (sec < 10) currentTime = "0" + hour + ":" + min + ":0" + sec;
                            else currentTime = "0" + hour + ":" + min + ":" + sec;
                        }
                    }
                    else{
                        if (min < 10) {
                            if (sec < 10) currentTime = hour + ":0" + min + ":0" + sec;
                            else currentTime = hour + ":0" + min + ":" + sec;
                        }
                        else {
                            if (sec < 10) currentTime = hour + ":" + min + ":0" + sec;
                            else currentTime = hour + ":" + min + ":" + sec;
                        }
                    }
                    arg0.setText(currentTime); // устанавливаем строку текущего времени
                    elapsedTime = SystemClock.elapsedRealtime(); // запоминаем прошедшее время
                }
                else{ // если возобновление секундомера
                    temp = elapsedTime - chr.getBase();
                    hour = (temp/1000)/3600;
                    min = (temp/1000)/60;
                    sec = (temp/1000)%60;
                    if (hour < 10) {
                        if (min < 10) {
                            if (sec < 10) currentTime = "0" + hour + ":0" + min + ":0" + sec;
                            else currentTime = "0" + hour + ":0" + min + ":" + sec;
                        }
                        else {
                            if (sec < 10) currentTime = "0" + hour + ":" + min + ":0" + sec;
                            else currentTime = "0" + hour + ":" + min + ":" + sec;
                        }
                    }
                    else{
                        if (min < 10) {
                            if (sec < 10) currentTime = hour + ":0" + min + ":0" + sec;
                            else currentTime = hour + ":0" + min + ":" + sec;
                        }
                        else {
                            if (sec < 10) currentTime = hour + ":" + min + ":0" + sec;
                            else currentTime = hour + ":" + min + ":" + sec;
                        }
                    }
                    arg0.setText(currentTime); // устанавливаем строку текущего времени
                    elapsedTime = elapsedTime + 1000; // продолжаем отсчет времени
                }
            }
        };
        return tickList;
    }

    protected void onDestroy() { // состояние Activity удален
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    protected void onPause() { // состояние Activity приостановлен
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    protected void onRestart() { // состояние Activity перезапущен
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) { // восстановление сохраненных данных при возобновлении Activity (не удалении)
        super.onRestoreInstanceState(savedInstanceState);
        mChr.setBase(savedInstanceState.getLong("base"));
        resume = savedInstanceState.getBoolean("resume");
        chrLnchd = savedInstanceState.getBoolean("chrLnchd");
        currentTime = savedInstanceState.getString("currentTime");
        elapsedTime = savedInstanceState.getLong("elapsedTime");
        btnStart.setEnabled(savedInstanceState.getBoolean("enBtnStart"));
        btnStop.setEnabled(savedInstanceState.getBoolean("enBtnStop"));
        btnReset.setEnabled(savedInstanceState.getBoolean("enBtnReset"));
        btnStart.setText(savedInstanceState.getCharSequence("tBtnStart"));
        btnStop.setText(savedInstanceState.getCharSequence("tBtnStop"));
        btnReset.setText(savedInstanceState.getCharSequence("tBtnReset"));
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    protected void onResume() { // состояние Activity возобновлен
        super.onResume();
        if (chrLnchd) mChr.start(); // если секундомер был запущен до возобновления, запускаем его
        else
            if (currentTime != "") mChr.setText(currentTime);
            else mChr.setText(R.string.start_time);
        Log.d(LOG_TAG, "onResume ");
    }

    protected void onSaveInstanceState(Bundle outState) { // сохранение данных при перезапуске Activity
        super.onSaveInstanceState(outState);
        outState.putLong("base", mChr.getBase());
        outState.putBoolean("resume", resume);
        outState.putBoolean("chrLnchd", chrLnchd);
        outState.putString("currentTime", currentTime);
        outState.putLong("elapsedTime", elapsedTime);
        outState.putBoolean("enBtnStart", btnStart.isEnabled());
        outState.putBoolean("enBtnStop", btnStop.isEnabled());
        outState.putBoolean("enBtnReset", btnReset.isEnabled());
        outState.putCharSequence("tBtnStart", btnStart.getText());
        outState.putCharSequence("tBtnStop", btnStop.getText());
        outState.putCharSequence("tBtnReset", btnReset.getText());
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    protected void onStart() { // состояние запуска Activity
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    protected void onStop() { // состояние остановки работы Activity
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    public void onStartClick(View view) { // действие при нажатии кнопки старта секундомера
        if(!resume) mChr.setBase(SystemClock.elapsedRealtime()); // если не возобновление секундомера, то сохраняем время начала работы
        mChr.start(); // запускаем секундомер
        chrLnchd = true; // секундомер запущен
        // устанавливаем активность кнопок
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        btnReset.setEnabled(true);
        // устанавливаем текст кнопок
        btnStart.setText("Запущен");
        btnReset.setText(R.string.reset);
        btnStop.setText(R.string.stop);
        Toast.makeText(this, "Секундомер запущен", Toast.LENGTH_SHORT).show(); // создаем всплывающее сообщение
    }

    public void onStopClick(View view) { // действие при нажатии кнопки останова
        resume = true; // чтобы была возможность восстановить отсчет
        mChr.stop(); // останавливаем секундомер
        chrLnchd = false; // секундомер остановлен
        mChr.setText(currentTime); // выводим текущее время
        // устанавливаем активность и текст кнопок
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnReset.setEnabled(true);
        btnStop.setText("Остановлен");
        btnStart.setText(R.string.start);
        Toast.makeText(this, "Секундомер остановлен", Toast.LENGTH_SHORT).show(); // создаем всплывающее сообщение
    }

    public void onResetClick(View view) {
        mChr.stop(); // останавливаем секундомер
        chrLnchd = false; // секундомер остановлен
        mChr.setText(R.string.start_time); // устанавливаем начальную строку текущего времени
        currentTime = "";
        resume = false; // выключаем возможность возобновления секундомера
        // устанавливаем активность и текст кнопок
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnReset.setEnabled(false);
        btnReset.setText("Сброшен");
        btnStop.setText(R.string.stop);
        btnStart.setText(R.string.start);
        Toast.makeText(this, "Секундомер сброшен", Toast.LENGTH_SHORT).show(); // создаем всплывающее сообщение
    }
}
