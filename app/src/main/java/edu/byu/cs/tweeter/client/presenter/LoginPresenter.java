package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;

import edu.byu.cs.tweeter.client.presenter.viewInterfaces.AuthView;


public class LoginPresenter extends SimplePresenter {

    private AuthView view;

    public LoginPresenter(AuthView view) {
        super(view);
        this.view = view;
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
        getUserService().login(username, password, new AuthObserver(view));
    }

    @Override
    public String getDescription() {
        return "login";
    }
}
