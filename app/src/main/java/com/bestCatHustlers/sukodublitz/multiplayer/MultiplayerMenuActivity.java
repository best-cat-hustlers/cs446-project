package com.bestCatHustlers.sukodublitz.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bestCatHustlers.sukodublitz.GameSetupActivity;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.join.JoinActivity;

public class MultiplayerMenuActivity extends AppCompatActivity implements MultiplayerMenuContract.View {

    private MultiplayerMenuContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multiplayer_menu);

        presenter = new MultiplayerMenuPresenter(this);
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }

    public void openGameSetup(View view) {
        Intent intent = new Intent(this, GameSetupActivity.class);
        presenter.prepareOpenGameSetup(intent);
        startActivity(intent);
    }

    public void openJoinActivity(View view) {
        Intent intent = new Intent(this, JoinActivity.class);

        startActivity(intent);
    }
}
