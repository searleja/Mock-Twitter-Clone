package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;


public class LoginPresenter {

    public interface View {
        void loginSuccessful(User user, AuthToken authToken);

        void displayMessage(String message);
    }

    private View view;
    private UserService userService;

    public LoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void validateLogin(EditText username, EditText password) {
        if (username.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (username.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public void InitiateLogin(String username, String password) {
        userService.login(username, password, new LoginObserver());
    }

    public class LoginObserver implements UserService.LoginObserver {

        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            view.displayMessage("Hello " + user.getName());
            view.loginSuccessful(user, authToken);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to login because of exception: " + exception.getMessage());
        }
    }
}
