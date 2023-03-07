package edu.byu.cs.tweeter.client.presenter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class PostStatusTest {

    private StatusService mockStatusService;
    private MainPresenter.MainView mockView;

    private MainPresenter mainPresenterSpy;

    @Before
    public void setup() {
        //create mocks
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView, new User()));
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

    }

    private void messagesTest(Answer<Void> answer, String message) {
        Cache.getInstance().setCurrUserAuthToken(new AuthToken());
        //Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.isA(AuthToken.class), Mockito.isA(Status.class), Mockito.isA(MainPresenter.PostStatusObserver.class));
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());

        mainPresenterSpy.postStatus(new Status());
        Mockito.verify(mockView).displayMessage("Posting Status...");

        Mockito.verify(mockView).displayMessage(message);
    }

    @Test
    public void testPostStatus_successful() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertTrue(invocation.getArgument(0) instanceof AuthToken);
                assertTrue(invocation.getArgument(1) instanceof Status);
                assertNotNull(invocation.getArgument(2));


                MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);

                observer.handleSuccess(new Bundle());
                return null;
            }
        };
        messagesTest(answer, "Successfully Posted!");
    }



    @Test
    public void testPostStatus_failedWithMessage() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertTrue(invocation.getArgument(0) instanceof AuthToken);
                assertTrue(invocation.getArgument(1) instanceof Status);
                assertNotNull(invocation.getArgument(2));



                MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);

                observer.handleFailure("the error message");
                return null;
            }
        };
        messagesTest(answer, "Failed to post status: the error message");

    }

    @Test
    public void testPostStatus_failedWithException() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                assertTrue(invocation.getArgument(0) instanceof AuthToken);
                assertTrue(invocation.getArgument(1) instanceof Status);
                assertNotNull(invocation.getArgument(2));


                MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);

                observer.handleException(new Exception("the exception message"));
                return null;
            }
        };

        messagesTest(answer, "Failed to post status because of exception: the exception message");

    }

}
