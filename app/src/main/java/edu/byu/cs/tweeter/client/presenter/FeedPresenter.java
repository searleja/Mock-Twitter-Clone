package edu.byu.cs.tweeter.client.presenter;

import android.widget.TextView;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {

    private static final int PAGE_SIZE = 10;

    public interface View {
        void displayErrorMessage(String message);

        void getUser(User user);

        void setLoadingStatus(boolean value);

        void addStatuses(List<Status> statuses);
    }

    private View view;
    private UserService userService;
    private StatusService statusService;

    private Status lastStatus;

    private boolean hasMorePages;
    private boolean isLoading = false;

    public FeedPresenter(View view) {
        this.view = view;
        userService = new UserService();
        statusService = new StatusService();
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void getUser(TextView username) {
        userService.getUser(username, new GetUserObserver());
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            statusService.getFeed(user, PAGE_SIZE, lastStatus, new GetFeedObserver());
        }
    }


    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            view.getUser(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }

    public class GetFeedObserver implements StatusService.GetFeedObserver {

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addStatuses(statuses);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get feed: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get feed because of exception: " + exception.getMessage());
        }
    }
}
