package pt.uma.cfm.simpleplayer;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity implements SensorEventListener {

    private FloatingActionButton _bPlay, _bBack, _bFor;

    private EditText _URL;
    private int _SURL;
    private SeekBar _timeBar;
    private int _maxTime = 0;
    private Timer _timer;
    private TextView _title;
    private Switch _sensorSwitch;
    private Boolean _bSeek = false;
    private int _pSeek = 0;
    private ProgressBar _progBar;

    private SurfaceView surfaceView;
    Uri targetUri;

    private MediaPlayer _video = null;

    private SensorManager sensorManager;
    //private String defaultURL = "https://img-9gag-fun.9cache.com/photo/agXODGv_460svvp9.webm";
    private String defaultURL = "https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";



    Sensor accelerometer;
    float xValue, yValue, zValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        setConstants();

        setBarListener(_timeBar);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(Main2Activity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.unregisterListener(Main2Activity.this);

    }

    public void setConstants(){
        _bPlay = findViewById(R.id.buttonPlay);
        _bBack = findViewById(R.id.buttonBackward);
        _bFor = findViewById(R.id.buttonForward);

        _timeBar =  findViewById(R.id.timeBar);
        _URL = findViewById(R.id.editText);
        _title = findViewById(R.id.eventName);
        _sensorSwitch = findViewById(R.id.sensorSwitch);
        _progBar = findViewById(R.id.progressBar);

        _URL.setText(defaultURL);

    }

    /*
     * Sensores
     * */
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
            _bPlay.setImageResource(android.R.drawable.ic_media_play);
        }
        else if(yValue > 0 && zValue > 3 && xValue < 3 && xValue > -3 && !_video.isPlaying()){
            Log.d("Action", "Play");
            _video.start();
            _bPlay.setImageResource(android.R.drawable.ic_media_pause);
        }
        //_video.start();
    }

    public void changeVideo(View v){
        targetUri = Uri.parse(defaultURL);

        try{
            if (_video != null) {
                _video.reset();
                _video.release();
                _timer.cancel();
            }

            getWindow().setFormat(PixelFormat.UNKNOWN);
            _video = MediaPlayer.create(this, targetUri);

            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            _video.setDisplay(surfaceHolder);
            _video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    DefineTimer();
                    _timeBar.setMax(mp.getDuration());
                }
            });
            _video.prepareAsync();

        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Define o que acontece quando a SeekBar é alterada.
     * Quando o utilizador mexe na barra guardamos o tempo e depois
     * definimos o tempo a ser tratado pela tarefa que espera que o tempo
     * chegue ao pretendido.
     * */
    public void setBarListener(SeekBar bar){
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int time;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                _video.seekTo(time);
                _pSeek = time;
                _bSeek = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                if(fromUser) {
                    time = progress;
                }
            }
        });
    }


    public void onClickButtonPlay(View v)
    {
        targetUri = Uri.parse(defaultURL);

        try{
            if (_video == null) {


                getWindow().setFormat(PixelFormat.UNKNOWN);
                _video = MediaPlayer.create(this, targetUri);

                SurfaceHolder surfaceHolder = surfaceView.getHolder();
                _video.setDisplay(surfaceHolder);
                _video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        DefineTimer();
                        _timeBar.setMax(mp.getDuration());
                    }
                });
                _video.prepareAsync();
            }

        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if(!_video.isPlaying()) {
            _video.start();
            _bPlay.setImageResource(android.R.drawable.ic_media_pause);
        }
        else
        {
            _video.pause();
            _bPlay.setImageResource(android.R.drawable.ic_media_play);
        }
    }


    public void onClickButtonBackward(View v)
    {
        int position;
        position = _video.getCurrentPosition() - 5000;
        //if(_video.canSeekBackward()){
        //if(position>0){
        _timeBar.setProgress(position);
        _video.seekTo(position);
        //}
    }

    public void onClickButtonForward(View v)
    {
        int position;
        /*if(_video.canSeekBackward()){
            position = _video.getCurrentPosition() + 5000;

            _timeBar.setProgress(position);
            _video.seekTo(position);
        }*/
        position = _video.getCurrentPosition() + 5000;

        _timeBar.setProgress(position);
        _video.seekTo(position);

    }

    public void onClickCheck(View v){

        if (_sensorSwitch.isChecked())
        {
            sensorManager.registerListener(Main2Activity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            sensorManager.unregisterListener(Main2Activity.this);
        }

    }


    /*
     * Função que atualiza os valores do progresso do video, em minutos.
     */
    public String progressBarVideoDuration(int _videoProgress, int _videoDuration)
    {

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

    /**
     * Esta função define um temporizador sempre igual para que se possa utuilizar na
     * barra de progresso.
     * Através desta função é possivel iniciar o temporizador logo apos termos cancelado o
     * mesmo.
     */
    private void DefineTimer()
    {
        _timer = new Timer();
        _timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 100);
    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
        this.runOnUiThread(seek);
    }


    private Runnable seek = new Runnable() {
        @Override
        public void run() {
            if (_bSeek){
                //Log.d("_bseek", _bSeek+"");
                if (_video.getCurrentPosition()<=_pSeek){
                    //Log.d("Tempo menor que Seek", _video.getCurrentPosition()+"");
                    _video.setVolume(0, 0);
                    _timeBar.setProgress(_pSeek);
                    _title.setText(progressBarVideoDuration(_pSeek,_video.getDuration()));
                    surfaceView.setVisibility(View.GONE);
                    _progBar.setVisibility(View.VISIBLE);

                    //_title.setVisibility(View.INVISIBLE);
                }
                else {
                    _video.setVolume(1,1);

                    surfaceView.setVisibility(View.VISIBLE);
                    _progBar.setVisibility(View.GONE);
                    SurfaceHolder surfaceHolder = surfaceView.getHolder();
                    _video.setDisplay(surfaceHolder);

                    _timeBar.setProgress(_video.getCurrentPosition());
                    _bSeek = false;
                    //_title.setVisibility(View.VISIBLE);

                }
            }
        }
    };

    private Runnable Timer_Tick = new Runnable()
    {
        public void run() {
            _timeBar.setProgress(_video.getCurrentPosition());
            _title.setText(progressBarVideoDuration(_video.getCurrentPosition(),_video.getDuration()));

            //This method runs in the same thread as the UI.
            //Do something to the UI thread here
        }
    };



}
