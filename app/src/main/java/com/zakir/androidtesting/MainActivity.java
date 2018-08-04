package com.zakir.androidtesting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zakir.androidtesting.user.UserListFragment;

public class MainActivity extends AppCompatActivity {

    UserListFragment userListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userListFragment = (UserListFragment) getSupportFragmentManager().findFragmentById(R.id.user_list);

        if (userListFragment == null) {
            userListFragment = new UserListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.user_list, userListFragment)
                    .commit();
        }
    }

}
