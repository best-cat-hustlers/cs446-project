package com.bestCatHustlers.sukodublitz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

public class MainSettingsActivity extends AppCompatActivity implements MainSettingsContract.View {
    MainSettingsContract.Presenter presenter;
    MainSettingsContract.Model mModel;
    SharedPreferences settings;
    static final String SHARE_PREF_KEY_SOUND = "sound";
    static final String SHARE_PREF_KEY_MUSIC = "music";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);
        mModel = MainSettingsModel.getInstance();
        settings = getSharedPreferences("globalSettings",MODE_PRIVATE);
        mModel.restoreState(settings);
        System.out.println("restore state.");
        presenter = new MainSettingsPresenter(this, mModel);
        Switch soundSwitch = findViewById(R.id.sound);
        soundSwitch.setChecked(presenter.getSound());
        Switch musicSwitch = findViewById(R.id.Music);
        musicSwitch.setChecked(presenter.getMusic());

    }


    @Override
    protected void onDestroy() {
        presenter.viewDestroy();
        // Save global settings
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(SHARE_PREF_KEY_SOUND,presenter.getSound());
        edit.putBoolean(SHARE_PREF_KEY_MUSIC,presenter.getMusic());
        edit.apply();
        System.out.println("Main Settings is destroyed");
        super.onDestroy();
    }

    public void onSoundCheck(View view) {
        Switch soundSwitch = (Switch) view;
        presenter.turnSound(soundSwitch.isChecked());
    }
    public void onMusicCheck(View view) {
        Switch musicSwitch = (Switch) view;
        presenter.turnMusic(musicSwitch.isChecked());
    }
    public void onBackPressed(View view){
        super.onBackPressed();
    }
}
