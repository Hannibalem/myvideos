package com.dubsmash.anibal.myvideogallery.dagger.component;

import com.dubsmash.anibal.myvideogallery.dagger.module.ListVideosModule;
import com.dubsmash.anibal.myvideogallery.dagger.scope.ActivityScope;
import com.dubsmash.anibal.myvideogallery.list_videos.MainActivity;

import dagger.Subcomponent;

/**
 * Created by anibal on 08.07.16.
 */
@ActivityScope
@Subcomponent(modules = {ListVideosModule.class})
public interface ListVideosComponent {

    void inject(MainActivity activity);
}
