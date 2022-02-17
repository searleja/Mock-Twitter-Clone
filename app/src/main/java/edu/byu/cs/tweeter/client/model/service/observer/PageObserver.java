package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.presenter.viewInterfaces.PagedView;
import edu.byu.cs.tweeter.client.presenter.viewInterfaces.View;

public abstract class PageObserver<T extends View> implements PageNotificationObserver {

    PagedView view;

    public PageObserver(PagedView view) {
        this.view = view;
    }

    @Override
    public void handleFailure(String message) {
        view.setLoading(false);
        String prefix = getDescription();
        view.displayMessage("Failed to get " + prefix + ": " + message);
    }

    @Override
    public void handleException(Exception exception) {
        view.setLoading(false);
        String prefix = getDescription();
        view.displayMessage("Failed to get " + prefix + " because of exception: " + exception.getMessage());
    }

    public abstract String getDescription();

}
