package com.zakir.androidtesting.addUser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zakir.androidtesting.R;
import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.persistence.User;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddUserActivity extends AppCompatActivity {

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
        addUserViewModel.insert(user);
    }

    private void handleResponse(Response<User> responseObserver) {

    }
}
