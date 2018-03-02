package cn.smiles.andclock.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.smiles.andclock.R;

public class MainActivity extends AppCompatActivity {

    private static final int PICKFILE_REQUEST_CODE = 199;
    private MediaPlayer player;
    private AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        player = new MediaPlayer();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playMus(mp);
            }
        });
    }

    private void playMus(MediaPlayer mp) {
        int rfocus = am.requestAudioFocus(focusChangeListener,
                // 指定所使用的音频流
                AudioManager.STREAM_MUSIC,
                // 请求长时间的音频焦点
                AudioManager.AUDIOFOCUS_GAIN);
        Log.i("===", "result= " + rfocus);
        if (rfocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {// 开始播放
            mp.start();
        }
    }

    AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.i("===", "focusChange= " + focusChange);
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS://会长时间失去，所以告知下面的判断，获得焦点后不要自动播放
                    Log.i("===", "AUDIOFOCUS_LOSS");
                    player.pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.i("===", "AUDIOFOCUS_LOSS_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://短暂失去焦点，先暂停。同时将标志位置成重新获得焦点后就开始播放
                    Log.i("===", "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;
                case AudioManager.AUDIOFOCUS_GAIN://重新获得焦点，且符合播放条件，开始播放
                    Log.i("===", "AUDIOFOCUS_GAIN");
                    player.start();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        am.abandonAudioFocus(focusChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
                break;
            case R.id.button2:
                playMus(player);
                break;
            case R.id.button3:
//                player.pause();
//                am.abandonAudioFocus(focusChangeListener);

                ArrayList<String> flist = new ArrayList<>();
                File root = Environment.getExternalStorageDirectory();
                File[] files = root.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return !name.startsWith(".");
                    }
                });
                for (File f : files) {
                    flist.add(f.getName());
                }
                System.out.println(flist);
                Collections.sort(flist, comparatorName);
                System.out.println(flist);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (PICKFILE_REQUEST_CODE == requestCode && resultCode == RESULT_OK && data != null) {
            String fpath = data.getDataString();
            try {
                player.setDataSource(fpath);
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 按照文件名称排序规则
     */
    private static Comparator comparatorName = new Comparator<String>() {
        @Override
        public int compare(String f1, String f2) {
            return f1.compareToIgnoreCase(f2);
        }
    };
}
