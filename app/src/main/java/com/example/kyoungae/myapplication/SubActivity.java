package com.example.kyoungae.myapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.kyoungae.myapplication.databinding.ActivitySubBinding;
import com.example.kyoungae.myapplication.event.MessageEvent;
import com.example.kyoungae.myapplication.event.ProgressBarEvent;
import com.example.kyoungae.myapplication.event.WebviewUrlEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SubActivity extends AppCompatActivity {

    private ActivitySubBinding binding;
//    private int mCurrentToolbar;
//    private View mToolbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (getIntent().getSerializableExtra("data") != null) {

            ActivityChangeIntentDataModel dataModel = (ActivityChangeIntentDataModel) intent.getSerializableExtra("data");

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_layout, dataModel.getFragment()).commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event) {
//        if (event instanceof ProgressBarEvent) {
//            ProgressBarEvent progressBarEvent = (ProgressBarEvent) event;
//            boolean progressbarShown = progressBarEvent.isShow();
//            if (progressbarShown) {
//                binding.progressBar.setVisibility(View.VISIBLE);
//            } else {
//                binding.progressBar.setVisibility(View.GONE);
//            }
//        }
//        if (event instanceof FragmentReplaceEvent) {
//            FragmentReplaceEvent fragmentReplaceEvent = (FragmentReplaceEvent) event;
//            if (fragmentReplaceEvent.isToolbarHide()) {
//                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
//                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
//                Toast.makeText(this, "숨김", Toast.LENGTH_SHORT).show();
//            } else {
//                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
//                params.setScrollFlags(0);
//                Toast.makeText(this, "보임", Toast.LENGTH_SHORT).show();
//            }
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragmentReplaceEvent.getFragment()).addToBackStack("null").commit();
//            setTitle(fragmentReplaceEvent.getFragmentName());
//
//        }
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//        String url = getIntent().getStringExtra("url");
//        if (url != null) {
//            EventBus.getDefault().post(new WebviewUrlEvent(url));
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }
}
