package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.viewInterfaces.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {
    private PagedView view;

    public FollowingPresenter(PagedView view, User targetUser) {
        super(view, targetUser, Cache.getInstance().getCurrUserAuthToken());
        this.view = view;
    }

    @Override
    public void getItems(AuthToken authToken, User targetUser, int pageSize, User lastItem) {
        getFollowService().getFollowing(authToken, targetUser, pageSize, lastItem, new BasePagedObserver(view));
    }

    @Override
    public String getDescription() {
        return "get following";
    }

}
