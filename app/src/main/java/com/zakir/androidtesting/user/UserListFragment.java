package com.zakir.androidtesting.user;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zakir.androidtesting.AndroidTestingApplication;
import com.zakir.androidtesting.R;
import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.Status;
import com.zakir.androidtesting.UserViewModel;
import com.zakir.androidtesting.UserViewModelFactoryType;
import com.zakir.androidtesting.addUser.AddUserActivity;
import com.zakir.androidtesting.persistence.User;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserListFragment extends Fragment implements UserListAdapter.ItemClickListener {

    @BindView(R.id.user_list_loader_fl)
    FrameLayout userListLoaderFL;
    @BindView(R.id.user_list_rv)
    RecyclerView userRecyclerView;

    @Inject
    @UserViewModelFactoryType
    public ViewModelProvider.Factory viewModelFactory;

    private UserListAdapter userListAdapter = new UserListAdapter();

    private UserViewModel userViewModel;

    private UserViewModel viewModel;

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
        UserListFragmentComponent component = AndroidTestingApplication.get(getActivity())
                .createUserListFragmentComponent(this);
        component.injectUserListFragment(this);

        userViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
        .get(UserViewModel.class);

        userRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userRecyclerView.setAdapter(userListAdapter);
        userListAdapter.setItemClickListener(this);

        userViewModel.getUsersMutableLiveData().observe(this,
                listResponse -> handleResponse(listResponse));
        userViewModel.loadUsers();
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
        else if (listResponse.getStatus() == Status.SUCCESS) {
            userListLoaderFL.setVisibility(View.GONE);
            userListAdapter.setUserList(listResponse.getData());
        }
        else if (listResponse.getStatus() == Status.ERROR) {
            userListLoaderFL.setVisibility(View.GONE);
            listResponse.getError().printStackTrace();
        }
    }

    @Override
    public void onItemClick(long userId) {
        ((Contract)getActivity()).onUserSelected(userId);
    }

    public interface Contract {
        void onUserSelected(long userId);
    }

    @Override
    public void onDetach() {
        AndroidTestingApplication.get(getActivity()).realeaseUserListFragmentComponent();
        userViewModel.dispose();
        super.onDetach();
    }

    @VisibleForTesting
    public void setUserViewModel(UserViewModel userViewModel) {
        this.userViewModel = userViewModel;
        userViewModel.getUsersMutableLiveData().observe(this,
                listResponse -> handleResponse(listResponse));
    }
}
