package com.bestCatHustlers.sukodublitz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainSettingsActivity extends AppCompatActivity implements MainSettingsContract.View {
    MainSettingsContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_settings);
        presenter = new MainSettingsPresenter(this,new MainSettingsModel());
    }

    @Override
    public void updateSound(boolean on) {
        // Show sound

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
}
