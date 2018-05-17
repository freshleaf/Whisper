package io.buzznerd.varys.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import io.buzznerd.varys.whisper.Whisper;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static String KEY_LOGIN_STATE = "key_login";
    private static String KEY_IN_SESSION = "key_in_session";

    private Button mBtnLogLogin, mBtnLogLogout;
    private Button mBtnStartSession, mBtnEndSession;
    private Button mBtnLogSession1, mBtnLogSession2;
    private Button mBtnNextPage;
    private String mLoginUserId = null;
    private boolean mInSession = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Whisper.getDefaultInstance().logAppStart();

        if (savedInstanceState != null) {
            mLoginUserId = savedInstanceState.getString(KEY_LOGIN_STATE);
            mInSession = savedInstanceState.getBoolean(KEY_IN_SESSION);
        }

        initUi();
    }

    private void initUi() {
        mBtnLogLogin = (Button) findViewById(R.id.activity_main_button_log_login);
        mBtnLogLogout = (Button) findViewById(R.id.activity_main_button_log_logout);
        mBtnLogLogin.setOnClickListener(this);
        mBtnLogLogout.setOnClickListener(this);

        mBtnStartSession = (Button) findViewById(R.id.activity_main_button_start_session);
        mBtnEndSession = (Button) findViewById(R.id.activity_main_button_end_session);
        mBtnStartSession.setOnClickListener(this);
        mBtnEndSession.setOnClickListener(this);

        mBtnLogSession1 = (Button) findViewById(R.id.activity_main_button_log_session_1);
        mBtnLogSession2 = (Button) findViewById(R.id.activity_main_button_log_session_2);
        mBtnLogSession1.setOnClickListener(this);
        mBtnLogSession2.setOnClickListener(this);

        mBtnNextPage = (Button) findViewById(R.id.activity_main_button_next_page);
        mBtnNextPage.setOnClickListener(this);

        updateUi();
    }

    private void updateUi() {
        if (mLoginUserId == null) {
            mBtnLogLogin.setEnabled(true);
            mBtnLogLogout.setEnabled(false);

            mBtnStartSession.setEnabled(false);
            mBtnEndSession.setEnabled(false);
            mBtnLogSession1.setEnabled(false);
            mBtnLogSession2.setEnabled(false);

            mBtnNextPage.setEnabled(false);
        } else {
            mBtnLogLogin.setEnabled(false);

            mBtnNextPage.setEnabled(true);

            if (mInSession) {
                mBtnLogLogout.setEnabled(false);
                mBtnStartSession.setEnabled(false);
                mBtnEndSession.setEnabled(true);
                mBtnLogSession1.setEnabled(true);
                mBtnLogSession2.setEnabled(true);
            } else {
                mBtnLogLogout.setEnabled(true);
                mBtnStartSession.setEnabled(true);
                mBtnEndSession.setEnabled(false);
                mBtnLogSession1.setEnabled(false);
                mBtnLogSession2.setEnabled(false);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_button_log_login:
                int number = new Random().nextInt();
                if (number < 0) {
                    number = -number;
                }
                mLoginUserId = "testapp_uid_" + number;
                Whisper.getDefaultInstance().logUserLogin(mLoginUserId);
                updateUi();
                break;
            case R.id.activity_main_button_log_logout:
                mLoginUserId = null;
                Whisper.getDefaultInstance().logUserLogout(mLoginUserId);
                updateUi();
                break;
            case R.id.activity_main_button_start_session:
                mInSession = true;
                Whisper.getDefaultInstance().startSession(this, mLoginUserId);
                updateUi();
                break;
            case R.id.activity_main_button_end_session:
                mInSession = false;
                Whisper.getDefaultInstance().endSession();
                updateUi();
                break;
            case R.id.activity_main_button_log_session_1:
                Whisper.getDefaultInstance().logOkchemBizInSession("10000011");
                break;
            case R.id.activity_main_button_log_session_2:
                Whisper.getDefaultInstance().logOkchemBizInSession("10000012");
                break;
            case R.id.activity_main_button_next_page:
                Intent intent = new Intent(MainActivity.this, PageOneActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_LOGIN_STATE, mLoginUserId);
        outState.putBoolean(KEY_IN_SESSION, mInSession);
        super.onSaveInstanceState(outState);
    }
}
