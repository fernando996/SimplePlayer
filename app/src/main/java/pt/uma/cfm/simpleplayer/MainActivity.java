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

public class MainActivity extends AppCompatActivity {

    private Button _bPlay, _bBack, bFowa;
    private VideoView _video;
    private EditText _URL;
    private String _SURL;
    private ProgressBar _timeBar;
    private boolean isPaused = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setConstants();
    }

    public void setConstants(){
        _bPlay = findViewById(R.id.buttonPlay);
        _video =  findViewById(R.id.videoView);
        _timeBar =  findViewById(R.id.timeBar);

        _video.setVideoPath("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        Log.d("Duração",_video.getDuration()+"");
        _timeBar.setMax(_video.getDuration());
        //_video.setVideoPath("https://youtu.be/IdoD2147Fik.mp4");
    }

    public void onClickButton(View v){
        Log.d("Teste","Funcionou");
        //Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.test);
        Uri uri = Uri.parse("https://youtu.be/IdoD2147Fik");
        //_video.setVideoURI(uri);
        //_video.setVideoPath("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        //_video.setVideoPath("https://cdn14.tubeload.tv/mp3/Wake_Me_Up_Jake_Donaldson_Avicii_Tribute_.mp3");
        //_video.setVideoPath("https://youtu.be/IdoD2147Fik");

        _timeBar.setProgress(_video.getCurrentPosition());
        if(isPaused) {
            _video.start();
            _bPlay.setText("Pause");
            isPaused = false;
        }
        else{
            _video.pause();
            _bPlay.setText("Play");
            isPaused = true;
        }

    }
}
