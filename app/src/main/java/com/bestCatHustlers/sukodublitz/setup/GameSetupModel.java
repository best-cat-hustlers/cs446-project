package com.bestCatHustlers.sukodublitz.setup;

public class GameSetupModel implements GameSetupContract.Model {
    // Default settings
    private boolean showPoints = true;
    private boolean showTimer = true;
    private boolean penaltyOn = true;
    private boolean multiplayerMode;
    private int aiDifficulty = 1;

    GameSetupModel() {
    }

    public boolean isShowPoints() {
        return showPoints;
    }

    public void setShowPoints(boolean showPoints) {
        this.showPoints = showPoints;
    }

    public void setShowTimer(boolean showTimer) {
        this.showTimer = showTimer;
    }

    public void setPenaltyOn(boolean penaltyOn) {
        this.penaltyOn = penaltyOn;
    }

    public void setMultiplayerMode(boolean multiplayerMode) {
        this.multiplayerMode = multiplayerMode;
    }

    public void setAiDifficulty(int aiDifficulty) {
        this.aiDifficulty = aiDifficulty;
    }

    public boolean isShowTimer() {
        return showTimer;
    }

    public boolean isPenaltyOn() {
        return penaltyOn;
    }

    public boolean isMultiplayerMode() {
        return multiplayerMode;
    }

    public int getAiDifficulty() {
        return aiDifficulty;
    }

}
