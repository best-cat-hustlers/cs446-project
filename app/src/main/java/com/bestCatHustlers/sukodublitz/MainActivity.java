package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity{
    MainActivityPresenter mainPresenter;
    private static final int SOUND_ON_OFF = 0;
    boolean Sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mainPresenter = new MainActivityPresenter(this);
        Sound = true;
    }

    public void clickSinglePlayer(View view) {
        mainPresenter.goToSinglePlayer(view);
    }

    public void clickMultiplayer(View view) {
        mainPresenter.goToMultiplayer(view);
    }

    public void clickSettings(View view) {
        Intent intent = new Intent(this, MainSettingsActivity.class);
        intent.putExtra("Sound", Sound);
        startActivityForResult(intent, SOUND_ON_OFF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Need to somehow store the data (sound)
        if (requestCode == 0) {
            if (resultCode == RESULT_OK){
                Sound = data.getExtras().getBoolean("Sound");
            }
        }
    }
}
