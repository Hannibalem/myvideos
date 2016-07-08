package com.dubsmash.anibal.myvideogallery.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by anibal on 06.07.16.
 */
@Table(database = MyVideoGalleryDatabase.class)
public class Video extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Unique
    public long id;

    @Column()
    public String filePath;

    @Column()
    public String name;

    @Column()
    public long duration;

    @Column()
    public String timeStamp;
}
