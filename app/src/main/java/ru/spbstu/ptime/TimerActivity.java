package ru.spbstu.ptime;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

public class TimerActivity extends Activity {

    private Button timerBtnStart, timerBtnSetTime, timerBtnStop;
    private Chronometer timer;
    private AlertDialog alert;

    int defaultColor;   // костыль для изменения цвета "00:00:00"

    private boolean isLaunchd = false;
    long elapsedTimeInMillis = 0;       // вспомогательная переменная для случая возобновления активити
    long currTimerTimeInMillis = 0;     // время, отображаемое на экране
    long fullTimerTimeInMillis = 6000;  // промежуток времени, заданный пользователем

    int i = 0;      // фича

    final String LOG_TAG = "TimerActivity";

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

        defaultColor = timer.getCurrentTextColor();     // костыль для изменения цвета "00:00:00"

        Chronometer.OnChronometerTickListener timerListner = MyTimer(timer);

        timerBtnStart.setEnabled(true);
        timerBtnSetTime.setEnabled(true);
        timerBtnStop.setEnabled(false);

        timer.setOnChronometerTickListener(timerListner);

        // создаем диалоговое окно, сообщающее о завершении таймера
        AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this);
        builder.setTitle("Таймер")
                .setMessage("Время истекло!")
                .setCancelable(false)
                .setNegativeButton("Ок",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
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
                elapsedTimeInMillis = SystemClock.elapsedRealtime();    //  запоминаем прошедшее время

                // если время истекло (следующий "тик" после 00:00:00)
                if (currTimerTimeInMillis < 0)
                {
                    timer.stop();                       // останавливаем таймер
                    isLaunchd = false;                  // таймер остановлен
                    timer.setTextColor(defaultColor);   // возвращаем цвет цифр по умолчанию
                    timer.setText(MillisesondsToString(fullTimerTimeInMillis)); // устанавливаем начальную строку таймера
                    currTimerTimeInMillis = fullTimerTimeInMillis;
                }
                // если на этот "тик" таймер досшел до 00:00:00
                else if (currTimerTimeInMillis < 1000)
                {
                    timer.setTextColor(getResources().getColor(R.color.colorAccent));       // выделяем 00:00:00 цветом
                    alert.show();   // отображаем диалоговое окно
                    timerBtnStart.setEnabled(true);
                    timerBtnSetTime.setEnabled(true);
                    timerBtnStop.setEnabled(false);
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
        isLaunchd = savedInstanceState.getBoolean("isLaunchd");
        currTimerTimeInMillis = savedInstanceState.getLong("currTimerTimeInMillis");
        elapsedTimeInMillis = savedInstanceState.getLong("elapsedTimeInMillis");
        timerBtnStart.setEnabled(savedInstanceState.getBoolean("enBtnStart"));
        timerBtnSetTime.setEnabled(savedInstanceState.getBoolean("enBtnSetTime"));
        timerBtnStop.setEnabled(savedInstanceState.getBoolean("enBtnStop"));
        Log.d(LOG_TAG, "TimerOnRestoreInstanceState");
    }

    // состояние Activity возобновлен
    protected void onResume() {
        super.onResume();
        if (isLaunchd)
            timer.start(); // если таймер был запущен до возобновления, запускаем его
        else
            timer.setText(MillisesondsToString(fullTimerTimeInMillis));
        Log.d(LOG_TAG, "TimerOnResume ");
    }

    // сохранение данных при перезапуске Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("base", timer.getBase());
        outState.putBoolean("isLaunchd", isLaunchd);
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
        Toast.makeText(this, "Таймер запущен", Toast.LENGTH_SHORT).show(); // всплывающее сообщение
    }

    // при нажатии кнопки "задать"
    public void onTimerSetTimeClick (View view) {
        String lol[] = {"Фигушки!", "Лол нет", "НЕЗЯ"};
        Toast.makeText(this, lol[i], Toast.LENGTH_SHORT).show(); // всплывающее сообщение
        i = (i + 1) % 3;
    }

    // при нажатии кнопки останова
    public void onTimerStopClick(View view) {
        timer.stop();       // останавливаем таймер
        isLaunchd = false; // таймер остановлен
        timer.setText(MillisesondsToString(fullTimerTimeInMillis)); // устанавливаем начальную строку таймера
        currTimerTimeInMillis = fullTimerTimeInMillis;
        // устанавливаем активность кнопок
        timerBtnStart.setEnabled(true);
        timerBtnSetTime.setEnabled(true);
        timerBtnStop.setEnabled(false);
        Toast.makeText(this, "Таймер остановлен", Toast.LENGTH_SHORT).show(); // всплывающее сообщение
    }
}