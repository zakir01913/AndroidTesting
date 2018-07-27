package com.zakir.androidtesting.addUser;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.persistence.User;
import com.zakir.androidtesting.repository.UserRepository;

import javax.inject.Inject;

/**
 * Created by zakir on 26/7/18.
 */

public class AddUserViewModel extends ViewModel {
    private UserRepository userRepository;
    private User user;
    private MutableLiveData<Response<User>> response = new MutableLiveData<>();

    @Inject
    public AddUserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void insertOrUpdate(User user) {
        if (validate(user)) {
            this.user = user;
            userRepository.insertOrUpdate(user);
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

        return true;
    }
}
