package com.zakir.androidtesting.user;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zakir.androidtesting.R;
import com.zakir.androidtesting.persistence.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private List<User> userList = new ArrayList<>();
    private ItemClickListener itemClickListener;
    private ItemDeleteClickListener itemDeleteClickListener;

    public interface ItemClickListener {
        void onItemClick(long userId);
    }

    public interface ItemDeleteClickListener {
        void onItemDelete(User user);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemDeleteClickListener(ItemDeleteClickListener itemDeleteClickListener) {
        this.itemDeleteClickListener = itemDeleteClickListener;
    }

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
        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(user.getId()));
        holder.deleteImageView.setOnClickListener(v -> itemDeleteClickListener.onItemDelete(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserList(List<User> userList) {
        int oldSize = !this.userList.isEmpty()? this.userList.size() : 0;
        this.userList = userList;
        int newSize = userList.size();
        if (oldSize > newSize) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(oldSize, this.userList.size() - 1);
        }
    }

    static class UserListViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView;
        TextView userEmailTextView;
        ImageView deleteImageView;

        public UserListViewHolder(View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.user_name_label_tv);
            userEmailTextView = itemView.findViewById(R.id.user_email_tv);
            deleteImageView = itemView.findViewById(R.id.delete_iv);
        }
    }

}
