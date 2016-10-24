package ru.spbstu.ptime;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;


public class ChronoActivity extends Activity {
    private Chronometer mChr;
    private Button btnStart;
    private Button btnStop;
    private Button btnReset;
    private boolean resume = false;
    private String currentTime = "";
    private long elapsedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrono);

        mChr = (Chronometer) findViewById(R.id.chronometer);

        Typeface dec = Typeface.createFromAsset(getAssets(), getString(R.string.digit_keyboard_font));
        mChr.setTypeface(dec);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnReset = (Button) findViewById(R.id.btnReset);

        mChr.setText(R.string.start_time);

        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnReset.setEnabled(false);

        mChr.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                long hour, min, sec, temp;
                if (!resume){
                    temp = SystemClock.elapsedRealtime() - mChr.getBase();
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
                    arg0.setText(currentTime);
                    elapsedTime = SystemClock.elapsedRealtime();
                }
                else{
                    temp = elapsedTime - mChr.getBase();
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
                    arg0.setText(currentTime);
                    elapsedTime = elapsedTime + 1000;
                }
            }
        });
    }

    public void onStartClick(View view) {
        if(!resume) mChr.setBase(SystemClock.elapsedRealtime());
        mChr.start();
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        btnReset.setEnabled(true);
        btnStart.setText("Запущен");
        btnReset.setText(R.string.reset);
        btnStop.setText(R.string.stop);
        Toast.makeText(this, "Секундомер запущен", Toast.LENGTH_SHORT).show();
    }

    public void onStopClick(View view) {
        resume = true;
        mChr.stop();
        mChr.setText(currentTime);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnReset.setEnabled(true);
        btnStop.setText("Остановлен");
        btnStart.setText(R.string.start);
        Toast.makeText(this, "Секундомер остановлен", Toast.LENGTH_SHORT).show();
    }

    public void onResetClick(View view) {
        mChr.stop();
        mChr.setText(R.string.start_time);
        resume = false;
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnReset.setEnabled(false);
        btnReset.setText("Сброшен");
        btnStop.setText(R.string.stop);
        btnStart.setText(R.string.start);
        Toast.makeText(this, "Секундомер сброшен", Toast.LENGTH_SHORT).show();
    }
}
