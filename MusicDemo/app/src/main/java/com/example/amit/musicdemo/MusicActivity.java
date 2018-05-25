package com.example.amit.musicdemo;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MusicActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    private LinearLayout mRootLayout;
    private Button mButtonPlay, forward, backward, pause;
    private SeekBar songProgressBar;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    MediaPlayer mPlayer;

    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    private Utilities utils;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        NotificationGenerator.customBigNotification(getApplicationContext());

        intent = new Intent(getApplicationContext(), MediaPlayerService.class);


        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songCurrentDurationLabel = (TextView) findViewById(R.id.start);
        songTotalDurationLabel = (TextView) findViewById(R.id.end);
        mButtonPlay = (Button) findViewById(R.id.btn_play);
        forward = (Button) findViewById(R.id.forward);
        backward = (Button) findViewById(R.id.backward);
        pause = (Button) findViewById(R.id.pause);

        utils = new Utilities();

        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        //mPlayer.setOnCompletionListener(this); // Important

        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Disable the play button
                mButtonPlay.setEnabled(false);

                // The audio url to play
                /*String audioUrl = "http://www.villopim.com.br/android/Music_01.mp3";

                // Initialize a new media player instance
                mPlayer = new MediaPlayer();

                // Set the media player audio stream type
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //Try to play music/audio from url
                try {
                    // Set the audio data source
                    mPlayer.setDataSource(audioUrl);

                    // Prepare the media player
                    mPlayer.prepare();

                    // Start playing audio from http url
                    mPlayer.start();

                    // Inform user for audio streaming
                    Toast.makeText(MusicActivity.this, "Playing", Toast.LENGTH_SHORT).show();

                    //set progress
                    // set Progress bar values
                    songProgressBar.setProgress(0);
                    songProgressBar.setMax(100);

                    // Updating progress bar
                    updateProgressBar();


                } catch (IOException e) {
                    // Catch the exception
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        //Toast.makeText(mContext,"End",Toast.LENGTH_SHORT).show();
                        Toast.makeText(MusicActivity.this, "End", Toast.LENGTH_SHORT).show();
                        mButtonPlay.setEnabled(true);
                    }
                });*/

                intent.setAction(MediaPlayerService.ACTION_PLAY);
                startService(intent);

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (mPlayer.isPlaying())
                    mPlayer.pause();
                else
                    mPlayer.start();*/
                intent = new Intent(getApplicationContext(), MediaPlayerService.class);
                intent.setAction(MediaPlayerService.ACTION_PAUSE);
                startService(intent);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // forward or backward to certain seconds
                mPlayer.seekTo(mPlayer.getCurrentPosition() + 10000);

                // update timer progress again
                updateProgressBar();
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // forward or backward to certain seconds
                mPlayer.seekTo(mPlayer.getCurrentPosition() - 10000);

                // update timer progress again
                updateProgressBar();
            }
        });
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
// remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mPlayer.getDuration();
            long currentDuration = mPlayer.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);
            //songProgressBar.setSecondaryProgress(progress + 10);
            mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                    songProgressBar.setSecondaryProgress(i);
                }
            });

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

}