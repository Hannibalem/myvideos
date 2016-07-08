package com.dubsmash.anibal.myvideogallery;

import android.content.Context;

import com.dubsmash.anibal.myvideogallery.list_videos.ListVideosContract;
import com.dubsmash.anibal.myvideogallery.list_videos.ListVideosPresenter;
import com.dubsmash.anibal.myvideogallery.model.Video;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by anibal on 08.07.16.
 */
public class ListVideosUnitTest {

    @Mock
    private ListVideosContract.View mView;

    @Mock
    private ListVideosContract.Repository mRepository;

    @Mock
    private Context mContext;

    @Captor
    private ArgumentCaptor<ListVideosContract.Repository.RepositoryCallbacks> mPresenterCallbackCaptor;

    private ListVideosContract.UserActionListener mPresenter;

    @Before
    public void setupOpinionPresenter() {

        MockitoAnnotations.initMocks(this);

        mPresenter = new ListVideosPresenter(mContext, mView, mRepository);
    }

    @Test
    public void loadVideosSuccess() {
        mPresenter.loadVideos();

        verify(mRepository).loadVideos(mPresenterCallbackCaptor.capture());

        List<Video> videoList = new ArrayList<>();

        mPresenterCallbackCaptor.getValue().success(videoList);

        verify(mView).showVideos(videoList);
    }

    @Test
    public void loadVideosFail() {
        mPresenter.loadVideos();

        verify(mRepository).loadVideos(mPresenterCallbackCaptor.capture());

        mPresenterCallbackCaptor.getValue().failure();

        verify(mView).showError();
    }

}
