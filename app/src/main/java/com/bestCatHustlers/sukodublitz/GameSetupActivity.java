package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bestCatHustlers.sukodublitz.game.GameActivity;

public class GameSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
    }
    public void clickGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
    }
    public void clickDifficulty(View view){
        Button diffButton = (Button) view;
        changeHighlightDifficulty(diffButton.getId());

    }
    private void changeHighlightDifficulty(int id) {
        int [] difficulties = {R.id.diff1, R.id.diff2, R.id.diff3, R.id.diff4, R.id.diff5};
        for (int i = 0; i < difficulties.length; i++){
            Button b = findViewById(difficulties[i]);
            if (id == difficulties[i]){
                b.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
            } else {
                b.setBackgroundResource(0);
            }
        }
    }
    public void onBackPressed(View view){
        super.onBackPressed();
    }
}
