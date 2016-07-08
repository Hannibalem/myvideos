package com.dubsmash.anibal.myvideogallery.play_video;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.dubsmash.anibal.myvideogallery.R;

public class VideoActivity extends AppCompatActivity implements OnPreparedListener, View.OnTouchListener, OnCompletionListener {

    private EMVideoView mVideoView;

    private String mVideoPath;

    private boolean mReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mVideoPath = getIntent().getStringExtra("videoPath");
        setupVideoView();
    }

    private void setupVideoView() {
        mVideoView = (EMVideoView)findViewById(R.id.video_view);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setVideoURI(Uri.parse(mVideoPath));
        mVideoView.setOnTouchListener(this);
        mVideoView.setOnCompletionListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoView.isPlaying() && mReady) {
            mVideoView.start();
        }
    }

    @Override
    public void onPrepared() {
        mReady = true;
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
            } else {
                mVideoView.start();
            }
        }
        return true;
    }

    @Override
    public void onCompletion() {
        mVideoView.restart();
    }
}
