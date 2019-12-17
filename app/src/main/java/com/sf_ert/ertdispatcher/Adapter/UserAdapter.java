package com.sf_ert.ertdispatcher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sf_ert.ertdispatcher.ChatActivity;
import com.sf_ert.ertdispatcher.Model.Users;
import com.sf_ert.ertdispatcher.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    List<Users> users;
    Context context;

    public UserAdapter(List<Users> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_items,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users u_model =users.get(position);
        holder.u_model = u_model;
        holder.mEmail.setText(users.get(position).getEmail());
        holder.mName.setText(users.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mName, mEmail;
        Users u_model;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.name);
            mEmail = itemView.findViewById(R.id.email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chat_intent = new Intent(itemView.getContext(), ChatActivity.class);
                    chat_intent.putExtra("p_k", u_model.getP_k());
                    chat_intent.putExtra("username", u_model.getUsername());
                    chat_intent.putExtra("email", u_model.getEmail());
                    chat_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    itemView.getContext().startActivity(chat_intent);
                }
            });
        }
    }
}
