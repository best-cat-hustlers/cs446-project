package com.bestCatHustlers.sukodublitz.main;

class MainActivityModel implements MainActivityContract.Model {
    private MainActivityContract.Presenter presenter;
    MainActivityModel(MainActivityContract.Presenter p) {
        presenter = p;
    }
}
