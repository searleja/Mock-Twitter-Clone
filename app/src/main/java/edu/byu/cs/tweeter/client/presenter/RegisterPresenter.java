package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;
import android.widget.ImageView;

import edu.byu.cs.tweeter.client.presenter.viewInterfaces.AuthView;

public class RegisterPresenter extends SimplePresenter {

    private AuthView view;

    public RegisterPresenter(AuthView view) {
        super(view);
        this.view = view;
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        getUserService().register(firstName, lastName, alias, password, imageBytesBase64, new AuthObserver(view));
    }

    public void validateRegistration(EditText firstName, EditText lastName, EditText alias, EditText password, ImageView imageToUpload) {
        if (firstName.getText().length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.getText().length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.getText().length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (imageToUpload.getDrawable() == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    @Override
    public String getDescription() {
        return "register";
    }
}
