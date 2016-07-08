package com.dubsmash.anibal.myvideogallery.dagger.module;

import android.content.Context;

import com.dubsmash.anibal.myvideogallery.dagger.scope.ActivityScope;
import com.dubsmash.anibal.myvideogallery.list_videos.ListVideosContract;
import com.dubsmash.anibal.myvideogallery.list_videos.ListVideosPresenter;
import com.dubsmash.anibal.myvideogallery.list_videos.ListVideosRepository;
import com.dubsmash.anibal.myvideogallery.list_videos.MainActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by anibal on 08.07.16.
 */
@Module
public class ListVideosModule {

    private MainActivity mActivity;

    public ListVideosModule(MainActivity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityScope
    ListVideosContract.View providesView() {
        return mActivity;
    }

    @Provides
    @ActivityScope
    Context providesContext() {
        return mActivity;
    }

    @Provides
    @ActivityScope
    ListVideosContract.Repository providesRepository() {
        return new ListVideosRepository();
    }

    @Provides
    @ActivityScope
    ListVideosContract.UserActionListener providesPresenter(ListVideosContract.Repository repository,
                                                            ListVideosContract.View view,
                                                            Context context) {
        return new ListVideosPresenter(context, view, repository);
    }
}
