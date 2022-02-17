package edu.byu.cs.tweeter.client.model.service.observer;

import android.os.Bundle;

public interface PageNotificationObserver extends ServiceObserver {
    void handleSuccess(Bundle data);
}
