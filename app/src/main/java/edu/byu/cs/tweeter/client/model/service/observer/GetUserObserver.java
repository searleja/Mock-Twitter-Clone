package edu.byu.cs.tweeter.client.model.service.observer;

import static edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask.USER_KEY;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.presenter.viewInterfaces.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserObserver<T extends PagedView<User>> extends PageObserver<PagedView<User>> {

    PagedView<User> view;

    public GetUserObserver(PagedView<User> view) {
        super(view);
        this.view = view;
    }

    public void handleSuccess(Bundle data) {
        User user = (User) data.getSerializable(USER_KEY);
        view.navigateToUser(user);
    }

    @Override
    public String getDescription() {
        return "user's profile";
    }
}