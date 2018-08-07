package com.zakir.androidtesting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zakir.androidtesting.user.UserDetailActivity;
import com.zakir.androidtesting.user.UserDetailFragment;
import com.zakir.androidtesting.user.UserListFragment;

public class MainActivity extends AppCompatActivity implements UserListFragment.Contract {

    UserListFragment userListFragment;
    UserDetailFragment userDetailFragment;

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

        userDetailFragment = (UserDetailFragment)getSupportFragmentManager()
                .findFragmentById(R.id.user_detail);

        if (userDetailFragment == null && findViewById(R.id.user_detail) != null) {
            userDetailFragment = new UserDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.user_detail, userDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onUserSelected(long userId) {
        if (userDetailFragment != null) {
            userDetailFragment.loadUser(userId);
        }
        else {
            Intent intent = new Intent(this, UserDetailActivity.class);
            intent.putExtra(UserDetailActivity.USER_ID_KEY, userId);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
