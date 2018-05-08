package pt.uma.cfm.simpleplayer;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button _bPlay, _bBack, bFowa;
    private VideoView _video;
    private EditText _URL;
    private String _SURL;
    private ProgressBar _timeBar;
    private boolean _isPaused = true;
    private int _maxTime = 0;
    private Timer _timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //_timer = new Timer();
        /*_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 1000);*/



        setConstants();
    }

    public void setConstants(){
        _bPlay = findViewById(R.id.buttonPlay);
        _video =  findViewById(R.id.videoView);
        _timeBar =  findViewById(R.id.timeBar);

        _video.setVideoPath("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        //int time = _video.getDuration();
        //Log.d("Duração",time +"");
        _timeBar.setMax(_video.getDuration());


    }

    public void onClickButton(View v){
        Log.d("Teste","Funcionou");
        //Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.test);
        Uri uri = Uri.parse("https://youtu.be/IdoD2147Fik");
        //_video.setVideoURI(uri);
        //_video.setVideoPath("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");



        _timeBar.setProgress(_video.getCurrentPosition());

        if(_isPaused) {
            DefineTimer();
            _video.start();
            _maxTime = _video.getDuration();
            Log.d("Duração",_maxTime+"");
            _timeBar.setMax(_video.getDuration());
            _bPlay.setText("Pause");
            _isPaused = false;

        }
        else{
            _video.pause();
            _bPlay.setText("Play");
            _isPaused = true;
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

        }, 0, 500);
    }



    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.
        //_timeBar.setProgress(_video.getCurrentPosition());

        //We call the method that will work with the UI
        //through the runOnUiThread method.
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
