package edu.byu.cs.tweeter.client.presenter;

import static edu.byu.cs.tweeter.client.model.service.backgroundTask.GetCountTask.COUNT_KEY;
import static edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask.IS_FOLLOWER_KEY;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterfaces.View;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter {

    public interface MainView extends View {
        void logout();

        void updateFollowerCount(int count);

        void updateFollowingCount(int count);

        void updateFollowing(boolean updateButton);
    }

    private MainView mainView;
    private User selectedUser;


    public MainPresenter(MainView mainView, User selectedUser) {
        super(mainView);
        this.mainView = mainView;
        this.selectedUser = selectedUser;
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
        getUserService().logout(new LogoutObserver(mainView), Cache.getInstance().getCurrUserAuthToken());
    }

    public void updateSelectedUserFollowingAndFollowers() {
        getFollowService().updateSelectedUserFollowingAndFollowers(selectedUser, Cache.getInstance().getCurrUserAuthToken(),
                new GetFollowersCountObserver(mainView), new GetFollowingCountObserver(mainView));
    }

    public void follow(boolean isFollowing) {
        if (!isFollowing)
            getFollowService().follow(selectedUser, Cache.getInstance().getCurrUserAuthToken(), new FollowObserver(mainView), isFollowing);
        else
            getFollowService().follow(selectedUser, Cache.getInstance().getCurrUserAuthToken(), new UnfollowObserver(mainView), isFollowing);
    }

    public void isFollowerTask() {
        getFollowService().isFollowerTask(Cache.getInstance().getCurrUser(), selectedUser,
                Cache.getInstance().getCurrUserAuthToken(), new IsFollowerObserver(mainView));
    }

    public void postStatus(Status newStatus) {
        view.displayMessage("Posting Status...");
        getStatusService().postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus, new PostStatusObserver(mainView));
    }

    @Override
    public String getDescription() {
        return null;
    }

    public class LogoutObserver extends SimpleObserver {

        public LogoutObserver(View view) {
            super(view);
        }

        @Override
        public void handleSuccess(Bundle data) {
            mainView.logout();
        }

        @Override
        public String getDescription() {
            return "logout";
        }
    }

    public class GetFollowersCountObserver extends SimpleObserver {

        public GetFollowersCountObserver(View view) {
            super(view);
        }

        @Override
        public void handleSuccess(Bundle data) {
            int count = data.getInt(COUNT_KEY);
            mainView.updateFollowerCount(count);
        }

        @Override
        public String getDescription() {
            return "get followers count";
        }
    }

    public class GetFollowingCountObserver extends SimpleObserver {

        public GetFollowingCountObserver(View view) {
            super(view);
        }

        @Override
        public void handleSuccess(Bundle data) {
            int count = data.getInt(COUNT_KEY);
            mainView.updateFollowingCount(count);
        }

        @Override
        public String getDescription() {
            return "get following count";
        }
    }

    public class UnfollowObserver extends SimpleObserver {

        public UnfollowObserver(View view) {
            super(view);
        }

        @Override
        public void handleSuccess(Bundle data) {
            updateSelectedUserFollowingAndFollowers();
            mainView.updateFollowing(true);
        }

        @Override
        public String getDescription() {
            return "unfollow";
        }
    }

    public class FollowObserver extends SimpleObserver {

        public FollowObserver(View view) {
            super(view);
        }

        @Override
        public void handleSuccess(Bundle data) {
            updateSelectedUserFollowingAndFollowers();
            mainView.updateFollowing(false);
        }

        @Override
        public String getDescription() {
            return "follow";
        }
    }

    public class IsFollowerObserver extends SimpleObserver {

        public IsFollowerObserver(View view) {
            super(view);
        }

        @Override
        public void handleSuccess(Bundle data) {
            boolean isFollower = data.getBoolean(IS_FOLLOWER_KEY);
            mainView.updateFollowing(isFollower);
        }

        @Override
        public String getDescription() {
            return "determine following relationship";
        }
    }

    public class PostStatusObserver extends SimpleObserver {

        public PostStatusObserver(View view) {
            super(view);
        }

        @Override
        public String getDescription() {
            return "post status";
        }

        @Override
        public void handleSuccess(Bundle data) {
            mainView.displayMessage("Successfully Posted!");
        }
    }
}
