package com.flytbasetask.splash.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.flytbasetask.MainActivity;
import com.flytbasetask.R;
import com.flytbasetask.core.AppState;
import com.flytbasetask.login.view.LoginActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {

    @Inject
    AppState appState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setSplashScreen();
    }

    private void setSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToApp();
            }
        }, 5000);
    }

    private void navigateToApp() {
        Log.e("SplashActivity","SplashActivity = "+appState.isLoggedIn());
        if (appState.isLoggedIn() == true){
            Intent intent_goToLogin = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent_goToLogin);
        }else {
            Intent intent_goToHome = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent_goToHome);
        }
    }

}