package com.dubsmash.anibal.myvideogallery.list_videos;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.dubsmash.anibal.myvideogallery.model.Video;

import java.util.List;

/**
 * Created by anibal on 08.07.16.
 */
public interface ListVideosContract {

    interface View {

        void showVideos(List<Video> videos);

        void openVideoActivity(String filePath);

        void openCamera();

        void deleteVideoFromListVideos(int position, String filePath);

        void shareVideo(String filePath);

        void showOptionsDialog(int position, long idVideo, String filePath);

        void showError();

        void addVideoToListVideos(Video video);
    }

    interface UserActionListener {

        void loadVideos();

        void openVideo(String filePath);

        void startRecording();

        void shareVideo(String filePath);

        void deleteVideo(int position, long idVideo, String filePath);

        void showOptions(int position, long idVideo, String filePath);

        void videoRecorded(MediaMetadataRetriever retriever, Uri fileUri);
    }

    interface Repository {

        void loadVideos(RepositoryCallbacks callbacks);

        void deleteVideo(long idVideo, String filePath);

        Video saveVideo(MediaMetadataRetriever retriever, Uri fileUri);

        interface RepositoryCallbacks {

            void success(List<Video> videos);

            void failure();
        }
    }
}
