package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PageNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PageNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, PageNotificationObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new PageNotificationHandler(observer));
        BackgroundTaskUtils.runTask(getFollowersTask);
    }

    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, PageNotificationObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new PageNotificationHandler(observer));
        BackgroundTaskUtils.runTask(getFollowingTask);
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser, AuthToken authToken,
                                                        SimpleNotificationObserver followerCount, SimpleNotificationObserver followingCount) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                selectedUser, new SimpleNotificationHandler(followerCount));
        BackgroundTaskUtils.runTask(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(followingCount));
        BackgroundTaskUtils.runTask(followingCountTask);
    }

    public void follow(User selectedUser, AuthToken authToken, SimpleNotificationObserver observer, boolean changeButton) {
        if (changeButton) {
            FollowTask followTask = new FollowTask(authToken,
                    selectedUser, new SimpleNotificationHandler(observer));
            BackgroundTaskUtils.runTask(followTask);
        } else {
            UnfollowTask unfollowTask = new UnfollowTask(authToken,
                    selectedUser, new SimpleNotificationHandler(observer));
            BackgroundTaskUtils.runTask(unfollowTask);
        }
    }

    public void isFollowerTask(User currUser, User selectedUser, AuthToken authToken, SimpleNotificationObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
                currUser, selectedUser, new SimpleNotificationHandler(observer));
        BackgroundTaskUtils.runTask(isFollowerTask);
    }
}
