package com.example.demomusicplayerver101;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demomusicplayerver101.model.Song;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicPlayerActivity extends AppCompatActivity {
    CircleImageView songImageView;
    TextView songNameTextView, artistTextView;
    ImageButton previousImageButton, playImageButton, nextImageButton;
    SeekBar positionSeekBar;
    TextView elapsedTimeTextView, remainingTimeTextView;

    Song song;
    MediaPlayer mediaPlayer;
    int totalTime;
    Handler handler;

    RotateAnimation rotateAnimation;

    private boolean isPlayed = true;
    File fileDir;
    File[] files;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        songImageView = findViewById(R.id.songImageView);
        songNameTextView = findViewById(R.id.songNameTextView);
        artistTextView = findViewById(R.id.artistTextView);

        previousImageButton = findViewById(R.id.previousImageButton);
        playImageButton = findViewById(R.id.playImageButton);
        nextImageButton = findViewById(R.id.nextImageButton);

        positionSeekBar = findViewById(R.id.positionSeekBar);
        elapsedTimeTextView = findViewById(R.id.elapsedTimeTextView);
        remainingTimeTextView = findViewById(R.id.remainingTimeTextView);

        Toast.makeText(MusicPlayerActivity.this, "hihi", Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();
        song = new Song();
        song = (Song) intent.getSerializableExtra("Song");

        displayContent();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        fileDir = new File(getFilesDir().getAbsolutePath());
        files = fileDir.listFiles();

        for (File file : files) {
            if (file.getName().equals(song.getSongName() + ".mp3")) {
                Uri uri = Uri.parse(file.getAbsolutePath());
                try {
                    mediaPlayer.setDataSource(MusicPlayerActivity.this, uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else {
                try {
                    mediaPlayer.setDataSource(song.getSongPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log.d("hihi", "hello");
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
                    Log.d("hihi", percent + "");
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer) {

                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    Log.d("hihi", "complete");
                }
            });

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaPlayer.seekTo(0);
        totalTime = mediaPlayer.getDuration();
        positionSeekBar.setMax(totalTime);
        positionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    positionSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Thread (update positonBar & timeLabel)
        // Worker Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    Message message = new Message();
                    message.what = mediaPlayer.getCurrentPosition();
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // UI Thread
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int currentPosition = msg.what;
                // update position seek bar
                positionSeekBar.setProgress(currentPosition);

                // update time label
                String elapsedTime = createTimeLabel(currentPosition);
                String remainingTime = createTimeLabel(totalTime);

                elapsedTimeTextView.setText(elapsedTime);
                remainingTimeTextView.setText(remainingTime);
            }
        };

    }

    private String createTimeLabel(int currentPosition) {
        String timeLabel = "";
        int minute = currentPosition / 1000 / 60;
        int second = currentPosition / 1000 % 60;

        timeLabel = minute + ":";
        if (second < 10) {
            timeLabel += "0";
        }
        timeLabel += second;

        return timeLabel;
    }

    private void displayContent() {
        Picasso.get().load(song.getImage()).into(songImageView);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // this is run 1 leo, ko phai toi luc het 360 roi dung lai 1 luc, wowwww
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(15000);
        songImageView.startAnimation(rotateAnimation);

        songNameTextView.setText(song.getSongName());
        artistTextView.setText(song.getArtist());
    }

    public void playMusic(View view) {
        if (isPlayed == true && playImageButton.isPressed()) {
            playImageButton.setImageResource(R.drawable.ic_play_arrow_white_32dp);
            mediaPlayer.pause();
            isPlayed = false;
        }
        else if (isPlayed == false && playImageButton.isPressed()) {
            playImageButton.setImageResource(R.drawable.ic_pause_white_32dp);
            mediaPlayer.start();
            isPlayed = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }
}
