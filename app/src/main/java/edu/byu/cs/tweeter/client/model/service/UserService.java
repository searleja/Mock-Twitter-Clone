package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PageNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PageNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserService {

    public void getUser(String username, PageNotificationObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                username, new PageNotificationHandler(observer));
        BackgroundTaskUtils.runTask(getUserTask);
    }

    public void login(String username, String password, SimpleNotificationObserver observer) {
        LoginTask loginTask = new LoginTask(username,
                password, new SimpleNotificationHandler(observer));
        BackgroundTaskUtils.runTask(loginTask);
    }

    public void logout(SimpleNotificationObserver observer, AuthToken authToken) {
        LogoutTask logoutTask = new LogoutTask(authToken, new SimpleNotificationHandler(observer));
        BackgroundTaskUtils.runTask(logoutTask);
    }

    public void register(String firstName, String lastName, String alias, String password,
                         String imageBytesBase64, SimpleNotificationObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new SimpleNotificationHandler(observer));

        BackgroundTaskUtils.runTask(registerTask);
    }
}
