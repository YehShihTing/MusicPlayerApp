package idv.yst.music_list;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;

public class Music_Activity extends AppCompatActivity implements View.OnClickListener{
    private static SeekBar sb;
    private static TextView tv_progress, tv_total, name_song;
    private ObjectAnimator animator;
    private MusicService.MusicControl musicControl;
    String name;
    Intent intent1, intent2;
    MyServiceConn conn;
    private boolean isUnBind=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        intent1=getIntent();
        init();
    }
    private void init(){
        tv_progress=(TextView)findViewById(R.id.tv_progress);
        tv_total=(TextView)findViewById(R.id.tv_total);
        sb=(SeekBar)findViewById(R.id.sb);
        name_song=(TextView)findViewById(R.id.song_name);

        findViewById(R.id.btn_play).setOnClickListener(this);
        findViewById(R.id.btn_continue_play).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);

    name=intent1.getStringExtra("name");
    name_song.setText(name);
    intent2=new Intent(this,MusicService.class);
    conn=new MyServiceConn();
    bindService(intent2,conn,BIND_AUTO_CREATE);

    sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(progress==seekBar.getMax()){
                animator.pause();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress=seekBar.getProgress();
            musicControl.seekTo(progress);
        }
    });
        ImageView iv_music=(ImageView)findViewById(R.id.iv_music);
        String position=intent1.getStringExtra("position");
        int i=parseInt(position);
        iv_music.setImageResource(frag1.icons[i]);

        animator=ObjectAnimator.ofFloat(iv_music,"rotation",0f,360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
    }
    public static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            Bundle bundle=msg.getData();
            int duration=bundle.getInt("duration");
            int currentPosition=bundle.getInt("currentPosition");
            sb.setMax(duration);
            sb.setProgress(currentPosition);

            int minute=duration/1000/60;
            int second=duration/1000%60;
            String strMinute=null;
            String strSecond=null;
            strMinute=minute<10?"0"+minute:minute+"";
            strSecond=second<10?"0"+second:minute+"";
            tv_progress.setText(strMinute+":"+strSecond);
        }
    };
    class MyServiceConn implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl=(MusicService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    private void unBind(boolean isUnBind){
        if(!isUnBind){
            musicControl.pausePlay();
            unbindService(conn);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                String position=intent1.getStringExtra("position");
                int i= parseInt(position);
                musicControl.play(i);
                animator.start();
                break;
            case R.id.btn_pause:
                musicControl.pausePlay();
                animator.pause();
                break;
            case R.id.btn_continue_play:
                musicControl.continuePlay();
                animator.start();
                break;
            case R.id.btn_exit:
                unBind(isUnBind);
                isUnBind=true;
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unBind(isUnBind);
    }
}