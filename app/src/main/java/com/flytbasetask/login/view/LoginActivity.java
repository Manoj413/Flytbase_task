package com.flytbasetask.login.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.flytbasetask.MainActivity;
import com.flytbasetask.core.AppState;
import com.flytbasetask.core.DatabaseModule;
import com.flytbasetask.databinding.ActivityLoginBinding;
import com.flytbasetask.history.viewmodel.HistoryViewModel;
import com.flytbasetask.login.viewmodel.LoginViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.flytbasetask.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    LoginViewModel loginViewModel;
    ActivityLoginBinding activityLoginBinding;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    LiveData<String> sendOtpLiveData;
    LiveData<String> verificationCode;
    Boolean is_phoneVerified;
    String recieved_otp,recieved_verification_code;
    @Inject
    AppState appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = activityLoginBinding.getRoot();
        setContentView(view);
        inIt();
        getOtpListener();
        verifyOtpListener();
        observeSendOtpLiveData();
    }

    private void observeSendOtpLiveData() {


        loginViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                {
                    activityLoginBinding.vFullLoader.setVisibility(View.VISIBLE);
                    activityLoginBinding.vFullLoader.setmLoadingText("Sending Otp.");
                }else {
                    activityLoginBinding.vFullLoader.setVisibility(View.GONE);
                }
            }
        });

        loginViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showAlertDialog("Error",s);
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show(); }
        });


    }

    private void verifyOtpListener() {
        activityLoginBinding.btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityLoginBinding.txtOtp.getText().toString()!=null && !activityLoginBinding.txtOtp.getText().toString().isEmpty())
                {

                    if (activityLoginBinding.txtOtp.getText().toString()!=null && !activityLoginBinding.txtOtp.getText().toString().isEmpty()){
                        activityLoginBinding.vFullLoader.setVisibility(View.VISIBLE);
                        activityLoginBinding.vFullLoader.setmLoadingText("Verifying Otp.");
                        verifyCode(activityLoginBinding.txtOtp.getText().toString(),recieved_verification_code);
                    }
                }
            }
        });
    }

    private void navigateUI(Boolean verificationCode) {
        if (verificationCode == true){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }else {
            Toast.makeText(LoginActivity.this, "Can not Verify", Toast.LENGTH_LONG).show();
        }
    }

    private void inIt() {
        mAuth = FirebaseAuth.getInstance();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    private void getOtpListener() {
        activityLoginBinding.btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityLoginBinding.txtPhoneNo.getText().toString()!=null && !activityLoginBinding.txtPhoneNo.getText().toString().isEmpty())
                {
                    String phone = "+91" + activityLoginBinding.txtPhoneNo.getText().toString();
                    sendOtpLiveData= loginViewModel.fetchResultResponse(LoginActivity.this,phone);

                    sendOtpLiveData.observe(LoginActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.e("observeSendOtpLiveData","observeSendOtpLiveData = "+s);
                            if (s!=null){
                                activityLoginBinding.txtOtp.setText(s);
                                recieved_otp = s;
                            }
                        }
                    });


                    verificationCode = loginViewModel.fetchVerificatinCode();
                    verificationCode.observe(LoginActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            recieved_verification_code = s;
                        }
                    });
                }
            }
        });
    }

    private void verifyCode(String code,String mVerificationId) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithCredential(credential,mVerificationId,code);
    }
    private void signInWithCredential(PhoneAuthCredential credential, String mVerificationId, String code) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        activityLoginBinding.vFullLoader.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            appState.login(credential.getSignInMethod(),mVerificationId);

                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void showAlertDialog(String title, String message){
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

}