package com.bestCatHustlers.sukodublitz.settings;

class MainSettingsPresenter implements MainSettingsContract.Presenter {
    private MainSettingsContract.View v;
    private MainSettingsModel m;
    MainSettingsPresenter(MainSettingsContract.View view, MainSettingsModel model) {
        v = view;
        m = model;
    }
    @Override
    public void turnSound(boolean on) {
        m.setSound(on);

    }

    @Override
    public boolean getSound() {
        return m.getSound();
    }

    @Override
    public void turnMusic(boolean on) {
        m.setMusic(on);
    }

    @Override
    public boolean getMusic() {
        return m.getMusic();
    }

    @Override
    public void viewDestroy() {
        v = null;
    }
}
