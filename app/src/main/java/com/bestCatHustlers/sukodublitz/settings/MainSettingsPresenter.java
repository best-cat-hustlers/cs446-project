package com.bestCatHustlers.sukodublitz.settings;

class MainSettingsPresenter implements MainSettingsContract.Presenter {
    private MainSettingsContract.View view;
    private MainSettingsModel model;
    MainSettingsPresenter(MainSettingsContract.View view, MainSettingsModel model) {
        this.view = view;
        this.model = model;
    }
    @Override
    public void setSoundEnabled(boolean newValue) {
        model.setSoundEnabled(newValue);

    }

    @Override
    public boolean isSoundEnabled() {
        return model.isSoundEnabled();
    }

    @Override
    public void setMusicEnabled(boolean newValue) {
        model.setMusicEnabled(newValue);
    }

    @Override
    public boolean isMusicEnabled() {
        return model.isMusicEnabled();
    }

    @Override
    public void viewDestroy() {
        view = null;
    }

    @Override
    public void setUserName(String text) {
        model.setUserName(text);
    }

    @Override
    public String getUserName() {
        return model.getUserName();
    }

}
