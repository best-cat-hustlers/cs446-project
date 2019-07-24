package com.bestCatHustlers.sukodublitz.setup;

import android.content.Intent;
import android.widget.RadioButton;

import com.bestCatHustlers.sukodublitz.ExtrasKeys;
import com.bestCatHustlers.sukodublitz.R;

public class GameSetupPresenter implements GameSetupContract.Presenter {
    private GameSetupActivity gameSetupActivity;
    private GameSetupContract.Model model;

    //AI buttons
    private RadioButton aiDifficulty1Button;
    private RadioButton aiDifficulty2Button;
    private RadioButton aiDifficulty3Button;
    private RadioButton aiDifficulty4Button;
    private RadioButton aiDifficulty5Button;

    GameSetupPresenter(GameSetupActivity gameSetupActivity) {
        this.gameSetupActivity = gameSetupActivity;
        model = new GameSetupModel();
    }

    @Override
    public void initAiButtons() {
        assert aiDifficulty1Button == null;
        // Single player mode
        aiDifficulty1Button = gameSetupActivity.findViewById(R.id.difficulty1);
        aiDifficulty2Button = gameSetupActivity.findViewById(R.id.difficulty2);
        aiDifficulty3Button = gameSetupActivity.findViewById(R.id.difficulty3);
        aiDifficulty4Button = gameSetupActivity.findViewById(R.id.difficulty4);
        aiDifficulty5Button = gameSetupActivity.findViewById(R.id.difficulty5);

        // Default radio button aiDifficulty to 1
        aiDifficulty1Button.toggle();
    }

    @Override
    public boolean isShowPoints() {
        return model.isShowPoints();
    }

    @Override
    public boolean isShowTimer() {
        return model.isShowTimer();
    }

    @Override
    public boolean isPenaltyOn() {
        return model.isPenaltyOn();
    }

    @Override
    public boolean isMultiplayerMode() {
        return model.isMultiplayerMode();
    }

    @Override
    public int getAiDifficulty() {
        return model.getAiDifficulty();
    }

    private void resetRadioButtonTextColor() {
        int color = gameSetupActivity.getResources().getColor(R.color.secondaryColor);
        aiDifficulty1Button.setTextColor(color);
        aiDifficulty2Button.setTextColor(color);
        aiDifficulty3Button.setTextColor(color);
        aiDifficulty4Button.setTextColor(color);
        aiDifficulty5Button.setTextColor(color);
    }

    @Override
    public void changeAiDifficulty(android.view.View view) {
        RadioButton selectedRadioButton = (RadioButton) view;
        boolean checked = selectedRadioButton.isChecked();

        resetRadioButtonTextColor();
        selectedRadioButton.setTextColor(gameSetupActivity.getResources().getColor(R.color.white));

        switch(view.getId()) {
            case R.id.difficulty1:
                if (checked)
                    // set some global variable, then send to model when start game is clicked
                    model.setAiDifficulty(1);
                break;
            case R.id.difficulty2:
                if (checked)
                    model.setAiDifficulty(2);
                break;
            case R.id.difficulty3:
                if (checked)
                    model.setAiDifficulty(3);
                break;
            case R.id.difficulty4:
                if (checked)
                    model.setAiDifficulty(4);
                break;
            case R.id.difficulty5:
                if (checked)
                    model.setAiDifficulty(5);
                break;
        }
    }

    @Override
    public void setShowPoints(boolean showPoints) {
        model.setShowPoints(showPoints);
    }

    @Override
    public void setShowTimer(boolean showTimer) {
        model.setShowTimer(showTimer);
    }

    @Override
    public void setPenaltyOn(boolean penaltyOn) {
        model.setPenaltyOn(penaltyOn);
    }

    @Override
    public void setMultiplayerMode(boolean multiplayerMode) {
        model.setMultiplayerMode(multiplayerMode);
    }

    @Override
    public void prepareLobbyExtras(Intent intent) {
        intent.putExtra(ExtrasKeys.IS_HOST, true);
        intent.putExtra(ExtrasKeys.IS_MULTIPLAYER, true);
        intent.putExtra(ExtrasKeys.SHOULD_SHOW_POINTS, isShowPoints());
        intent.putExtra(ExtrasKeys.SHOULD_SHOW_TIMER, isShowTimer());
        intent.putExtra(ExtrasKeys.SHOULD_USE_PENALTY, isPenaltyOn());
    }

    @Override
    public void prepareGameExtras(Intent intent) {
        intent.putExtra(ExtrasKeys.IS_MULTIPLAYER, false);
        intent.putExtra(ExtrasKeys.SHOULD_SHOW_POINTS, isShowPoints());
        intent.putExtra(ExtrasKeys.SHOULD_SHOW_TIMER, isShowTimer());
        intent.putExtra(ExtrasKeys.SHOULD_USE_PENALTY, isPenaltyOn());
        intent.putExtra(ExtrasKeys.AI_DIFFICULTY, getAiDifficulty());
    }
}
