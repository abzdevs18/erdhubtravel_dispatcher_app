package com.sf_ert.ertdispatcher.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sf_ert.ertdispatcher.Adapter.MessageAdapter;
import com.sf_ert.ertdispatcher.Api.ApiClient;
import com.sf_ert.ertdispatcher.Api.ApiInterface;
import com.sf_ert.ertdispatcher.Model.Messages;
import com.sf_ert.ertdispatcher.Model.SendMessage;
import com.sf_ert.ertdispatcher.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Messenger extends Fragment {
    TextView mReciever,mSendIcon;
    EditText mMsg;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private List<Messages> messages;
    private MessageAdapter messageAdapter;
    View v;
    public Messenger(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_chat,container,false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        final String sender = sharedPreferences.getString("p_k","");


        LinearLayoutManager transitActivity = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView = v.findViewById(R.id.msgs_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // TODO: 18/04/2019 The code below the setReverseLayout() is really cool..
//        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        final String user_id = "1";

        mMsg = v.findViewById(R.id.msg);
        mSendIcon = v.findViewById(R.id.send_icon);
        mSendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m = mMsg.getText().toString();
//                sendMessage(sender,m);

            }
        });
//        message_recieve(user_id,sender);


        // TODO: 18/04/2019 What the below Does is retrieving messages from DB every 2 seconds
//        final Thread fetching_msg = new Thread(){
//            @Override
//            public void run() {
//                while (!interrupted()){
//                    try {
//                        Thread.sleep(1000);
//                        message_recieve(user_id,sender);
////                        getContext().runOnUiThread(new Runnable() {
////                            @Override
////                            public void run() {
////                                message_recieve(user_id,sender);
////                            }
////                        });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        fetching_msg.start();

//        recyclerView.onTouchEvent()

        /*Fetching Messages Ends here*/
        return v;
    }

//    private void message_recieve(String reciever,String sender) {
//        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//        Call<List<Messages>> r_chats = apiInterface.getMessages(reciever,sender);
//        r_chats.enqueue(new Callback<List<Messages>>() {
//            @Override
//            public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
//                messages = response.body();
//                messageAdapter = new MessageAdapter(messages,getActivity());
//                recyclerView.setAdapter(messageAdapter);
////                Toast.makeText(getApplicationContext(),"Locaded",Toast.LENGTH_LONG).show();
//                for (int i = 0; i < response.body().size(); i++){
//                    Log.d("D", response.body().get(i).getContent());
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Messages>> call, Throwable t) {
//                Log.d("D", "Error");
//
//            }
//        });
//    }
//
//    private void sendMessage(final String sender, String message) {
//        final String sSend = sender.toString();
//        final String sMsg = message.toString();
//        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//        Call<List<SendMessage>> mChat = apiInterface.sendMessage(sSend,sMsg);
//        mChat.enqueue(new Callback<List<SendMessage>>() {
//            @Override
//            public void onResponse(Call<List<SendMessage>> call, Response<List<SendMessage>> response) {
//                Toast.makeText(getContext(),"Sent",Toast.LENGTH_SHORT).show();
//                mMsg.setText("");
////                message_recieve(sender,receiver);
//            }
//
//            @Override
//            public void onFailure(Call<List<SendMessage>> call, Throwable t) {
//                Toast.makeText(getContext(),"Sent?",Toast.LENGTH_SHORT).show();
//                mMsg.setText("");
//
//            }
//        });
//    }
}
