package ru.spbstu.ptime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Timer;

public class TimerActivity extends Activity {

    private Button timerBtnStart, timerBtnSetTime, timerBtnStop;
    private Chronometer timer;
    private ProgressBar timerProgressBar;
    private AlertDialog alert;
    private MyTimePickerDialog mTimePicker;
    private MediaPlayer timerSoundMP;

    int defaultColor;   // костыль для изменения цвета "00:00:00"

    private boolean isSoundPlaying = false;
    private boolean isAlertShowing = false;
    private boolean isSetTimeDialog = false;
    int currTimePickerHour = 0;
    int currTimePickerMin = 0;
    int currTimePickerSec = 0;
    private boolean isLaunchd = false;
    long elapsedTimeInMillis = 0;       // вспомогательная переменная для случая возобновления активити
    long currTimerTimeInMillis = 0;     // время, отображаемое на экране
    int MaxProgress = 10000;            // процент пройденного времени для прогресс бара (0..100)
    long fullTimerTimeInMillis = 0;  // промежуток времени, заданный пользователем

    int i = 0;      // фича

    final String LOG_TAG = "TimerActivity";

    int GetHours(long millis){
        return (int)((millis/1000)/3600000);
    }

    int GetMinuts(long millis){
        return (int)(((millis/1000)%3600)/60);
    }

    int GetSeconds(long millis){
        return (int)((millis/1000)%60);
    }

    String MillisesondsToString (long millis)
    {
        return String.format("%02d:%02d:%02d", (millis / 1000) / 3600, ((millis / 1000) % 3600) / 60, (millis / 1000) % 60);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Log.d(LOG_TAG, "TimerOnCreate");

        timerBtnStart = (Button) findViewById(R.id.timerBtnStart);
        timerBtnSetTime = (Button) findViewById(R.id.timerBtnSetTime);
        timerBtnStop = (Button) findViewById(R.id.timerBtnStop);
        timer = (Chronometer) findViewById(R.id.timerChronometer);
        timerProgressBar = (ProgressBar) findViewById(R.id.timerProgressBar);
        timerProgressBar.setMax(MaxProgress);

        defaultColor = timer.getCurrentTextColor();     // костыль для изменения цвета "00:00:00"

        Chronometer.OnChronometerTickListener timerListner = MyTimer(timer);

        timerBtnStart.setEnabled(true);
        timerBtnSetTime.setEnabled(true);
        timerBtnStop.setEnabled(false);

        timer.setOnChronometerTickListener(timerListner);

        timerSoundMP = MediaPlayer.create(TimerActivity.this, R.raw.lg_timer);
        timerSoundMP.setLooping(true);

        // создаем диалоговое окно, сообщающее о завершении таймера
        AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this);
        builder.setTitle("Timer")
                .setMessage("Time is up")
                .setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                isAlertShowing = false;
                                timerSoundMP.stop();
                                isSoundPlaying = false;
                            }
                        });
        alert = builder.create();
    }

    public  Chronometer.OnChronometerTickListener MyTimer (final Chronometer timer) {
        Chronometer.OnChronometerTickListener tickList = new Chronometer.OnChronometerTickListener() { // TickListener для таймера
            @Override
            public void onChronometerTick(Chronometer arg0) { // что делать на каждый "тик"

                // если не возобновление таймера
                currTimerTimeInMillis = fullTimerTimeInMillis - SystemClock.elapsedRealtime() + timer.getBase() + 500;  // +500mlsec чтобы покрыть задержку
                arg0.setText(MillisesondsToString(currTimerTimeInMillis));
                timerProgressBar.setProgress((int)((1.0 - ((double)currTimerTimeInMillis/(double)fullTimerTimeInMillis)) * (double)MaxProgress));   //вычисляем и устанавливаем прогресс для прогресс-бара
                elapsedTimeInMillis = SystemClock.elapsedRealtime();    //  запоминаем прошедшее время

                // если время истекло (следующий "тик" после 00:00:00)
                if (currTimerTimeInMillis < 0)
                {
                    timer.stop();                       // останавливаем таймер
                    isLaunchd = false;                  // таймер остановлен
                    timer.setTextColor(defaultColor);   // возвращаем цвет цифр по умолчанию
                    timer.setText(MillisesondsToString(fullTimerTimeInMillis)); // устанавливаем начальную строку таймера
                    timerProgressBar.setProgress(0);    // сбрасываем ProgressBar
                    currTimerTimeInMillis = fullTimerTimeInMillis;
                    timerBtnStart.setEnabled(true);
                    timerBtnSetTime.setEnabled(true);
                    timerBtnStop.setEnabled(false);
                }
                // если на этот "тик" таймер досшел до 00:00:00
                else if (currTimerTimeInMillis < 1000)
                {
                    timer.setTextColor(getResources().getColor(R.color.colorAccent));       // выделяем 00:00:00 цветом
                    timerSoundMP.start();
                    isSoundPlaying = true;
                    timerProgressBar.setProgress(timerProgressBar.getMax());                // прогресс-бар дошел до конца
                    alert.show();   // отображаем диалоговое окно
                    isAlertShowing = true;
                }
            }
        };
        return tickList;
    }

    // состояние Activity удален
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "TimerOnDestroy");
    }

    // состояние Activity приостановлен
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "TimerOnPause");
    }

    // состояние Activity перезапущен
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "TimerOnRestart");
    }

    // восстановление сохраненных данных при возобновлении Activity (не удалении)
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timer.setBase(savedInstanceState.getLong("base"));
        fullTimerTimeInMillis = savedInstanceState.getLong("fullTimerTimeInMillis");
        isLaunchd = savedInstanceState.getBoolean("isLaunchd");
        timerProgressBar.setProgress(savedInstanceState.getInt("currentProgress"));
        currTimerTimeInMillis = savedInstanceState.getLong("currTimerTimeInMillis");
        elapsedTimeInMillis = savedInstanceState.getLong("elapsedTimeInMillis");
        timerBtnStart.setEnabled(savedInstanceState.getBoolean("enBtnStart"));
        timerBtnSetTime.setEnabled(savedInstanceState.getBoolean("enBtnSetTime"));
        timerBtnStop.setEnabled(savedInstanceState.getBoolean("enBtnStop"));
        isAlertShowing = savedInstanceState.getBoolean("isAlertShowing");
        isSoundPlaying = savedInstanceState.getBoolean("isSoundPlaying");
        isSetTimeDialog = savedInstanceState.getBoolean("isSetTimeDilog");
        if (isSetTimeDialog) {
            currTimePickerHour = savedInstanceState.getInt("currTimePickerHour");
            currTimePickerMin = savedInstanceState.getInt("currTimePickerMin");
            currTimePickerSec = savedInstanceState.getInt("currTimePickerSec");
        }
        Log.d(LOG_TAG, "TimerOnRestoreInstanceState");
    }

    // состояние Activity возобновлен
    protected void onResume() {
        super.onResume();
        if (isLaunchd)
            timer.start(); // если таймер был запущен до возобновления, запускаем его
        else
            timer.setText(MillisesondsToString(fullTimerTimeInMillis));

        if (isAlertShowing)
            alert.show();
        if (isSoundPlaying)
            timerSoundMP.start();

        if (isSetTimeDialog){
            mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(MyTimePicker view, int hourOfDay, int minute, int seconds) {
                    // TODO Auto-generated method stub
                    fullTimerTimeInMillis = hourOfDay*1000*3600 + minute*1000*60 + seconds*1000;
                    currTimerTimeInMillis = fullTimerTimeInMillis;
                    timer.setText(MillisesondsToString(fullTimerTimeInMillis));
                    isSetTimeDialog = false;
                }
            }, currTimePickerHour, currTimePickerMin, currTimePickerSec);
            mTimePicker.show();
        }
        Log.d(LOG_TAG, "TimerOnResume ");
    }

    // сохранение данных при перезапуске Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isAlertShowing", isAlertShowing);
        outState.putBoolean("isSoundPlaying", isSoundPlaying);
        outState.putBoolean("isSetTimeDilog", isSetTimeDialog);
        if (isSetTimeDialog){
            outState.putInt("currTimePickerHour", mTimePicker.getmInitialHourOfDay());
            outState.putInt("currTimePickerMin", mTimePicker.getmInitialMinute());
            outState.putInt("currTimePickerSec", mTimePicker.getmInitialSeconds());
        }
        outState.putLong("base", timer.getBase());
        outState.putLong("fullTimerTimeInMillis", fullTimerTimeInMillis);
        outState.putBoolean("isLaunchd", isLaunchd);
        outState.putInt("currentProgress", timerProgressBar.getProgress());
        outState.putLong("currTimerTimeInMillis", currTimerTimeInMillis);
        outState.putLong("elapsedTimeInMillis", elapsedTimeInMillis);
        outState.putBoolean("enBtnStart", timerBtnStart.isEnabled());
        outState.putBoolean("enBtnSetTime", timerBtnSetTime.isEnabled());
        outState.putBoolean("enBtnStop", timerBtnStop.isEnabled());
        outState.putCharSequence("tBtnStart", timerBtnStart.getText());
        Log.d(LOG_TAG, "TimerOnSaveInstanceState");
    }

    // состояние запуска Activity
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "TimerOnStart");
    }

    // состояние остановки работы Activity
    protected void onStop() {
        if (isSoundPlaying)
            timerSoundMP.stop();
        super.onStop();
        Log.d(LOG_TAG, "TimerOnStop");
    }

    // при нажатии кнопки старта таймера
    public void onTimerStartClick(View view) {
        timer.setBase(SystemClock.elapsedRealtime());   // сохраняем время начала работы
        timer.start();                                  // запускаем таймер
        isLaunchd = true;                               // таймер запущен
        // активность кнопок
        timerBtnStart.setEnabled(false);
        timerBtnSetTime.setEnabled(false);
        timerBtnStop.setEnabled(true);
        Toast.makeText(this, "Timer is started", Toast.LENGTH_SHORT).show(); // всплывающее сообщение
    }

    // при нажатии кнопки "задать"
    public void onTimerSetTimeClick (View view) {
        mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(MyTimePicker view, int hourOfDay, int minute, int seconds) {
                // TODO Auto-generated method stub
                fullTimerTimeInMillis = hourOfDay*1000*3600 + minute*1000*60 + seconds*1000;
                currTimerTimeInMillis = fullTimerTimeInMillis;
                timer.setText(MillisesondsToString(fullTimerTimeInMillis));
                isSetTimeDialog = false;
            }
        }, GetHours(fullTimerTimeInMillis), GetMinuts(fullTimerTimeInMillis), GetSeconds(fullTimerTimeInMillis));
        mTimePicker.show();
        isSetTimeDialog = true;
    }

    // при нажатии кнопки останова
    public void onTimerStopClick(View view) {
        timer.stop();       // останавливаем таймер
        isLaunchd = false; // таймер остановлен
        timer.setText(MillisesondsToString(fullTimerTimeInMillis)); // устанавливаем начальную строку таймера
        currTimerTimeInMillis = fullTimerTimeInMillis;
        timerProgressBar.setProgress(0);
        // устанавливаем активность кнопок
        timerBtnStart.setEnabled(true);
        timerBtnSetTime.setEnabled(true);
        timerBtnStop.setEnabled(false);
        Toast.makeText(this, "Timer is cancelled", Toast.LENGTH_SHORT).show(); // всплывающее сообщение
    }
}