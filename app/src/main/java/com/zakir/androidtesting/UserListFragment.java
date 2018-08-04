package com.zakir.androidtesting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zakir.androidtesting.addUser.AddUserActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.add_user_fab)
    public void lunchAddUserActivity() {
        Intent addUserIntent = new Intent(getActivity(), AddUserActivity.class);
        startActivity(addUserIntent);
    }
}
