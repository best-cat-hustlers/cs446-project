package com.bestCatHustlers.sukodublitz.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.bestCatHustlers.sukodublitz.R;

public class MainSettingsActivity extends AppCompatActivity implements MainSettingsContract.View {
    MainSettingsContract.Presenter presenter;
    SharedPreferences settings;
    static final String SHARE_PREF_KEY_SOUND_ENABLED = "sound";
    static final String SHARE_PREF_KEY_MUSIC_ENABLED = "music";
    static final String SHARE_PREF_KEY_USER_NAME = "userName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);
        MainSettingsModel mModel = (MainSettingsModel) MainSettingsModel.getInstance();
        settings = getSharedPreferences("globalSettings",MODE_PRIVATE);
        System.out.println("restore state.");
        presenter = new MainSettingsPresenter(this, mModel);
        Switch soundSwitch = findViewById(R.id.soundEnabled);
        soundSwitch.setChecked(presenter.isSoundEnabled());
        Switch musicSwitch = findViewById(R.id.Music);
        musicSwitch.setChecked(presenter.isMusicEnabled());
        EditText editText = findViewById(R.id.userName);
        editText.setText(presenter.getUserName());

    }


    @Override
    protected void onDestroy() {
        presenter.viewDestroy();
        // Save global settings
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(SHARE_PREF_KEY_SOUND_ENABLED,presenter.isSoundEnabled());
        edit.putBoolean(SHARE_PREF_KEY_MUSIC_ENABLED,presenter.isMusicEnabled());
        edit.putString(SHARE_PREF_KEY_USER_NAME,presenter.getUserName());
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
    public void onUpdateUserName(View view) {
        EditText editText = findViewById(R.id.userName);
        presenter.setUserName(editText.getText().toString());
        editText.setCursorVisible(false);
        // Hides keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        // Show toast message
        Context context = getApplicationContext();
        CharSequence text = "User name is updated";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }
    public void onClickEdit(View view) {
        ((EditText)view).setCursorVisible(true);
    }
}
