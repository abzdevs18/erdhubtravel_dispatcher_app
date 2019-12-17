package com.sf_ert.ertdispatcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jgabrielfreitas.core.BlurImageView;
import com.sf_ert.ertdispatcher.Api.ApiClient;
import com.sf_ert.ertdispatcher.Api.ApiInterface;
import com.sf_ert.ertdispatcher.Model.SignIn;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText mEmail,mPass;
    BlurImageView mBlur;
    Button mGetStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mBlur = (BlurImageView)findViewById(R.id.blurSplash);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.transition);
        mBlur.startAnimation(animation);

        mEmail = findViewById(R.id.lEmail);
        mPass = findViewById(R.id.lPass);

        mGetStart = findViewById(R.id.get_started);
        mGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String sEmail = mEmail.getText().toString();
        String sPass = mPass.getText().toString();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<SignIn> call = apiInterface.signIn(sEmail,sPass);
        call.enqueue(new Callback<SignIn>() {
            @Override
            public void onResponse(Call<SignIn> call, Response<SignIn> response) {

                String status = response.body().getData().getStatus();

                if (status.equals("1")){
                    String p_k = response.body().getRow().getUsr_id();
                    String terminal = response.body().getRow().getTerminalName();

                    SharedPreferences sharedPref = Login.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("p_k",p_k);
                    editor.putString("terminal",terminal);

                    editor.apply();
                    startActivity(new Intent(Login.this,MainActivity.class));
                    Toast.makeText(getApplicationContext(),response.body().getRow().getUsr_id(),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Seems something went wrong...",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SignIn> call, Throwable t) {

            }
        });
    }
}
