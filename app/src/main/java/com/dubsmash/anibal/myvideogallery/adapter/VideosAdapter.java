package com.dubsmash.anibal.myvideogallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dubsmash.anibal.myvideogallery.R;
import com.dubsmash.anibal.myvideogallery.model.Video;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by anibal on 06.07.16.
 */
public class VideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;

    private final int VIEW_PROG = 0;

    private List<Video> mListVideos;

    private Context mContext;

    private Callbacks mListener;

    private boolean mLoaderActive = true;

    private DateTimeFormatter mTimeInFormatter;

    private DateTimeFormatter mTimeOutFormatter;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mComment;

        public ImageView mAvatar;

        public View mRow;

        public TextView mDate;

        public TextView mDuration;

        public ViewHolder(View v) {
            super(v);
            mComment = (TextView) v.findViewById(R.id.comment);
            mDate = (TextView) v.findViewById(R.id.timeStamp);
            mDuration = (TextView) v.findViewById(R.id.duration);
            mAvatar = (ImageView) v.findViewById(R.id.avatar);
            mRow = v.findViewById(R.id.container_row);
        }
    }

    public static class LoaderViewHolder extends RecyclerView.ViewHolder {

        public View mLoader;

        public LoaderViewHolder(View v) {
            super(v);
            mLoader = v.findViewById(R.id.loader);
        }
    }

    public VideosAdapter(Context context, List<Video> listVideos, Callbacks listener) {
        mContext = context;
        mListVideos = listVideos;
        mListener = listener;
        mTimeInFormatter = DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss.SSSZ");
        mTimeOutFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v;
        switch (viewType) {
            case VIEW_ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row, parent, false);
                vh = new ViewHolder(v);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loader_row, parent, false);
                vh = new LoaderViewHolder(v);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {

            final Video video = mListVideos.get(position);
            ((ViewHolder) holder).mComment.setText(video.name);

            Glide.with(mContext)
                    .load(video.filePath)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .into(((ViewHolder) holder).mAvatar);

            ((ViewHolder) holder).mRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.videoSelected(video.filePath);
                }
            });

            ((ViewHolder) holder).mRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.videoLongSelected(video.filePath, video.id, position);
                    return false;
                }
            });

            // Parsing the date
            try {
                DateTime time = mTimeInFormatter.parseDateTime(video.timeStamp);
                ((ViewHolder) holder).mDate.setText(mTimeOutFormatter.print(time));
            } catch (Exception e) {
                e.printStackTrace();
                ((ViewHolder) holder).mDate.setText(video.timeStamp);
            }

            ((ViewHolder) holder).mDuration.setText(String.valueOf(video.duration / 1000) + " secs");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position < mListVideos.size() ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        if (mLoaderActive) {
            return mListVideos.size() + 1;
        }

        return mListVideos.size();
    }

    public void setLoaderActive(boolean loaderActive) {
        this.mLoaderActive = loaderActive;
    }


    public interface Callbacks {

        void videoSelected(String videoPath);

        void videoLongSelected(String videoPath, long idVideo, int position);
    }
}
