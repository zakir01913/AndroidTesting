package com.zakir.androidtesting.addUser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zakir.androidtesting.R;

public class AddUserActivity extends AppCompatActivity {

    AddUserViewModel addUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
    }
}
