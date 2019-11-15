package com.liuzq.basemodule.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IWebLayout;
import com.just.agentweb.LogUtils;
import com.just.agentweb.MiddlewareWebChromeBase;
import com.just.agentweb.MiddlewareWebClientBase;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;
import com.liuzq.basemodule.R;
import com.liuzq.basemodule.client.MiddlewareWebViewClient;
import com.liuzq.basemodule.ui.activity.base.CommWebActivity;
import com.liuzq.basemodule.widget.WebLayout;

import butterknife.BindView;

public class WebActivity extends CommWebActivity {

    @BindView(R.id.container)
    LinearLayout mLinearLayout;

    @Override
    public void initData(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_web;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void doBusiness() {
        super.doBusiness();
    }

    @Nullable
    @Override
    protected String getUrl() {
        return "https://www.baidu.com";
    }

    @NonNull
    @Override
    protected ViewGroup getAgentWebParent() {
        return mLinearLayout;
    }

    @Nullable
    @Override
    protected WebChromeClient getWebChromeClient() {
        return mWebChromeClient;
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };

    @Nullable
    @Override
    protected WebViewClient getWebViewClient() {
        return mWebViewClient;
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            LogUtils.i("Info", "WebActivity onPageStarted");
        }
    };

    @Nullable
    @Override
    protected PermissionInterceptor getPermissionInterceptor() {
        return mPermissionInterceptor;
    }

    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            Gson mGson = new Gson();
            LogUtils.i(TAG, "mUrl:" + url + "  permission:" + mGson.toJson(permissions) + " action:" + action);
            return false;
        }
    };

    @Nullable
    @Override
    protected IWebLayout getWebLayout() {
        return new WebLayout(this);
    }

    @Nullable
    @Override
    public DefaultWebClient.OpenOtherPageWays getOpenOtherAppWay() {
        return DefaultWebClient.OpenOtherPageWays.ASK;
    }


    @NonNull
    @Override
    protected MiddlewareWebChromeBase getMiddleWareWebChrome() {
        return super.getMiddleWareWebChrome();
    }

    @NonNull
    @Override
    protected MiddlewareWebClientBase getMiddleWareWebClient() {
        return getMiddlewareWebClient();
    }

    protected MiddlewareWebClientBase getMiddlewareWebClient() {
        return this.mMiddleWareWebClient = new MiddlewareWebViewClient() {
            /**
             *
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.e(TAG, "MiddlewareWebClientBase#shouldOverrideUrlLoading url:" + url);
				/*if (url.startsWith("agentweb")) { // 拦截 url，不执行 DefaultWebClient#shouldOverrideUrlLoading
					Log.i(TAG, "agentweb scheme ~");
					return true;
				}*/

                if (super.shouldOverrideUrlLoading(view, url)) { // 执行 DefaultWebClient#shouldOverrideUrlLoading
                    return true;
                }
                // do you work
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                LogUtils.e(TAG, "MiddlewareWebClientBase#shouldOverrideUrlLoading request url:" + request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        };
    }

}
