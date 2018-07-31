package com.zakir.androidtesting.addUser;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.repository.UserRepository;

import org.apache.commons.validator.routines.EmailValidator;

import javax.inject.Inject;

/**
 * Created by zakir on 26/7/18.
 */

public class AddUserViewModel extends ViewModel {
    private UserRepository userRepository;
    private User user;
    @Inject
    MutableLiveData<Response<User>> response;
    @Inject
    AddUserViewModel addUserViewModel;

    @Inject
    public AddUserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void insert(User user) {
        if (validate(user)) {
            this.user = user;
            response.setValue(Response.loading());
            long id = userRepository.insert(user);
            user.setId(id);
            response.setValue(Response.success(user));
        }
    }

    public MutableLiveData<Response<User>> response() {
        return response;
    }

    private boolean validate(User user) {
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            response.setValue(Response.error(new AddUserException(
                    "First name can't be empty", AddUserException.ErrorCode.EMPTY_FIRST_NAME)));
            return false;
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            response.setValue(Response.error(new AddUserException(
                    "Last name can't be empty", AddUserException.ErrorCode.EMPTY_LAST_NAME)));
            return false;
        }
        if (user.getEmail() == null || user.getEmail().isEmpty() || !EmailValidator.getInstance().isValid(user.getEmail())) {
            response.setValue(Response.error(new AddUserException(
                    "Invalid email address", AddUserException.ErrorCode.INVALID_EMAIL)));
            return false;
        }

        return true;
    }
}
