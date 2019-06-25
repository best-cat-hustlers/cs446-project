package com.bestCatHustlers.sukodublitz;

class MainSettingsModel implements MainSettingsContract.Model  {
    private boolean sound;

    MainSettingsModel(boolean sound) {
        this.sound = sound;
    }

    @Override
    public void setSound(boolean on) {
        sound = on;
    }
    public boolean getSound(){
        return sound;
    }
}
