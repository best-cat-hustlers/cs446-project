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
    public void updateDifficulty(int d) {
        // Show difficulty
        Intent intent = new Intent();
        intent.putExtra("Difficulty", d);
        setResult(Activity.RESULT_OK, intent);
    }
}
