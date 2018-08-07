package com.zakir.androidtesting.user;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zakir.androidtesting.R;

public class UserDetailActivity extends AppCompatActivity {
    public static String USER_ID_KEY = "userId";
    private long userId;
    private UserDetailFragment userDetailFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        userDetailFragment = (UserDetailFragment) getSupportFragmentManager().findFragmentById(R.id.user_detail);
        if (userDetailFragment == null) {
            userDetailFragment = new UserDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.user_detail, userDetailFragment)
                    .commit();
        }

        userId = getIntent().getLongExtra(USER_ID_KEY, -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userId != -1) {
            userDetailFragment.loadUser(userId);
        }
    }
}
