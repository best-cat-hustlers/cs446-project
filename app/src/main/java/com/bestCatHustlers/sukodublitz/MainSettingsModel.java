package com.bestCatHustlers.sukodublitz;

class MainSettingsModel implements MainSettingsContract.Model  {
    private boolean sound = true;
    @Override
    public void setSound(boolean on) {
        sound = on;
    }
    public boolean getSound(){
        return sound;
    }
}
