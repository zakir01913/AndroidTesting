package com.zakir.androidtesting.user;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zakir.androidtesting.R;
import com.zakir.androidtesting.persistence.User;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private List<User> userList = new ArrayList<>();

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.user_list_item, parent, false);

        return new UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userNameTextView.setText(user.getFirstName() + " " + user.getLastName());
        holder.userEmailTextView.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    static class UserListViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView;
        TextView userEmailTextView;

        public UserListViewHolder(View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.user_name_label_tv);
            userEmailTextView = itemView.findViewById(R.id.user_email_tv);
        }
    }

}
