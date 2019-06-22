package cn.cslg.bigtask.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.util.Random;

import cn.cslg.bigtask.R;

public class MainActivity extends AppCompatActivity {
    private boolean musicstart =false;
    LinearLayout layout;
    private MediaPlayer mp;
    private AlertDialog.Builder builder;
    private MenuItem menuItem;
    SoundPool soundPool;
    int id1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(this,R.raw.music);
        layout= (LinearLayout) findViewById(R.id.id_ll);
        soundPool=new SoundPool(2, AudioManager.STREAM_SYSTEM,0);
        id1=soundPool.load(this,R.raw.sd1,1);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                showAbout();
                soundPool.play(id1,1,1,1,1,1f);
                break;
            case R.id.music:
                soundPool.play(id1,1,1,1,1,1f);
                if(musicstart){
                    mp.pause();
                    musicstart=false;
                }
                else {
                    mp.start();
                    musicstart = true;
                }
                break;
            case R.id.cbg:
                //变换颜色
                Random random = new Random();
                int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                layout.setBackgroundColor(color);
                soundPool.play(id1,1,1,1,1,1f);
                break;
            case R.id.collect:
               Intent intent = new Intent(MainActivity.this,LikeActivity.class);
               startActivity(intent);
                soundPool.play(id1,1,1,1,1,1f);
                break;

        }
        return true;
    }

    private void showAbout() {
        builder = new AlertDialog.Builder(this).setTitle("关于")
                .setMessage("电视节目单\n"+
                        "版本1.0\n"+"作者：金贝\n"+"学号：Z09416234\n").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

}


