package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    private boolean isFollowing;


    public interface GetFollowingObserver {
        void handleSuccess(List<User> followees, boolean hasMorePages);

        void handleFailure(String message);

        void handleException(Exception exception);
    }

    public interface GetFollowingCountObserver {
        void handleSuccess(int count);

        void handleFailure(String message);

        void handleException(Exception exception);
    }

    public interface GetFollowersObserver {
        void handleSuccess(List<User> followers, boolean hasMorePages);

        void handleFailure(String message);

        void handleException(Exception exception);
    }

    public interface GetFollowersCountObserver {
        void handleSuccess(int count);

        void handleFailure(String message);

        void handleException(Exception exception);
    }

    public interface ChangeFollowingObserver {
        void handleSuccess(boolean updateButton);

        void handleFailure(String message, boolean updateButton);

        void handleException(Exception exception, boolean updateButton);
    }

    public interface IsFollowerObserver {
        void handleSuccess(boolean isFollower);

        void handleFailure(String message);

        void handleException(Exception exception);
    }

    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, GetFollowersObserver getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowersHandler(getFollowersObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, GetFollowingObserver getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(getFollowingObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser, AuthToken authToken,
                                                        GetFollowersCountObserver followerCount, GetFollowingCountObserver followingCount) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                selectedUser, new GetFollowersCountHandler(followerCount));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(followingCount));
        executor.execute(followingCountTask);
    }

    public void follow(User selectedUser, AuthToken authToken, ChangeFollowingObserver observer, boolean changeButton) {
        this.isFollowing = changeButton;
        if (changeButton) {
            FollowTask followTask = new FollowTask(authToken,
                    selectedUser, new ChangeFollowingHandler(observer));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(followTask);
        } else {
            UnfollowTask unfollowTask = new UnfollowTask(authToken,
                    selectedUser, new ChangeFollowingHandler(observer));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(unfollowTask);

        }
    }

    public void isFollowerTask(User currUser, User selectedUser, AuthToken authToken, IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken,
                currUser, selectedUser, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends Handler {
        private GetFollowersObserver observer;

        public GetFollowersHandler(GetFollowersObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
                observer.handleSuccess(followers, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }

    // GetFollowersCountHandler

    private class GetFollowersCountHandler extends Handler {
        private GetFollowersCountObserver observer;

        public GetFollowersCountHandler(GetFollowersCountObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
                observer.handleSuccess(count);
            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends Handler {
        private GetFollowingObserver observer;

        public GetFollowingHandler(GetFollowingObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                observer.handleSuccess(followees, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }

    // GetFollowingCountHandler

    private class GetFollowingCountHandler extends Handler {
        GetFollowingCountObserver observer;

        public GetFollowingCountHandler(GetFollowingCountObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
                observer.handleSuccess(count);
            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }


    // FollowHandler

    private class ChangeFollowingHandler extends Handler {
        private ChangeFollowingObserver observer;

        public ChangeFollowingHandler(ChangeFollowingObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                observer.handleSuccess(isFollowing);
            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
                observer.handleFailure(message, isFollowing);
            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
                observer.handleException(ex, isFollowing);
            }
        }
    }


    // IsFollowerHandler

    private class IsFollowerHandler extends Handler {
        private IsFollowerObserver observer;

        public IsFollowerHandler(IsFollowerObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
                observer.handleSuccess(isFollower);
            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }


}
