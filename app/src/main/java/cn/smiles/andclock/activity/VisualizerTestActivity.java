package cn.smiles.andclock.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.smiles.andclock.R;
import cn.smiles.andclock.view.VisualizerView;

public class VisualizerTestActivity extends AppCompatActivity implements View.OnClickListener {
    VisualizerView mVisualizerView;

    private MediaPlayer mMediaPlayer;
    private Visualizer mVisualizer;
    private TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizer_test);

        mVisualizerView = (VisualizerView) findViewById(R.id.myvisualizerview);
        findViewById(R.id.btn_music).setOnClickListener(this);
        textView4 = (TextView) findViewById(R.id.textView4);

        String musicUri = PreferenceManager.getDefaultSharedPreferences(this).getString("musicUri", null);
        if (musicUri != null) {
            initAudio(Uri.parse(musicUri));
        }

        textView4.post(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null) {
                    int currentPosition = mMediaPlayer.getCurrentPosition();
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(currentPosition);
                    String current = String.format(Locale.getDefault(), "%02d:%02d",
                            minutes,
                            TimeUnit.MILLISECONDS.toSeconds(currentPosition) -
                                    TimeUnit.MINUTES.toSeconds(minutes)
                    );
                    int duration = mMediaPlayer.getDuration();
                    long minutes1 = TimeUnit.MILLISECONDS.toMinutes(duration);
                    String total = String.format(Locale.getDefault(), "%02d:%02d",
                            minutes1,
                            TimeUnit.MILLISECONDS.toSeconds(duration) -
                                    TimeUnit.MINUTES.toSeconds(minutes1)
                    );
                    textView4.setText(current + " / " + total);
                }
                textView4.postDelayed(this, 1000);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && mMediaPlayer != null) {
            mVisualizer.release();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initAudio(Uri uri) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mMediaPlayer = MediaPlayer.create(this, uri);
        mMediaPlayer.setLooping(true);
        // When the stream ends, we don't need to collect any more data. We
        // don't do this in
        // setupVisualizerFxAndUI because we likely want to have more,
        // non-Visualizer related code
        // in this callback.
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
            }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                setupVisualizerFxAndUI();
            }
        });

    }

    private void setupVisualizerFxAndUI() {
        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                        mVisualizerView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);

        // Make sure the visualizer is enabled only when you actually want to
        // receive data, and
        // when it makes sense to receive data.
        mVisualizer.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
//        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, 199);

        final Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
        intent2.setType("audio/*");
        startActivityForResult(intent2, 199);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 199 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null) return;
            initAudio(uri);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("musicUri", uri.toString()).apply();
        } else {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
            }
        }
    }
}
