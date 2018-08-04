package com.zakir.androidtesting;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zakir.androidtesting.addUser.AddUserActivity;
import com.zakir.androidtesting.persistence.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserListFragment extends Fragment {

    @BindView(R.id.user_list_loader_fl)
    FrameLayout userListLoaderFL;

    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userViewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(UserViewModel.class);
        userViewModel.getResponseMutableLiveData().observe(this,
                listResponse -> handleResponse(listResponse));
    }

    @OnClick(R.id.add_user_fab)
    public void lunchAddUserActivity() {
        Intent addUserIntent = new Intent(getActivity(), AddUserActivity.class);
        startActivity(addUserIntent);
    }

    private void handleResponse(Response<List<User>> listResponse) {
        if (listResponse.getStatus() == Status.LOADING) {
            userListLoaderFL.setVisibility(View.VISIBLE);
        }
    }
}
