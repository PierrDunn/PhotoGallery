package com.pierrdunn.photogallery;

import android.os.HandlerThread;
import android.util.Log;

/**
 * Created by pierrdunn on 10.03.18.
 * Фоновый поток для эффективной загрузки изображений
 */

public class ThumbnailDowloader<T> extends HandlerThread {

    private static final String TAG = "ThumbnailDowloader";

    private boolean mHasQuit = false;

    public ThumbnailDowloader() {
        super(TAG);
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    public void queueThumbnail(T target, String url){
        Log.i(TAG, "Got a URL: " + url);
    }
}
