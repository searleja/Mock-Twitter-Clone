package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.viewInterfaces.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    private PagedView view;

    public StoryPresenter(PagedView view, User targetUser) {
        super(view, targetUser, Cache.getInstance().getCurrUserAuthToken());
        this.view = view;
    }

    @Override
    public void getItems(AuthToken authToken, User targetUser, int pageSize, Status lastItem) {
        getStatusService().getStory(targetUser, authToken, pageSize, lastItem, new BasePagedObserver(view));
    }

    @Override
    public String getDescription() {
        return "get story";
    }

}
