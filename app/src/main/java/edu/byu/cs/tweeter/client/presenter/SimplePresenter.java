package edu.byu.cs.tweeter.client.presenter;

import static edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenticateTask.AUTH_TOKEN_KEY;
import static edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask.USER_KEY;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterfaces.AuthView;
import edu.byu.cs.tweeter.client.presenter.viewInterfaces.View;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class SimplePresenter extends Presenter {

    AuthView view;

    public SimplePresenter(AuthView view) {
        super(view);
        this.view = view;
    }

    public class AuthObserver<T extends View> extends SimpleObserver {

        public AuthObserver(T view) {
            super(view);
        }

        @Override
        public void handleSuccess(Bundle data) {
            User user = (User) data.getSerializable(USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(AUTH_TOKEN_KEY);

            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            view.displayMessage("Hello " + user.getName());
            view.taskSucceeded(user);
        }

        @Override
        public String getDescription() {
            return null;
        }
    }
}
