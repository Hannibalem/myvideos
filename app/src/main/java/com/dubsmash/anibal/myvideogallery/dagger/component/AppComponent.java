package com.dubsmash.anibal.myvideogallery.dagger.component;

import com.dubsmash.anibal.myvideogallery.dagger.module.AppModule;
import com.dubsmash.anibal.myvideogallery.dagger.module.ListVideosModule;
import com.dubsmash.anibal.myvideogallery.dagger.module.ServiceModule;
import com.dubsmash.anibal.myvideogallery.list_videos.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by anibal on 08.07.16.
 */
@Singleton
@Component(modules = {ServiceModule.class, AppModule.class})
public interface AppComponent {

    ListVideosComponent plus(ListVideosModule listVideosModule);
}
