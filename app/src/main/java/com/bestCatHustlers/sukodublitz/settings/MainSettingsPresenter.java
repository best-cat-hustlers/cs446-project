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
        m.setSoundEnabled(on);

    }

    @Override
    public boolean getSound() {
        return m.getSoundEnabled();
    }

    @Override
    public void turnMusic(boolean on) {
        m.setMusicEnabled(on);
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
