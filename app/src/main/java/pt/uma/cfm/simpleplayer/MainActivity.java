package pt.uma.cfm.simpleplayer;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private FloatingActionButton _bPlay, _bBack, _bFor;
    private VideoView _video;
    //private EditText _URL;
    private SeekBar _timeBar;
    private int _maxTime = 0;
    private Timer _timer;
    private TextView _title;
    private Switch _sensorSwitch;
    //private String _defaultURL = "https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
    //private String _defaultURL = "https://r6---sn-pouxjivoapox-cvhl.googlevideo.com/videoplayback?ms=au%2Crdu&fvip=6&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cexpire&mt=1525961085&mv=m&mime=video%2Fmp4&id=o-ADLGjRZK4KSHfAl4eTLpu-iMCtb2aIWE64eD6hqx0FRq&pl=24&gir=yes&key=yt6&ip=103.42.162.50&mn=sn-pouxjivoapox-cvhl%2Csn-cvh7knez&ipbits=0&mm=31%2C29&c=WEB&ratebypass=yes&lmt=1457644399034408&source=youtube&initcwndbps=688750&dur=58.374&clen=2624875&expire=1525982797&ei=7VH0WpnpLeWVz7sP_ISxgAM&itag=18&signature=641851770CA988F2164873F0253D98AAF45E6A0C.4CBD38B6C88ED879E0BDA0A03E3346A755AA5AC4&requiressl=yes&video_id=xqOFl93sHno&title=Batman+lesson+-+dont+give+the+Joker+a+glass+of+water";
    //private String _defaultURL = "http://srv4.youtubemp3.to/download.php?output=MjM4MDU2NTcvMTUyNTk2MzM0NQ==";

    private String _defaultURL = "https://img-9gag-fun.9cache.com/photo/aeMO8Zv_460svvp9.webm";

    private SensorManager sensorManager;
    Sensor accelerometer;
    float xValue, yValue, zValue;

    private int _saveProgress;
    private ArrayList<String> _playList;
    private int _playListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar((android.support.v7.widget.Toolbar) toolbar);



        setPlayList();
        setConstants();
        setBarListener(_timeBar);
        setSensors();
        //Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        sensorManager.unregisterListener(MainActivity.this);
        _video.setMediaController(null);
        setFinishListener(_video);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            addVideo();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        _video.pause();
        _video.seekTo(_saveProgress);
        _bPlay.setImageResource(android.R.drawable.ic_media_play);

    }

    @Override
    public void onPause(){
        super.onPause();
        // put your code here...
        _saveProgress = _video.getCurrentPosition();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void setSensors(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setPlayList(){
        _playListPosition = 0;
        _playList = new ArrayList<>();
        //_playList.add(_defaultURL);
        //_playList.add("https://img-9gag-fun.9cache.com/photo/anM2ZQn_460svvp9.webm");
        _playList.add("https://img-9gag-fun.9cache.com/photo/am7xVLv_460svvp9.webm");
        _playList.add("https://img-9gag-fun.9cache.com/photo/aOrZEor_460svvp9.webm");
        _playList.add("https://img-9gag-fun.9cache.com/photo/a47Ag16_460svvp9.webm");

    }

    private void setFinishListener(final VideoView video){
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //video.seekTo(0);
                Log.d("Posição na lista antes da transição", _playListPosition+"");
                _playListPosition++;
                Log.d("Posição na lista após a transição", _playListPosition+"");
                if(_playListPosition == _playList.size()){
                    _playListPosition = 0;
                }

                video.setVideoPath(_playList.get(_playListPosition)/* % (_playList.size() - 1))*/);
                _bPlay.setImageResource(android.R.drawable.ic_media_play);
            }
        });
    }

    public void addVideo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insert the video's URL:");
        if(_video.isPlaying()){
            _video.pause();
            _bPlay.setImageResource(android.R.drawable.ic_media_play);
        }

// Set up the input
        final EditText input = new EditText(this);
        //input.setBackgroundColor(R.color.colorPrimary);
        //input.setLayerPaint();

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT /*| InputType.TYPE_TEXT_VARIATION_PASSWORD*/);
        input.setText(_defaultURL);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*_title.setText(input.getText().toString());
                Log.d("URL", input.getText().toString());
                Log.d("Input", _title.getText().toString());*/

                _playList.add(input.getText().toString());
                Log.d("Posição na lista após adicionar", _playListPosition+"");
                //_playListPosition++;
                //_playListPosition = _playListPosition.
                //_video.setVideoPath(_playList);
                _bPlay.setImageResource(android.R.drawable.ic_media_pause);
                _video.start();
                DefineTimer();
                Context context = getApplicationContext();
                CharSequence text = "The video has been added to the list! Enjoy!";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                LinearLayout layout = (LinearLayout) toast.getView();
                if (layout.getChildCount() > 0) {
                    TextView tv = (TextView) layout.getChildAt(0);
                    tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                }
                toast.show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //_video.start();
                _bPlay.setImageResource(android.R.drawable.ic_media_pause);
                _video.start();
                DefineTimer();
                dialog.cancel();
            }
        });

        builder.show();
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

        if(yValue > 0 && xValue > 5 && _video.canSeekBackward()/*&& zValue < 3 && zValue > -3*/){
            Log.d("Action", "Rewind");
            _video.pause();
            _video.seekTo(_video.getCurrentPosition() - 2000);
            //_video.start();
        }
        else if(yValue > 0 && xValue < -5 && _video.canSeekForward()/*&& zValue < 3 && zValue > -3*/){
            Log.d("Action", "Advance");
            _video.pause();
            _video.seekTo(_video.getCurrentPosition() + 2000);
            //_video.start();
        }
        else if(yValue > 0 && zValue < -5 /*&& xValue < 3 && xValue > -3*/ && !_video.isPlaying()){
            Log.d("Action", "Play");
            _video.start();
            DefineTimer();
            _bPlay.setImageResource(android.R.drawable.ic_media_pause);
        }
        else if(yValue > 0 && zValue > 5 /*&& xValue < 3 && xValue > -3 */&& _video.isPlaying()){
            Log.d("Action", "Pause");
            _video.pause();
            _bPlay.setImageResource(android.R.drawable.ic_media_play);
        }
        //_video.start();
    }

    private void setBarListener(SeekBar bar){
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


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
        _bPlay = findViewById(R.id.buttonPlay);
        _bBack = findViewById(R.id.buttonBackward);
        _bFor = findViewById(R.id.buttonForward);
        _video =  findViewById(R.id.videoView);
        _timeBar =  findViewById(R.id.timeBar);
        //_URL = findViewById(R.id.editText);
        _title = findViewById(R.id.eventName);
        _sensorSwitch = findViewById(R.id.sensorSwitch);

        //_URL.setText(_defaultURL);
        _timeBar.setMax(_video.getDuration());
        _video.setVideoPath(_playList.get(_playListPosition));
    }

    public void onClickButtonPlay(View v){
        Log.d("Teste","Funcionou");

        _timeBar.setProgress(_video.getCurrentPosition());

        if(!_video.isPlaying()) {

            _video.start();

            DefineTimer();
            Log.d("Duração",_maxTime+"");
            _timeBar.setMax(_video.getDuration());
            _bPlay.setImageResource(android.R.drawable.ic_media_pause);

        }

        else{
            _video.pause();
            _bPlay.setImageResource(android.R.drawable.ic_media_play);
            _timer.cancel();

        }

    }

    public void onClickButtonBackward(View v){

        int position;
        if(_video.canSeekBackward()){
            position = _video.getCurrentPosition() - 5000;

            _timeBar.setProgress(position);
            _video.seekTo(position);
        }

    }

    public void onClickButtonForward(View v){

        int position;
        if(_video.canSeekForward()){
            position = _video.getCurrentPosition() + 5000;

            _timeBar.setProgress(position);
            _video.seekTo(position);
        }

    }

    public void onClickCheck(View v){

        if (_sensorSwitch.isChecked())
        {
            sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            _bPlay.setVisibility(View.GONE);
            _bBack.setVisibility(View.GONE);
            _bFor.setVisibility(View.GONE);
            Context context = getApplicationContext();
            CharSequence text = "Try moving the phone to control the video!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else
        {
            sensorManager.unregisterListener(MainActivity.this);
            _bPlay.setVisibility(View.VISIBLE);
            _bBack.setVisibility(View.VISIBLE);
            _bFor.setVisibility(View.VISIBLE);
        }


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
        //_timeBar.setMax(_video.getDuration());
        _timeBar.setMax(_maxTime);

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
