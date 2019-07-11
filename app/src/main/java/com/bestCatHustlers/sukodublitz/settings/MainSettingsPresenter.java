package com.bestCatHustlers.sukodublitz.settings;

class MainSettingsPresenter implements MainSettingsContract.Presenter {
    private MainSettingsContract.View v;
    private MainSettingsModel m;
    MainSettingsPresenter(MainSettingsContract.View view, MainSettingsModel model) {
        v = view;
        m = model;
    }
    @Override
    public void turnSound(boolean newValue) {
        m.setSoundEnabled(newValue);

    }

    @Override
    public boolean getSound() {
        return m.getSoundEnabled();
    }

    @Override
    public void turnMusic(boolean newValue) {
        m.setMusicEnabled(newValue);
    }

    @Override
    public boolean getMusic() {
        return m.getMusicEnabled();
    }

    @Override
    public void viewDestroy() {
        v = null;
    }
}
