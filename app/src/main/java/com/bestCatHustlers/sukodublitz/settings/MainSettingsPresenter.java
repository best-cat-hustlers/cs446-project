package com.bestCatHustlers.sukodublitz.settings;

class MainSettingsPresenter implements MainSettingsContract.Presenter {
    private MainSettingsContract.View v;
    private MainSettingsModel m;
    MainSettingsPresenter(MainSettingsContract.View view, MainSettingsModel model) {
        v = view;
        m = model;
    }
    @Override
    public void setSoundEnabled(boolean newValue) {
        m.setSoundEnabled(newValue);

    }

    @Override
    public boolean isSoundEnabled() {
        return m.isSoundEnabled();
    }

    @Override
    public void setMusicEnabled(boolean newValue) {
        m.setMusicEnabled(newValue);
    }

    @Override
    public boolean isMusicEnabled() {
        return m.isMusicEnabled();
    }

    @Override
    public void viewDestroy() {
        v = null;
    }
}
