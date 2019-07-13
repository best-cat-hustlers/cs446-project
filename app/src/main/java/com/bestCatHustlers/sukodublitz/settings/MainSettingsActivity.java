package com.bestCatHustlers.sukodublitz.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

import com.bestCatHustlers.sukodublitz.R;

public class MainSettingsActivity extends AppCompatActivity implements MainSettingsContract.View {
    MainSettingsContract.Presenter presenter;
    MainSettingsModel mModel;
    SharedPreferences settings;
    static final String SHARE_PREF_KEY_SOUND_ENABLED = "sound";
    static final String SHARE_PREF_KEY_MUSIC_ENABLED = "music";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);
        mModel = (MainSettingsModel) MainSettingsModel.getInstance();
        settings = getSharedPreferences("globalSettings",MODE_PRIVATE);
        mModel.restoreState(settings);
        System.out.println("restore state.");
        presenter = new MainSettingsPresenter(this, mModel);
        Switch soundSwitch = findViewById(R.id.soundEnabled);
        soundSwitch.setChecked(presenter.isSoundEnabled());
        Switch musicSwitch = findViewById(R.id.Music);
        musicSwitch.setChecked(presenter.isMusicEnabled());

    }


    @Override
    protected void onDestroy() {
        presenter.viewDestroy();
        // Save global settings
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(SHARE_PREF_KEY_SOUND_ENABLED,presenter.isSoundEnabled());
        edit.putBoolean(SHARE_PREF_KEY_MUSIC_ENABLED,presenter.isMusicEnabled());
        edit.apply();
        System.out.println("Main Settings is destroyed");
        super.onDestroy();
    }

    public void onSoundCheck(View view) {
        Switch soundSwitch = (Switch) view;
        presenter.setSoundEnabled(soundSwitch.isChecked());
    }
    public void onMusicCheck(View view) {
        Switch musicSwitch = (Switch) view;
        presenter.setMusicEnabled(musicSwitch.isChecked());
    }
    public void onBackPressed(View view){
        super.onBackPressed();
    }
}
