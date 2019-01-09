package com.liuzq.basemodule.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuzq.basemodule.R;
import com.liuzq.commlibrary.widget.PageLayout;

public class ErrorActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    PageLayout mPageLayout;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,ErrorActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        customPageLayout();
    }


    //默认加载样式
    private void defaultPageLayout() {
        View ll = findViewById(R.id.ll);
        View layout_custom = LayoutInflater.from(this).inflate(R.layout.layout_custom, null);
        mPageLayout = new PageLayout.Builder(this)
                .initPage(ll)
                .setCustomView(layout_custom)
                .setOnRetryListener(new PageLayout.OnRetryClickListener() {
                    @Override
                    public void onRetry() {
                        loadData();
                    }
                })
                .create();
        loadData();
    }

    //自定义加载模式
    private void customPageLayout() {
        View ll = findViewById(R.id.ll);
        View custom = LayoutInflater.from(this).inflate(R.layout.layout_custom, null);
        ((ImageView) (custom.findViewById(R.id.iv_custom))).setImageResource(R.mipmap.icon_smile);
        ((TextView) custom.findViewById(R.id.tv_custom_content)).setText("This is PageLayout");
        mPageLayout = new PageLayout.Builder(this)
                .initPage(ll)
                .setLoading(R.layout.layout_loading_demo, R.id.tv_page_loading_demo)
                .setEmpty(R.layout.layout_empty_demo, R.id.tv_page_empty_demo)
                .setCustomView(custom)
                .setError(R.layout.layout_error_demo, R.id.tv_page_error_demo, new PageLayout.OnRetryClickListener() {
                    @Override
                    public void onRetry() {
                        loadData();
                    }
                })
//                .setEmptyDrawable(R.drawable.pic_empty)
//                .setErrorDrawable(R.drawable.pic_error)
//                .setLoadingText("Loading")
                .create();

        loadData();
    }

    private void loadData() {
        mPageLayout.showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageLayout.hide();
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_content:
                mPageLayout.hide();
                break;
            case R.id.menu_customer:
                mPageLayout.showCustom();
                break;
            case R.id.menu_empty:
                mPageLayout.showEmpty();
                break;
            case R.id.menu_error:
                mPageLayout.showError();
                break;
            case R.id.menu_loading:
                mPageLayout.showLoading();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
