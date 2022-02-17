package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.PageNotificationObserver;

public class PageNotificationHandler extends BackgroundTaskHandler<PageNotificationObserver>{

    public PageNotificationHandler(PageNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(PageNotificationObserver observer, Bundle data) {
        observer.handleSuccess(data);
    }
}
