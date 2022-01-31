package edu.byu.cs.tweeter.client.presenter;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {

    public interface View {
        void displayMessage(String message);

        void logout();

        void updateFollowerCount(int count);

        void updateFollowingCount(int count);

        void updateFollowing(boolean updateButton);

        void updateFollowBtn(boolean isFollowing);
    }

    private View view;
    private User selectedUser;

    private FollowService followService;
    private UserService userService;
    private StatusService statusService;

    public MainPresenter(View view, User selectedUser) {
        this.view = view;
        this.selectedUser = selectedUser;
        followService = new FollowService();
        userService = new UserService();
        statusService = new StatusService();
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public void logout() {
        userService.logout(new LogoutObserver(), Cache.getInstance().getCurrUserAuthToken());
    }

    public void updateSelectedUserFollowingAndFollowers() {
        followService.updateSelectedUserFollowingAndFollowers(selectedUser, Cache.getInstance().getCurrUserAuthToken(),
                new GetFollowersCountObserver(), new GetFollowingCountObserver());
    }

    public void follow(boolean isFollowing) {
        followService.follow(selectedUser, Cache.getInstance().getCurrUserAuthToken(), new ChangeFollowingObserver(), isFollowing);
    }

    public void isFollowerTask() {
        followService.isFollowerTask(Cache.getInstance().getCurrUser(), selectedUser,
                Cache.getInstance().getCurrUserAuthToken(), new IsFollowerObserver());
    }

    public void postStatus(Status newStatus) {
        statusService.postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus, new PostStatusObserver());
    }

    public class LogoutObserver implements UserService.LogoutObserver {

        @Override
        public void handleSuccess() {
            view.logout();
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to logout because of exception: " + exception.getMessage());
        }
    }

    public class GetFollowersCountObserver implements FollowService.GetFollowersCountObserver {

        @Override
        public void handleSuccess(int count) {
            view.updateFollowerCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to get followers count because of exception: " + exception.getMessage());
        }
    }

    public class GetFollowingCountObserver implements FollowService.GetFollowingCountObserver {

        @Override
        public void handleSuccess(int count) {
            view.updateFollowingCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to get following count because of exception: " + exception.getMessage());
        }
    }

    public class ChangeFollowingObserver implements FollowService.ChangeFollowingObserver {

        @Override
        public void handleSuccess(boolean updateButton) {
            followService.updateSelectedUserFollowingAndFollowers(selectedUser, Cache.getInstance().getCurrUserAuthToken(),
                    new GetFollowersCountObserver(), new GetFollowingCountObserver());
            view.updateFollowing(updateButton);
        }

        @Override
        public void handleFailure(String message, boolean updateButton) {
            if (updateButton)
                view.displayMessage("Failed to unfollow: " + message);
            else
                view.displayMessage("Failed to follow: " + message);
        }

        @Override
        public void handleException(Exception exception, boolean updateButton) {
            if (updateButton)
                view.displayMessage("Failed to unfollow because of exception: " + exception.getMessage());
            else
                view.displayMessage("Failed to follow because of exception: " + exception.getMessage());
        }
    }

    public class IsFollowerObserver implements FollowService.IsFollowerObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            view.updateFollowing(isFollower);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to determine following relationship because of exception: " + exception.getMessage());
        }
    }

    public class PostStatusObserver implements StatusService.PostStatusObserver {

        @Override
        public void handleSuccess(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to post status because of exception: " + exception.getMessage());
        }
    }
}
