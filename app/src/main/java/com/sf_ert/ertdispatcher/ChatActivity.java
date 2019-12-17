package com.sf_ert.ertdispatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sf_ert.ertdispatcher.Adapter.MessageAdapter;
import com.sf_ert.ertdispatcher.Api.ApiClient;
import com.sf_ert.ertdispatcher.Api.ApiInterface;
import com.sf_ert.ertdispatcher.Model.Messages;
import com.sf_ert.ertdispatcher.Model.SendMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    TextView mReciever,mSendIcon;
    EditText mMsg;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private List<Messages> messages;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Intent data = getIntent();
        String username = data.getStringExtra("username");
        final String intent_user_id = data.getStringExtra("p_k");

                Toast.makeText(getApplicationContext(),"Locaded"+intent_user_id,Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        final String sender = sharedPreferences.getString("p_k","");


        LinearLayoutManager transitActivity = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView = findViewById(R.id.msgs_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // TODO: 18/04/2019 The code below the setReverseLayout() is really cool..
//        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

//        final String user_id = "1";

        mMsg = findViewById(R.id.msg);
        mSendIcon = findViewById(R.id.send_icon);
        mSendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m = mMsg.getText().toString();
                sendMessage(sender,intent_user_id,m);

            }
        });
        message_recieve(intent_user_id,sender);


        // TODO: 18/04/2019 What the below Does is retrieving messages from DB every 2 seconds
        final Thread fetching_msg = new Thread(){
            @Override
            public void run() {
                while (!interrupted()){
                    try {
                        Thread.sleep(1000);
                        message_recieve(intent_user_id,sender);
//                        getContext().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                message_recieve(user_id,sender);
//                            }
//                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
//        fetching_msg.start();

//        recyclerView.onTouchEvent()

        /*Fetching Messages Ends here*/
    }

    private void message_recieve(String reciever,String sender) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Messages>> r_chats = apiInterface.getMessages(reciever,sender);
        r_chats.enqueue(new Callback<List<Messages>>() {
            @Override
            public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
                messages = response.body();
                messageAdapter = new MessageAdapter(messages,getApplicationContext());
                recyclerView.setAdapter(messageAdapter);
//                Toast.makeText(getApplicationContext(),"Locaded",Toast.LENGTH_LONG).show();
                for (int i = 0; i < response.body().size(); i++){
                    Log.d("D", response.body().get(i).getContent());
                }


            }

            @Override
            public void onFailure(Call<List<Messages>> call, Throwable t) {
                Log.d("D", "Error");

            }
        });
    }

    private void sendMessage(final String sender, String receiver, String message) {
        final String sSend = sender.toString();
        final String sMsg = message.toString();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<SendMessage>> mChat = apiInterface.sendMessage(sSend,receiver,sMsg);
        mChat.enqueue(new Callback<List<SendMessage>>() {
            @Override
            public void onResponse(Call<List<SendMessage>> call, Response<List<SendMessage>> response) {
                Toast.makeText(getApplicationContext(),"Sent",Toast.LENGTH_SHORT).show();
                mMsg.setText("");
//                message_recieve(sender,receiver);
            }

            @Override
            public void onFailure(Call<List<SendMessage>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Sent?",Toast.LENGTH_SHORT).show();
                mMsg.setText("");

            }
        });
    }
}
