package pt.uma.cfm.simpleplayer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton _bPlay, _bBack, bFowa;
    private VideoView _video;
    private EditText _URL;
    private String _SURL;
    private SeekBar _timeBar;
    //private boolean _isPaused = true;
    private int _maxTime = 0;
    private Timer _timer;
    private TextView _title;
    private String defaultURL = "https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
    //private String defaultURL = "https://r6---sn-pouxjivoapox-cvhl.googlevideo.com/videoplayback?ms=au%2Crdu&fvip=6&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cexpire&mt=1525961085&mv=m&mime=video%2Fmp4&id=o-ADLGjRZK4KSHfAl4eTLpu-iMCtb2aIWE64eD6hqx0FRq&pl=24&gir=yes&key=yt6&ip=103.42.162.50&mn=sn-pouxjivoapox-cvhl%2Csn-cvh7knez&ipbits=0&mm=31%2C29&c=WEB&ratebypass=yes&lmt=1457644399034408&source=youtube&initcwndbps=688750&dur=58.374&clen=2624875&expire=1525982797&ei=7VH0WpnpLeWVz7sP_ISxgAM&itag=18&signature=641851770CA988F2164873F0253D98AAF45E6A0C.4CBD38B6C88ED879E0BDA0A03E3346A755AA5AC4&requiressl=yes&video_id=xqOFl93sHno&title=Batman+lesson+-+dont+give+the+Joker+a+glass+of+water";
    //private String defaultURL = "http://srv4.youtubemp3.to/download.php?output=MjM4MDU2NTcvMTUyNTk2MzM0NQ==";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //_timer = new Timer();
        /*_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 1000);*/


        //setBarListener(_video,_timeBar);
        //_title = findViewById(R.id.eventName);
        //_title.setText(_video.getLabelFor()+"");

        setConstants();
        _timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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
                }

            }
        });
    }

    public void setConstants(){
        _bPlay = findViewById(R.id.buttonPlay);
        _video =  findViewById(R.id.videoView);
        _timeBar =  findViewById(R.id.timeBar);
        _URL = findViewById(R.id.editText);
        _URL.setText(defaultURL);

        _video.setVideoPath(defaultURL);
        //int time = _video.getDuration();
        //Log.d("Duração",time +"");
        _timeBar.setMax(_video.getDuration());


    }

    public void changeVideo(View v){
        _video.setVideoPath(_URL.getText().toString());
        _video.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Do stuff here
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final int rotation = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
//            case Surface.ROTATION_0:
//                return "portrait";
            case Surface.ROTATION_90:

                _video.setLayoutParams(lp);
                break;
            case Surface.ROTATION_270:
                //WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                _video.setLayoutParams(lp);
                break;
        }
    }

    public void onClickButton(View v){
        Log.d("Teste","Funcionou");
        //Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.test);
        //Uri uri = Uri.parse("https://youtu.be/IdoD2147Fik");
        //_video.setVideoURI(uri);
        //_video.setVideoPath("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");

        //_video.setVideoPath(_URL.getText().toString());

        _timeBar.setProgress(_video.getCurrentPosition());

        if(!_video.isPlaying()) {

            _video.start();

            DefineTimer();
            Log.d("Duração",_maxTime+"");
            _timeBar.setMax(_video.getDuration());
            _bPlay.setImageResource(android.R.drawable.ic_media_pause);
            //_isPaused = false;

        }

        else{
            _video.pause();
            _bPlay.setImageResource(android.R.drawable.ic_media_play);
            //_isPaused = true;
            _timer.cancel();

        }

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
        //_timeBar.setProgress(_video.getCurrentPosition());

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        _maxTime = _video.getDuration();
        _timeBar.setMax(_video.getDuration());

        this.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            _timeBar.setProgress(_video.getCurrentPosition());
            Log.d("Posição Atual",_video.getCurrentPosition()+"");
            //This method runs in the same thread as the UI.

            //Do something to the UI thread here

        }
    };




}
