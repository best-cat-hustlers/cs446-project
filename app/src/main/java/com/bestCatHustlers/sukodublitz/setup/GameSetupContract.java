package com.bestCatHustlers.sukodublitz.setup;

import android.content.Intent;

interface GameSetupContract {
    interface View {
    }
    interface Presenter{
        void initAiButtons();
        boolean isShowPoints();
        boolean isShowTimer();
        boolean isPenaltyOn();
        boolean isMultiplayerMode();
        int getAiDifficulty();
        void changeAiDifficulty(android.view.View view);
        void setShowPoints(boolean showPoints);
        void setShowTimer(boolean showTimer);
        void setPenaltyOn(boolean penaltyOn);
        void setMultiplayerMode(boolean multiplayerMode);
        void prepareLobbyExtras(Intent intent);
        void prepareGameExtras(Intent intent);
    }
    interface Model {
        boolean isShowPoints();
        boolean isShowTimer();
        boolean isPenaltyOn();
        boolean isMultiplayerMode();
        int getAiDifficulty();
        void setShowPoints(boolean showPoints);
        void setShowTimer(boolean showTimer);
        void setPenaltyOn(boolean penaltyOn);
        void setMultiplayerMode(boolean multiplayerMode);
        void setAiDifficulty(int aiDifficulty);
    }
}
