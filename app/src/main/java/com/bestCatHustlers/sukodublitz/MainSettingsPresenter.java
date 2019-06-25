package com.bestCatHustlers.sukodublitz;

class MainSettingsPresenter implements MainSettingsContract.Presenter {
    private MainSettingsContract.View v;
    private MainSettingsContract.Model m;
    MainSettingsPresenter(MainSettingsContract.View view, MainSettingsContract.Model model) {
        v = view;
        m = model;
    }
    @Override
    public void turnSound(boolean on) {
        m.setSound(on);
        v.updateSound(on);
    }

    @Override
    public boolean getSound() {
        return m.getSound();
    }

    @Override
    public void viewDestroy() {
        v = null;
    }
}