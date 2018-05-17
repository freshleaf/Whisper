package io.buzznerd.varys.app;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import io.buzznerd.varys.whisper.Whisper;

/**
 * Created on 2016/11/7
 *
 * @author Xingye
 * @since 1.0.0
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        Whisper.getDefaultInstance().logOnResume(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        Whisper.getDefaultInstance().logOnPause(this);
        MobclickAgent.onPause(this);
        super.onPause();
    }

}
