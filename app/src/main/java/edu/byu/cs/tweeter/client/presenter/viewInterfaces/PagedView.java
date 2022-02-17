package edu.byu.cs.tweeter.client.presenter.viewInterfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface PagedView<T> extends View {

    abstract void setLoading(boolean isLoading);

    abstract void addItems(List<T> items);

    abstract void navigateToUser(User user);
}
