package com.bestCatHustlers.sukodublitz;

class MainSettingsPresenter implements MainSettingsContract.Presenter {
    private MainSettingsContract.View v;
    private MainSettingsContract.Model m;
    MainSettingsPresenter(MainSettingsContract.View view, MainSettingsContract.Model model) {
        v = view;
        m = model;
    }
    @Override
    public void changeDifficulty(int d) {
        m.setDifficulty(d);
        v.updateDifficulty(d);
    }
}
