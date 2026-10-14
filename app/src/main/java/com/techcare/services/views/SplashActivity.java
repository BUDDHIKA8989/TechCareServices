package com.techcare.services.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.techcare.services.R;
import com.techcare.services.utils.SessionManager;

public class SplashActivity extends Activity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

        // Show splash for 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is already logged in
                if (sessionManager.isLoggedIn()) {
                    // User is logged in, go to appropriate dashboard
                    if (sessionManager.isAdmin()) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashActivity.this, CustomerDashboardActivity.class);
                        startActivity(intent);
                    }

                } else {
                    // User not logged in, go to Login screen
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 2000); // 2 seconds delay
    }
}
