package com.dubsmash.anibal.myvideogallery.list_videos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dubsmash.anibal.myvideogallery.MyVideoGalleryApplication;
import com.dubsmash.anibal.myvideogallery.R;
import com.dubsmash.anibal.myvideogallery.adapter.VideosAdapter;
import com.dubsmash.anibal.myvideogallery.dagger.module.ListVideosModule;
import com.dubsmash.anibal.myvideogallery.model.MyVideoGalleryDatabase;
import com.dubsmash.anibal.myvideogallery.model.Video;
import com.dubsmash.anibal.myvideogallery.ui.OptionsDialogFragment;
import com.dubsmash.anibal.myvideogallery.play_video.VideoActivity;
import com.dubsmash.anibal.myvideogallery.utils.CameraUtils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements VideosAdapter.Callbacks, OptionsDialogFragment.Callbacks, ListVideosContract.View {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private Uri mFileUri;

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private List<Video> mListVideos = new ArrayList<>();

    @Inject
    ListVideosContract.UserActionListener mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyVideoGalleryApplication) getApplication()).mAppComponent
                .plus(new ListVideosModule(this)).inject(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_videos);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new VideosAdapter(this, mListVideos, this);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            showVideos(getLastCustomNonConfigurationInstance());
            return;
        }

        mPresenter.loadVideos();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mListVideos;
    }

    @Override
    public List<Video> getLastCustomNonConfigurationInstance() {
        return (List<Video>) super.getLastCustomNonConfigurationInstance();
    }

    //View listener
    @Override
    public void showVideos(List<Video> videos) {
        mListVideos.clear();
        mListVideos.addAll(videos);
        ((VideosAdapter) mAdapter).setLoaderActive(false);
        mAdapter.notifyDataSetChanged();
    }

    //View listener
    @Override
    public void showError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    //View listener
    @Override
    public void openVideoActivity(String filePath) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("videoPath", filePath);
        startActivity(intent);
    }

    //View listener
    @Override
    public void showOptionsDialog(int position, long idVideo, String filePath) {
        OptionsDialogFragment.newInstance(position, idVideo, filePath)
                .show(getSupportFragmentManager(), "dialog");
    }

    //View listener
    @Override
    public void deleteVideoFromListVideos(int position, String filePath) {
        File file = new File(filePath);
        file.delete();

        mListVideos.get(position).delete();

        mListVideos.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    //View listener
    @Override
    public void shareVideo(String filePath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("video/mp4");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent, "share"));
    }

    //View listener
    @Override
    public void openCamera() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2909);
                return;
            }
        }

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        mFileUri = CameraUtils.getOutputMediaFileUri(CameraUtils.MEDIA_TYPE_VIDEO);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    //View listener
    @Override
    public void addVideoToListVideos(Video video) {
        mListVideos.add(video);
        mAdapter.notifyItemInserted(mListVideos.size() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_record) {
            mPresenter.startRecording();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 2909){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                mPresenter.startRecording();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, mFileUri);

            mPresenter.videoRecorded(retriever, mFileUri);
        }
    }

    //Adapter listener
    @Override
    public void videoSelected(String videoPath) {
        mPresenter.openVideo(videoPath);
    }

    //Adapter listener
    @Override
    public void videoLongSelected(String videoPath, long idVideo, int position) {
        mPresenter.showOptions(position, idVideo, videoPath);
    }

    //Dialog listener
    @Override
    public void onShareVideo(int position, String videoPath) {
        mPresenter.shareVideo(videoPath);
    }

    //Dialog listener
    @Override
    public void onDeleteVideo(int position, long idVideo, String videoPath) {
        mPresenter.deleteVideo(position, idVideo, videoPath);
    }
}
