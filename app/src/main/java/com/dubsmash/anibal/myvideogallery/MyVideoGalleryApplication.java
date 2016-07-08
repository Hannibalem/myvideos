package com.dubsmash.anibal.myvideogallery;

import android.app.Application;

import com.dubsmash.anibal.myvideogallery.dagger.component.AppComponent;
import com.dubsmash.anibal.myvideogallery.dagger.component.DaggerAppComponent;
import com.dubsmash.anibal.myvideogallery.dagger.module.AppModule;
import com.dubsmash.anibal.myvideogallery.dagger.module.ServiceModule;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by anibal on 06.07.16.
 */
public class MyVideoGalleryApplication extends Application {

    public AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this)).serviceModule(new ServiceModule()).build();

        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
