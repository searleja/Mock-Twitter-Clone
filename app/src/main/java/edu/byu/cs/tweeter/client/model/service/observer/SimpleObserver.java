package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.presenter.viewInterfaces.View;

public abstract class SimpleObserver<T extends View> implements SimpleNotificationObserver {
    T view;

    public SimpleObserver(T view) {
        this.view = view;
    }

    @Override
    public void handleFailure(String message) {
        String prefix = getDescription();
        view.displayMessage("Failed to " + prefix + ": " + message);
    }

    @Override
    public void handleException(Exception exception) {
        String prefix = getDescription();
        view.displayMessage("Failed to " + prefix + " because of exception: " + exception.getMessage());
    }

    public abstract String getDescription();

}
