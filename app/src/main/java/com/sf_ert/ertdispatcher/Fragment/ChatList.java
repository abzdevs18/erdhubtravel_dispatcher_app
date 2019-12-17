package com.sf_ert.ertdispatcher.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sf_ert.ertdispatcher.Adapter.UserAdapter;
import com.sf_ert.ertdispatcher.Api.ApiClient;
import com.sf_ert.ertdispatcher.Api.ApiInterface;
import com.sf_ert.ertdispatcher.ChatActivity;
import com.sf_ert.ertdispatcher.Model.Users;
import com.sf_ert.ertdispatcher.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatList extends Fragment {
    private RecyclerView mRecycler;
    private List<Users> users;
    private UserAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ApiInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_list_fragment,container,false);
        mRecycler = v.findViewById(R.id.userList);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);

        SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        final String user_pk = sharedPreferences.getString("p_k","");
        final String terminal = sharedPreferences.getString("terminal","");

        Log.d("Data", user_pk + ":" + terminal);
        users(user_pk,terminal);

        LinearLayout sendMsgAdmin = v.findViewById(R.id.adminList);
        sendMsgAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chat_intent = new Intent(getActivity(), ChatActivity.class);
                chat_intent.putExtra("p_k", "1");
                chat_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(chat_intent);

            }
        });

        return v;
    }
    private void users(String p_k,String terminal) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        String sId = p_k;

        Call<List<Users>> usersList = apiInterface.userList(sId,terminal);
        usersList.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                users = response.body();
                adapter = new UserAdapter(users,getActivity());
                mRecycler.setAdapter(adapter);
                Log.d("Data", response.body().get(0).getP_k());

            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Log.d("Data", t.getMessage());

            }
        });

    }
}
