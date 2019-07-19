package com.bestCatHustlers.sukodublitz;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.bestCatHustlers.sukodublitz.settings.MainSettingsModel;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BackgroundMusicService extends Service {
    private MediaPlayer mediaPlayer;
    GlobalSettingsInterface globalSettingsInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BackgroundMusicService", "onCreate");
        globalSettingsInterface = MainSettingsModel.getInstance();
        // TODO: need to replace this in after previous PR get merged
        //((MainSettingsModel)globalSettingsInterface).restoreLastState(this);
        try {
            String path = this.getFilesDir().getAbsolutePath();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.nocturne_in_e_flat_major_op_9_no_2);
            Log.d("BackgroundMusicService","Song is set as Data Source");
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(100,100);
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void onPause(){
        mediaPlayer.pause();
    }
    public void onResume(){
        mediaPlayer.start();
    }

}
