package edu.byu.cs.tweeter.client.model.service.observer;

import android.os.Bundle;

public interface SimpleNotificationObserver extends ServiceObserver {
    void handleSuccess(Bundle data);
}
