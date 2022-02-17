package edu.byu.cs.tweeter.client.presenter;

import static edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask.ITEMS_KEY;
import static edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask.MORE_PAGES_KEY;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterfaces.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {
    private int pageSize = 10;
    private User targetUser;
    private AuthToken authToken;
    private T lastItem;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private boolean isGettingUser = false;

    private PagedView view;

    public PagedPresenter(PagedView<User> view, User targetUser,
                          AuthToken authToken, T lastItem, boolean hasMorePages,
                          boolean isLoading, boolean isGettingUser) {
        super(view);
        this.view = view;
        this.targetUser = targetUser;
        this.authToken = authToken;
        this.lastItem = lastItem;
        this.hasMorePages = hasMorePages;
        this.isLoading = isLoading;
        this.isGettingUser = isGettingUser;
    }

    public PagedPresenter(PagedView view, User targetUser, AuthToken authToken) {
        super(view);
        this.view = view;
        this.targetUser = targetUser;
        this.authToken = authToken;
    }

    public void loadMoreItems() {
        if (!isLoading) {
            isLoading = true;
            view.setLoading(true);
            getItems(authToken, targetUser, pageSize, lastItem);
        }
    }

    public void getUser(String alias) {
        isGettingUser = true;
        new UserService().getUser(alias, new GetUserObserver(view));
        isGettingUser = false;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public abstract void getItems(AuthToken authToken, User targetUser, int pageSize, T lastItem);


    public class BasePagedObserver extends PageObserver {

        public BasePagedObserver(PagedView view) {
            super(view);
        }

        @Override
        public void handleSuccess(Bundle data) {
            List<User> items = (List<User>) data.getSerializable(ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(MORE_PAGES_KEY);

            isLoading = false;
            //setLoading(false);
            view.setLoading(false);
            lastItem = (items.size() > 0) ? (T) items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(items);
        }

        @Override
        public void handleFailure(String message) {
            setLoading(false);
            super.handleFailure(message);
        }

        @Override
        public void handleException(Exception exception) {
            setLoading(false);
            super.handleException(exception);
        }


        @Override
        public String getDescription() {
            return null;
        }
    }
}
