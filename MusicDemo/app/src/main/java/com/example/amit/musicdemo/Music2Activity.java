package com.example.amit.musicdemo;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;

import java.io.IOException;

public class Music2Activity extends AppCompatActivity {

    //private Context mContext;
    //private Activity mActivity;

    private LinearLayout mRootLayout;
    private Button mButtonPlay;

    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music2);

        // Get the application context
        //mContext = getApplicationContext();
        //mActivity = MainActivity.this;

        // Get the widget reference from xml layout
        // mRootLayout = findViewById(R.id.root_layout);
        mButtonPlay = (Button) findViewById(R.id.btn_play);

        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Disable the play button
                mButtonPlay.setEnabled(false);

                // The audio url to play
                String audioUrl = "http://www.all-birds.com/Sound/western%20bluebird.wav";

                // Initialize a new media player instance
                mPlayer = new MediaPlayer();

                // Set the media player audio stream type
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //Try to play music/audio from url
                try{
                    /*
                        void setDataSource (String path)
                            Sets the data source (file-path or http/rtsp URL) to use.

                        Parameters
                            path String : the path of the file, or the http/rtsp URL of the stream you want to play

                        Throws
                            IllegalStateException : if it is called in an invalid state

                                When path refers to a local file, the file may actually be opened by a
                                process other than the calling application. This implies that the
                                pathname should be an absolute path (as any other process runs with
                                unspecified current working directory), and that the pathname should
                                reference a world-readable file. As an alternative, the application
                                could first open the file for reading, and then use the file
                                descriptor form setDataSource(FileDescriptor).

                            IOException
                            IllegalArgumentException
                            SecurityException
                    */
                    // Set the audio data source
                    mPlayer.setDataSource(audioUrl);

                    /*
                        void prepare ()
                            Prepares the player for playback, synchronously. After setting the
                            datasource and the display surface, you need to either call prepare()
                            or prepareAsync(). For files, it is OK to call prepare(), which blocks
                            until MediaPlayer is ready for playback.

                        Throws
                            IllegalStateException : if it is called in an invalid state
                            IOException
                    */
                    // Prepare the media player
                    mPlayer.prepare();

                    // Start playing audio from http url
                    mPlayer.start();

                    // Inform user for audio streaming
                    //Toast.makeText(mContext,"Playing",Toast.LENGTH_SHORT).show();
                    Toast.makeText(Music2Activity.this, "Playing", Toast.LENGTH_SHORT).show();
                }catch (IOException e){
                    // Catch the exception
                    e.printStackTrace();
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }catch (SecurityException e){
                    e.printStackTrace();
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        //Toast.makeText(mContext,"End",Toast.LENGTH_SHORT).show();
                        Toast.makeText(Music2Activity.this, "End", Toast.LENGTH_SHORT).show();
                        mButtonPlay.setEnabled(true);
                    }
                });
            }
        });
    }
}