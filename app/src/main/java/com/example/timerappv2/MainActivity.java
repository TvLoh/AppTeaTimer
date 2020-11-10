package com.example.timerappv2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, TextView.OnEditorActionListener {

    SeekBar time_set;
    TextView show_time;
    Button start_stop;
    Button timer_01;
    Button timer_02;
    Button timer_03;
    Button timer_04;
    EditText set_min;
    EditText set_sec;
    Chronometer Chrono;
    LinearLayout set_time;

    boolean run_timer;
    boolean change_style;
    long time_left;
    long time_preeset_1;
    long time_preeset_2;
    long time_preeset_3;
    long time_preeset_4;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SP_TIME01 = "sp_timer01";
    public static final String SP_TIME02 = "sp_timer02";
    public static final String SP_TIME03 = "sp_timer03";
    public static final String SP_TIME04 = "sp_timer04";

    static CountDownTimer countdown = null;
    MediaPlayer playSound;
    AudioManager audioManager;
    Vibrator vibrator;

    public void counter(long start){
        Log.i("Counter: ","Start");

        countdown = new CountDownTimer((start*1000+500),1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                run_timer = true;
                time_left = millisUntilFinished/1000;
                build_page(time_left, true);
            }
            @Override
            public void onFinish() {
                Log.i("Counter: ","finished");
                Chrono.setVisibility(View.VISIBLE);
                show_time.setVisibility(View.GONE);
                Chrono.setBase(SystemClock.elapsedRealtime());
                Chrono.start();
                build_page(00,true);
                vibration();
                sound();
            }
        }.start();
    }

    public void build_page(long time_sec, boolean status){
        Log.i("BUILD PAGE","time_sec: "+Long.toString(time_sec)+"; status: "+Boolean.toString(status));
        show_time.setText(Long.toString(time_sec/60)+":"+(time_sec%60<10?"0"+Long.toString(time_sec%60):Long.toString(time_sec%60)));
        if(status == true){
            Log.i("BUILD PAGE", "true");
            timer_01.setEnabled(false);
            timer_02.setEnabled(false);
            timer_03.setEnabled(false);
            timer_04.setEnabled(false);
            time_set.setEnabled(false);
            start_stop.setText("stop");
            start_stop.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            start_stop.setHighlightColor(getResources().getColor(android.R.color.holo_red_light));

        } else{
            Log.i("BUILD PAGE", "false");
            timer_01.setEnabled(true);
            timer_02.setEnabled(true);
            timer_03.setEnabled(true);
            timer_04.setEnabled(true);
            time_set.setEnabled(true);
            start_stop.setText("start");
            start_stop.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            start_stop.setHighlightColor(getResources().getColor(android.R.color.holo_green_dark));
        }
    }

    public  void vibration(){
        long[] pattern = {0, 1000,1000,1000,1000,1500,2000,1500,500,500,500,500,500,500};
        try {
            vibrator.vibrate(pattern,-1);
        }catch (Exception e){ }

    }

    public  void  sound(){
        playSound = MediaPlayer.create(this, R.raw.be);
        final int vol = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        playSound.setVolume(vol,vol);
        try {
            playSound.start();
            playSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                int i = 5;
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(i >= 1 && run_timer == true){
                        i--;
                        playSound.start();
                    }
                }
            });

        }catch (Exception e){}
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        time_set    = (SeekBar)findViewById(R.id.sb_set_time);
        show_time   = (TextView)findViewById(R.id.tv_show_timer);
        set_min     = (EditText)findViewById(R.id.et_set_minuets);
        set_sec     = (EditText)findViewById(R.id.et_set_sec);
        start_stop  = (Button)findViewById(R.id.btn_start_stop);
        timer_01    = (Button)findViewById(R.id.btn_timer_01);
        timer_02    = (Button)findViewById(R.id.btn_timer_02);
        timer_03    = (Button)findViewById(R.id.btn_timer_03);
        timer_04    = (Button)findViewById(R.id.btn_timer_04);
        set_time    = (LinearLayout)findViewById(R.id.ll_set_time);
        Chrono      = (Chronometer)findViewById(R.id.Chrono);

        start_stop.setOnClickListener(this);
        timer_01.setOnClickListener(this);
        timer_02.setOnClickListener(this);
        timer_03.setOnClickListener(this);
        timer_04.setOnClickListener(this);
        timer_01.setOnLongClickListener(this);
        timer_02.setOnLongClickListener(this);
        timer_03.setOnLongClickListener(this);
        timer_04.setOnLongClickListener(this);
        time_set.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Button: ", "SeekBar");
                build_page(time_set.getProgress(),false);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        set_min.setOnEditorActionListener(this);
        set_sec.setOnEditorActionListener(this);

        time_set.setMin(1);
        set_time.setVisibility(View.GONE);
        Chrono.setVisibility(View.GONE);

        loadDate();
        timer_01.setText(Long.toString(time_preeset_1/60)+":"+(time_preeset_1%60>10?Long.toString(time_preeset_1%60):"0"+Long.toString(time_preeset_1%60)));
        timer_02.setText(Long.toString(time_preeset_2/60)+":"+(time_preeset_2%60>10?Long.toString(time_preeset_2%60):"0"+Long.toString(time_preeset_2%60)));
        timer_03.setText(Long.toString(time_preeset_3/60)+":"+(time_preeset_3%60>10?Long.toString(time_preeset_3%60):"0"+Long.toString(time_preeset_3%60)));
        timer_04.setText(Long.toString(time_preeset_4/60)+":"+(time_preeset_4%60>10?Long.toString(time_preeset_4%60):"0"+Long.toString(time_preeset_4%60)));

        if(run_timer){
            build_page(time_left, run_timer);
        }else{
            build_page(time_set.getProgress(), run_timer);
        }   //check if timer is running

        if (savedInstanceState != null)
        {
            run_timer = savedInstanceState.getBoolean("runstate");
            time_left = savedInstanceState.getLong("timeleft");
            if(run_timer)
            {
                counter(time_left);
            }
            Log.i("onCreate: ","time_left: "+Long.toString(time_left));
            Log.i("onCreate: ","run_time: "+Boolean.toString(run_timer));
            Log.i("onCreate: ","change_style: "+Boolean.toString(change_style));
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Livecycle","onSaveInstanceState");
        outState.putBoolean("runstate",run_timer);
        outState.putLong("timeleft", time_left);
        countdown.cancel();
        Log.i("onSaveInstance: ","time_left: "+Long.toString(time_left));
        Log.i("onSaveInstance: ","run_time: "+Boolean.toString(run_timer));
    }

    public void saveData(){
        SharedPreferences shared_Prevs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_Prevs.edit();
        editor.putLong(SP_TIME01, time_preeset_1);
        editor.putLong(SP_TIME02, time_preeset_2);
        editor.putLong(SP_TIME03, time_preeset_3);
        editor.putLong(SP_TIME04, time_preeset_4);
        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT);
    }

    public void loadDate(){
        SharedPreferences shared_prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        time_preeset_1 = shared_prefs.getLong(SP_TIME01, 5*60);
        time_preeset_2 = shared_prefs.getLong(SP_TIME02, 8*60);
        time_preeset_3 = shared_prefs.getLong(SP_TIME03, 20*60);
        time_preeset_4 = shared_prefs.getLong(SP_TIME04, 45*60);
    }

    @Override
    public void onClick(View viewID) {
        switch (viewID.getId()) {
            case R.id.btn_start_stop:
                Log.i("Button: ", "start_stop");
                if (run_timer){
                    Log.i("Counter: ","deleted");
                    countdown.cancel();
                    vibrator.cancel();
                    run_timer = false;
                    build_page(time_set.getProgress(),run_timer);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    Chrono.stop();
                    show_time.setVisibility(View.VISIBLE);
                    Chrono.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }else{
                    counter(time_set.getProgress());
                }
                break;
            case R.id.btn_timer_01:
                Log.i("Button: ", "Timer 1");
                counter(time_preeset_1);
                break;
            case R.id.btn_timer_02:
                Log.i("Button: ", "Timer 2");
                counter(time_preeset_2);
                break;
            case R.id.btn_timer_03:
                Log.i("Button: ", "Timer 3");
                counter(time_preeset_3);
                break;
            case R.id.btn_timer_04:
                Log.i("Button: ", "Timer 4");
                counter(time_preeset_4);
                break;
            case R.id.et_set_minuets:
                set_min.setText("");
                break;
            case R.id.et_set_sec:
                set_sec.setText("");
                break;
        }
    }

    @Override
    public boolean onLongClick(View viewID) {
        switch (viewID.getId()) {
            case R.id.btn_timer_01:
                timer_02.setVisibility(View.INVISIBLE);
                timer_03.setVisibility(View.INVISIBLE);
                timer_04.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_timer_02:
                timer_01.setVisibility(View.INVISIBLE);
                timer_03.setVisibility(View.INVISIBLE);
                timer_04.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_timer_03:
                timer_02.setVisibility(View.INVISIBLE);
                timer_01.setVisibility(View.INVISIBLE);
                timer_04.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_timer_04:
                timer_02.setVisibility(View.INVISIBLE);
                timer_03.setVisibility(View.INVISIBLE);
                timer_01.setVisibility(View.INVISIBLE);
                break;
        }
        set_time.setVisibility(View.VISIBLE);
        start_stop.setVisibility(View.INVISIBLE);
        show_time.setVisibility(View.GONE);
        time_set.setVisibility(View.INVISIBLE);
        time_set.setActivated(true);
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId){
            case EditorInfo.IME_ACTION_NEXT:
                set_sec.setText("");
                break;
            case EditorInfo.IME_ACTION_DONE:
                if(timer_01.getVisibility() == View.VISIBLE){
                    timer_01.setText(set_min.getText()+":"+ (Integer.parseInt(set_sec.getText().toString()) < 10 ? ("0"+set_sec.getText()) : set_sec.getText()));
                    time_preeset_1 = (60*Long.parseLong(set_min.getText().toString())+Long.parseLong(set_sec.getText().toString()));
                }else if(timer_02.getVisibility() == View.VISIBLE){
                    timer_02.setText(set_min.getText()+":"+ (Integer.parseInt(set_sec.getText().toString()) < 10 ? ("0"+set_sec.getText()) : set_sec.getText()));
                    time_preeset_2 = (60*Long.parseLong(set_min.getText().toString())+Long.parseLong(set_sec.getText().toString()));
                }else if(timer_03.getVisibility() == View.VISIBLE){
                    timer_03.setText(set_min.getText()+":"+ (Integer.parseInt(set_sec.getText().toString()) < 10 ? ("0"+set_sec.getText()) : set_sec.getText()));
                    time_preeset_3 = (60*Long.parseLong(set_min.getText().toString())+Long.parseLong(set_sec.getText().toString()));
                }else if(timer_04.getVisibility() == View.VISIBLE){
                    timer_04.setText(set_min.getText()+":"+ (Integer.parseInt(set_sec.getText().toString()) < 10 ? ("0"+set_sec.getText()) : set_sec.getText()));
                    time_preeset_4 = (60*Long.parseLong(set_min.getText().toString())+Long.parseLong(set_sec.getText().toString()));
                }
                timer_01.setVisibility(View.VISIBLE);
                timer_02.setVisibility(View.VISIBLE);
                timer_03.setVisibility(View.VISIBLE);
                timer_04.setVisibility(View.VISIBLE);
                time_set.setVisibility(View.VISIBLE);
                start_stop.setVisibility(View.VISIBLE);
                show_time.setVisibility(View.VISIBLE);
                set_time.setVisibility(View.GONE);
                saveData();
                break;
        }
        return false;
    }
/*
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("livesycle","onRestoreInstanceState");
        run_timer = savedInstanceState.getBoolean("runstate");
        time_left = savedInstanceState.getLong("timeleft");
        change_style = true;
        //build_page(time_left,run_timer);
        Log.i("onRestoreInstance: ","time_left: "+Long.toString(time_left));
        Log.i("onRestoreInstance: ","run_time: "+Boolean.toString(run_timer));
        Log.i("onRestoreInstance: ","change_style: "+Boolean.toString(change_style));
    }
 */
}
