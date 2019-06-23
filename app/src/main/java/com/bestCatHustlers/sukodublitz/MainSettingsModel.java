package com.bestCatHustlers.sukodublitz;

class MainSettingsModel implements MainSettingsContract.Model  {
    private int difficulty = 0;
    @Override
    public void setDifficulty(int d) {
        difficulty = d;
    }
    public int getDifficulty(){
        return difficulty;
    }
}
