package com.lcarrasco.chihuahua_noticias;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by lcarrasco on 7/6/16.
 */
public class ChihuahuaNoticiasApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
