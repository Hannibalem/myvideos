package com.dubsmash.anibal.myvideogallery.list_videos;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.dubsmash.anibal.myvideogallery.model.Video;

import java.util.List;

/**
 * Created by anibal on 08.07.16.
 */
public class ListVideosPresenter implements ListVideosContract.UserActionListener {

    private ListVideosContract.View mView;

    private ListVideosContract.Repository mRepository;

    private Context mContext;

    public ListVideosPresenter(Context context, ListVideosContract.View view, ListVideosContract.Repository repository) {
        mContext = context;
        mView = view;
        mRepository = repository;
    }

    @Override
    public void loadVideos() {
        mRepository.loadVideos(new ListVideosContract.Repository.RepositoryCallbacks() {
            @Override
            public void success(List<Video> videos) {
                mView.showVideos(videos);
            }

            @Override
            public void failure() {
                mView.showError();
            }
        });
    }

    @Override
    public void startRecording() {
        mView.openCamera();
    }

    @Override
    public void videoRecorded(MediaMetadataRetriever retriever, Uri fileUri) {
        Video video = mRepository.saveVideo(retriever, fileUri);
        mView.addVideoToListVideos(video);
    }

    @Override
    public void openVideo(String filePath) {
        mView.openVideoActivity(filePath);
    }

    @Override
    public void showOptions(int position, long idVideo, String filePath) {
        mView.showOptionsDialog(position, idVideo, filePath);
    }

    @Override
    public void shareVideo(String filePath) {
        mView.shareVideo(filePath);
    }

    @Override
    public void deleteVideo(int position, long idVideo, String filePath) {
        mRepository.deleteVideo(idVideo, filePath);
        mView.deleteVideoFromListVideos(position, filePath);
    }
}


