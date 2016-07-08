package com.dubsmash.anibal.myvideogallery.list_videos;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.dubsmash.anibal.myvideogallery.model.MyVideoGalleryDatabase;
import com.dubsmash.anibal.myvideogallery.model.Video;
import com.dubsmash.anibal.myvideogallery.model.Video_Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.io.File;
import java.util.List;

/**
 * Created by anibal on 08.07.16.
 */
public class ListVideosRepository implements ListVideosContract.Repository {

    @Override
    public void loadVideos(final RepositoryCallbacks callbacks) {
        FlowManager.getDatabase(MyVideoGalleryDatabase.class).beginTransactionAsync(
                new QueryTransaction.Builder<>(
                        SQLite.select().from(Video.class))
                        .queryResult(new QueryTransaction.QueryResultCallback<Video>() {
                            @Override
                            public void onQueryResult(QueryTransaction transaction, @NonNull CursorResult<Video> tResult) {
                                callbacks.success(((CursorResult<Video>) tResult).toList());
                            }
                        }).build()).build().execute();
    }

    @Override
    public void deleteVideo(long idVideo, String filePath) {
        File file = new File(filePath);
        file.delete();

        SQLite.select().from(Video.class).where(Video_Table.id.eq(idVideo)).querySingle().delete();
    }

    @Override
    public Video saveVideo(MediaMetadataRetriever retriever, Uri fileUri) {

        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        String timeStamp = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        String name = fileUri.getPath().substring(fileUri.getPath().lastIndexOf("/") + 1);

        Video video = new Video();
        video.name = name;
        video.filePath = fileUri.getPath();
        video.duration = Long.valueOf(time);
        video.timeStamp = timeStamp;
        video.save();

        return video;
    }
}
