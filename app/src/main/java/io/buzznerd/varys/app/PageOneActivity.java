package io.buzznerd.varys.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.buzznerd.varys.whisper.Whisper;

/**
 * Created on 2016/11/7
 *
 * @author Xingye
 * @since 1.0.0
 */

public class PageOneActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pageone);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_pageone_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.activity_pageone_button_log).setOnClickListener(this);
        findViewById(R.id.activity_pageone_button_next_page).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_pageone_button_log:
                Whisper.getDefaultInstance().logOkchemBizInApplication("10000002");
                break;
            case R.id.activity_pageone_button_next_page:
                Intent intent = new Intent(PageOneActivity.this, PageTwoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
