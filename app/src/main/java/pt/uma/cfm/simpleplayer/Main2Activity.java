package pt.uma.cfm.simpleplayer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity implements SensorEventListener {

    private FloatingActionButton _bPlay;
    private VideoView _video;
    private EditText _URL;
    private SeekBar _timeBar;
    private int _maxTime = 0;
    private Timer _timer;
    private TextView _title;

    //private String defaultURL = "https://img-9gag-fun.9cache.com/photo/agXODGv_460svvp9.webm";
    private String defaultURL = "https://img-9gag-fun.9cache.com/photo/aeMO8Zv_460svvp9.webm";

    private SensorManager sensorManager;
    Sensor accelerometer;
    float xValue, yValue, zValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setConstants();
        setBarListener(_timeBar);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(Main2Activity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        xValue = sensorEvent.values[0];
        yValue = sensorEvent.values[1];
        zValue = sensorEvent.values[2];
        Log.d("X", xValue + "");
        Log.d("Y", yValue + "");
        Log.d("Z", zValue + "");
        //_video.pause();

        if(yValue > 0 && xValue > 3 && zValue < 3 && zValue > -3){
            Log.d("Action", "Rewind");
            _video.pause();
            _video.seekTo(_video.getCurrentPosition() - 2000);
            //_video.start();
        }
        else if(yValue > 0 && xValue < -3 && zValue < 3 && zValue > -3){
            Log.d("Action", "Advance");
            _video.pause();
            _video.seekTo(_video.getCurrentPosition() + 2000);
            //_video.start();
        }
        else if(yValue > 0 && zValue < -3 && xValue < 3 && xValue > -3 && _video.isPlaying()){
            Log.d("Action", "Pause");
            _video.pause();
        }
        else if(yValue > 0 && zValue > 3 && xValue < 3 && xValue > -3 && !_video.isPlaying()){
            Log.d("Action", "Play");
            _video.start();
        }
        //_video.start();
    }

    private void setBarListener(SeekBar bar){
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                if(fromUser) {

                    _video.seekTo(progress);

                    Log.d("Progresso",progress+"");
                    Log.d("Buffer Atual",_video.getBufferPercentage()+"");
                    Log.d("Max",_video.getDuration()+"");
                }



            }
        });
    }

    private void setConstants(){
        //_bPlay = findViewById(R.id.buttonPlay);
        _video =  findViewById(R.id.videoView2);
        _timeBar =  findViewById(R.id.timeBar2);
        _URL = findViewById(R.id.editText2);
        _title = findViewById(R.id.eventName2);

        _URL.setText(defaultURL);
        _video.setVideoPath(defaultURL);
        _timeBar.setMax(_video.getDuration());
        DefineTimer();
        //_video.start();

    }

    public void changeVideo(View v){
        _video.setVideoPath(_URL.getText().toString());
        _timeBar.setMax(_video.getDuration());
        //_bPlay.setImageResource(android.R.drawable.ic_media_pause);
        DefineTimer();
        _video.start();
    }

    /**
     * Esta função define um temporizador sempre igual para que se possa utuilizar na
     * barra de progresso.
     * Através desta função é possivel iniciar o temporizador logo apos termos cancelado o
     * mesmo.
     */
    private void DefineTimer(){
        _timer = new Timer();
        _timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 100);
    }

    private void TimerMethod(){
        //This method is called directly by the timer
        //and runs in the same thread as the timer.
        //_timeBar.setProgress(_video.getCurrentPosition());

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        _maxTime = _video.getDuration();
        _timeBar.setMax(_video.getDuration());

        this.runOnUiThread(Timer_Tick);
    }

    private String progressBarVideoDuration(int _videoProgress, int _videoDuration){

        String aVideoProgress = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(_videoProgress),
                TimeUnit.MILLISECONDS.toSeconds(_videoProgress) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(_videoProgress))
        );

        String aVideoDuration = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(_videoDuration),
                TimeUnit.MILLISECONDS.toSeconds(_videoDuration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(_videoDuration))
        );

        return aVideoProgress + " / " + aVideoDuration;
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            _timeBar.setProgress(_video.getCurrentPosition());
            Log.d("Posição Atual",_video.getCurrentPosition()+"");
            _title.setText(progressBarVideoDuration(_video.getCurrentPosition(),_video.getDuration()));
            //This method runs in the same thread as the UI.
            //Do something to the UI thread here

        }
    };
}
