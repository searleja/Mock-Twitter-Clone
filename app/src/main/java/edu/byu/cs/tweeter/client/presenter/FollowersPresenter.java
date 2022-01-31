package edu.byu.cs.tweeter.client.presenter;

import android.widget.TextView;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {

    private static final int PAGE_SIZE = 10;

    public interface View {
        void displayErrorMessage(String message);

        void setLoadingStatus(boolean value);

        void addFollowers(List<User> followers);

        void getUser(User user);
    }

    private View view;

    private boolean isLoading = false;
    private boolean hasMorePages;

    private User lastFollower;
    private FollowService followService;
    private UserService userService;

    public FollowersPresenter(View view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new GetFollowersObserver());
        }
    }

    public void getUser(TextView username) {
        userService.getUser(username, new GetUserObserver());
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


    public class GetFollowersObserver implements FollowService.GetFollowersObserver {

        @Override
        public void handleSuccess(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addFollowers(followers);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get followers: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get followers because of exception: " + exception.getMessage());
        }
    }

}
