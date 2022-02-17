package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PageNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PageNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public void getFeed(User user, int pageSize, Status lastStatus, PageNotificationObserver observer) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PageNotificationHandler(observer));
        BackgroundTaskUtils.runTask(getFeedTask);
    }

    public void getStory(User user, AuthToken authToken, int pageSize, Status lastStatus, PageNotificationObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(authToken,
                user, pageSize, lastStatus, new PageNotificationHandler(observer));
        BackgroundTaskUtils.runTask(getStoryTask);
    }

    public void postStatus(AuthToken authToken, Status newStatus, SimpleNotificationObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(authToken,
                newStatus, new SimpleNotificationHandler(observer));
        BackgroundTaskUtils.runTask(statusTask);
    }
}
