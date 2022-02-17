package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.presenter.viewInterfaces.View;

public abstract class Observer<T extends View> implements ServiceObserver {

    private T view;

    public Observer(T view) {
        this.view = view;
    }

    @Override
    public void handleFailure(String message) {
        String prefix = getDescription();
        view.displayMessage("Failed to get " + prefix + ": " + message);
    }

    @Override
    public void handleException(Exception exception) {
        String prefix = getDescription();
        view.displayMessage("Failed to get " + prefix + ": " + exception.getMessage());
    }

    public abstract String getDescription();
}