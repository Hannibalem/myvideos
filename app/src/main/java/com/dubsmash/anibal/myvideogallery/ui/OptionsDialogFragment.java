package com.dubsmash.anibal.myvideogallery.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dubsmash.anibal.myvideogallery.R;

/**
 * Created by anibal on 06.07.16.
 */
public class OptionsDialogFragment extends DialogFragment {

    private Callbacks mListener;

    private String mVideoPath;

    private int mPosition;

    private long mIdVideo;

    public OptionsDialogFragment() {
        // Required empty constructor.
    }

    public static OptionsDialogFragment newInstance(int position, long idVideo, String videoPath) {
        Bundle args = new Bundle();
        args.putString("videoPath", videoPath);
        args.putInt("position", position);
        args.putLong("idVideo", idVideo);
        return createDialog(args);
    }

    private static OptionsDialogFragment createDialog(Bundle args){
        OptionsDialogFragment dialogFragment = new OptionsDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mPosition = getArguments().getInt("position");

        mIdVideo = getArguments().getLong("idVideo");

        mVideoPath = getArguments().getString("videoPath");

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        View view
                = getActivity().getLayoutInflater().inflate(R.layout.options_layout, null);

        view.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeleteVideo(mPosition, mIdVideo, mVideoPath);
                dismiss();
            }
        });

        view.findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShareVideo(mPosition, mVideoPath);
                dismiss();
            }
        });

        builder.customView(view, false);
        return builder.build();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface Callbacks {

        void onShareVideo(int position, String videoPath);

        void onDeleteVideo(int position, long idVideo, String videoPath);
    }
}
