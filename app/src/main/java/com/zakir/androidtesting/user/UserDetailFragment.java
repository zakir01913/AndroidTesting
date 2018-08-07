package com.zakir.androidtesting.user;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zakir.androidtesting.AndroidTestingApplication;
import com.zakir.androidtesting.R;
import com.zakir.androidtesting.Response;
import com.zakir.androidtesting.Status;
import com.zakir.androidtesting.UserViewModel;
import com.zakir.androidtesting.UserViewModelFactoryType;
import com.zakir.androidtesting.persistence.User;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailFragment extends Fragment {

    @BindView(R.id.user_name_tv)
    TextView userNameTextView;
    @BindView(R.id.user_email_tv)
    TextView userEmainTextView;
    @BindView(R.id.user_company_tv)
    TextView userCompanyTextView;
    @BindView(R.id.user_designation_tv)
    TextView userDesignationTextView;
    @BindView(R.id.user_detail_group)
    Group userDetailGroup;
    @BindView(R.id.user_detail_error_tv)
    TextView userDetailError;

    @Inject
    @UserViewModelFactoryType
    public ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidTestingApplication.get(getActivity()).
                getAndroidTestingApplicationComponent()
                .inject(this);

        userViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).
                get(UserViewModel.class);
        userViewModel.getUserMutableLiveData().observe(getActivity(),
                userResponse -> handleResponse(userResponse));

    }

    public void loadUser(long userId) {
        userViewModel.loadUser(userId);
    }

    private void handleResponse(Response<User> userResponse) {
        if (userResponse.getStatus() == Status.SUCCESS) {
            User user = userResponse.getData();
            userNameTextView.setText(user.getFullName());
            userEmainTextView.setText(user.getEmail());
            userCompanyTextView.setText(user.getCompany());
            userDesignationTextView.setText(user.getDesignation());
            userDetailError.setVisibility(View.GONE);
        }

        else if (userResponse.getStatus() == Status.ERROR) {
            userDetailGroup.setVisibility(View.GONE);
            userDetailError.setText(userResponse.getError().getMessage());
            userDetailError.setVisibility(View.VISIBLE);
        }

    }
}
