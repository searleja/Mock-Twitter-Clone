package edu.byu.cs.tweeter.client.view.login;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.client.R;

/**
 * The first activity that appears when the app is run.
 * Contains the "Login" and "Register" tabs
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TESTING", "FIRST");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("TESTING", "FIRST");

        LoginSectionsPagerAdapter loginSectionsPagerAdapter = new LoginSectionsPagerAdapter(this, getSupportFragmentManager());
        Log.d("TESTING", "SECOND");
        ViewPager viewPager = findViewById(R.id.login_view_pager);
        Log.d("TESTING", "THIRD");
        viewPager.setAdapter(loginSectionsPagerAdapter);
        Log.d("TESTING", "FOURTH");
        TabLayout tabs = findViewById(R.id.loginTabs);
        Log.d("TESTING", "FIFTH");
        tabs.setupWithViewPager(viewPager);
    }
}
