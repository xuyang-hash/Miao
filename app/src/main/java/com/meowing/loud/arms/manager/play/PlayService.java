package com.meowing.loud.arms.manager.play;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class PlayService extends Service {

    private PlayMusicManager musicManager;

    private static final String TAG = "PlayService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"->onCreate");
        if (musicManager == null) {
            musicManager = new PlayMusicManager();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"->IBinder");
        return musicManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicManager = null;
    }
}
