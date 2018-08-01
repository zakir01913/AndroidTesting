package com.zakir.androidtesting.addUser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.zakir.androidtesting.R;
import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.Status;
import com.zakir.androidtesting.persistence.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddUserActivity extends AppCompatActivity {

    @BindView(R.id.first_name_et)
    EditText firstNameEditText;

    @BindView(R.id.last_name_et)
    EditText lastNameEditText;

    @BindView(R.id.email_et)
    EditText emailEditText;

    @BindView(R.id.company_et)
    EditText companyEditText;

    @BindView(R.id.designation_et)
    EditText designationEditText;

    @BindView(R.id.add_user_pb)
    ProgressBar progressBar;

    AddUserViewModel addUserViewModel;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);
    }

    public void setAddUserViewModel(AddUserViewModel addUserViewModel) {
        this.addUserViewModel = addUserViewModel;
        this.addUserViewModel.response().observe(this, observer -> handleResponse(observer));
    }

    @OnClick(R.id.save_btn)
    public void save() {
        user = new User(
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                emailEditText.getText().toString());
        user.setCompany(companyEditText.getText().toString());
        user.setDesignation(designationEditText.getText().toString());
        addUserViewModel.insert(user);
    }

    private void handleResponse(Response<User> responseObserver) {
        if (responseObserver.getStatus() == Status.ERROR) {
            AddUserException addUserException = (AddUserException) responseObserver.getError();
            if (addUserException.errorCode == AddUserException.ErrorCode.EMPTY_FIRST_NAME) {
                firstNameEditText.setError(addUserException.getMessage());
            }
            else if (addUserException.errorCode == AddUserException.ErrorCode.EMPTY_LAST_NAME) {
                lastNameEditText.setError(addUserException.getMessage());
            }
            else if (addUserException.errorCode == AddUserException.ErrorCode.INVALID_EMAIL) {
                emailEditText.setError(addUserException.getMessage());
            }
        }
        else if (responseObserver.getStatus() == Status.LOADING) {
            progressBar.setVisibility(View.VISIBLE);
        }
        else if (responseObserver.getStatus() == Status.SUCCESS) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
