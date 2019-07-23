package com.bestCatHustlers.sukodublitz.setup;

import android.view.View;
import android.view.ViewGroup;

import com.bestCatHustlers.sukodublitz.R;

public class GameSetupPresenter implements GameSetupContract.Presenter {
    private GameSetupActivity view;
    private static final int topToBottomMarginRatio = 4;

    GameSetupPresenter(GameSetupActivity gameSetupActivity) {
        view = gameSetupActivity;
    }

    @Override
    public void hideAIDifficultyOptions() {
        // Hide AI difficulty options
        View aiGroupView = view.findViewById(R.id.AI_difficulty);
        View aiTitle = view.findViewById(R.id.textView);
        View bottomView = view.findViewById(R.id.penalty_switch);
        aiGroupView.setVisibility(View.GONE);
        aiTitle.setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) bottomView.getLayoutParams();
        params.bottomMargin = params.topMargin * topToBottomMarginRatio;
    }
}
