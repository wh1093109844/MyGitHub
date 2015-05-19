package com.example.miuscplayer;

import com.facebook.drawee.backends.pipeline.Fresco;

import static com.example.miuscplayer.ImagePipelineConfigFactory.getImagePipelineConfig;

/**
 * Created by he.b.wang on 15/5/15.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this, getImagePipelineConfig(this));
    }
}
