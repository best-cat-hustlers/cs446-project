package com.bestCatHustlers.sukodublitz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

public class MainSettingsActivity extends AppCompatActivity implements MainSettingsContract.View {
    MainSettingsContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean Sound = getIntent().getExtras().getBoolean("Sound");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);
        presenter = new MainSettingsPresenter(this,new MainSettingsModel(Sound));
        Switch soundSwitch = findViewById(R.id.sound);
        soundSwitch.setChecked(presenter.getSound());
    }

    @Override
    public void updateSound(boolean on) {
        // Back to parent activity
        Intent intent = new Intent();
        intent.putExtra("Sound", on);
        setResult(Activity.RESULT_OK, intent);
    }
    public void onSoundClick(){
        boolean b = presenter.getSound();
        presenter.turnSound(b);
    }

    @Override
    protected void onDestroy() {
        presenter.viewDestroy();
        super.onDestroy();
    }

    public void onCheck(View view) {
        Switch soundSwitch = (Switch) view;
        presenter.turnSound(soundSwitch.isChecked());

    }
}
