package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterfaces.View;

public abstract class Presenter implements ServiceObserver {
    View view;

    private UserService userService;
    private StatusService statusService;
    private FollowService followService;

    public UserService getUserService() {
        if (userService == null)
            userService = new UserService();
        return userService;
    }

    public StatusService getStatusService() {
        if (statusService == null)
            statusService = new StatusService();
        return statusService;
    }

    public FollowService getFollowService() {
        if (followService == null)
            followService = new FollowService();
        return followService;
    }

    public Presenter(View view) {
        this.view = view;
    }

    @Override
    public void handleFailure(String message) {
        view.displayMessage(" Service failed: " + message);
    }

    @Override
    public void handleException(Exception ex) {
        view.displayMessage(" Service threw exception: " + ex.getMessage());
    }

    public abstract String getDescription();
}
