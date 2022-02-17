package edu.byu.cs.tweeter.client.presenter.viewInterfaces;

import edu.byu.cs.tweeter.model.domain.User;

public interface AuthView extends View {
    void taskSucceeded(User user);
}
