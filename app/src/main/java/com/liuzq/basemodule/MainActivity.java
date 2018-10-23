package com.liuzq.basemodule;

import android.app.Dialog;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.liuzq.basemodule.api.ApiService;
import com.liuzq.basemodule.bean.BookBean;
import com.liuzq.commlibrary.utils.LogUtils;
import com.liuzq.commlibrary.utils.ToastUtils;
import com.liuzq.commlibrary.widget.PageLayout;
import com.liuzq.httplibrary.RxHttpUtils;
import com.liuzq.httplibrary.download.DownloadObserver;
import com.liuzq.httplibrary.interceptor.Transformer;
import com.liuzq.httplibrary.observer.CommonObserver;
import com.liuzq.httplibrary.upload.UploadUtils;

import java.io.IOException;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private Dialog loading_dialog;
    String url = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";
    final String fileName = "alipay.apk";
    String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + "meinv.jpg";  //需要在手机Root目录下存放一张图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loading_dialog = new AlertDialog.Builder(this).setMessage("loading...").create();
        setContentView(R.layout.activity_main);
        pageLayout();
    }

    public void tv(View view) {

    }

    /**
     * 全局请求方式
     */
    private void globalRequest() {
        RxHttpUtils
                .createApi(ApiService.class)
                .getBook()
                .compose(Transformer.<BookBean>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<BookBean>() {
                    //默认false   隐藏onError的提示
                    @Override
                    protected boolean isHideToast() {
                        return false;
                    }

                    //tag下的一组或一个请求，用来处理一个页面的所以请求或者某个请求
                    //设置一个tag就行就可以取消当前页面所有请求或者某个请求了
                    @Override
                    protected String setTag() {
                        return "tag1";
                    }

                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(BookBean bookBean) {
                        LogUtils.e(TAG, bookBean.getSummary());
                    }
                });
    }

    /**
     * 全局下载
     */
    private void globalDownload() {
        RxHttpUtils
                .createApi(ApiService.class)
                .downloadFile(url)
                .compose(Transformer.<ResponseBody>switchSchedulers())
                .subscribe(new DownloadObserver(fileName) {
                    //可以去下下载
                    @Override
                    protected String setTag() {
                        return "download";
                    }

                    @Override
                    protected void onError(String errorMsg) {
                        ToastUtils.show(errorMsg);
                    }

                    @Override
                    protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                        LogUtils.e("下载中：" + progress + "% == 下载文件路径：" + filePath);
                    }
                });
    }

    /**
     * 单个下载
     */
    private void singleDownload() {
        RxHttpUtils
                .downloadFile(url)
                .subscribe(new DownloadObserver(fileName) {
                    //可以去下下载
                    @Override
                    protected String setTag() {
                        return "download";
                    }

                    @Override
                    protected void onError(String errorMsg) {
                        ToastUtils.show(errorMsg);
                    }

                    @Override
                    protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                        LogUtils.e("下载中：" + progress + "% == 下载文件路径：" + filePath);
                    }
                });
    }

    /**
     * 全局上传图片
     */
    private void globalUpload() {
        RxHttpUtils
                .createApi(ApiService.class)
                .uploadFiles("http://t.xinhuo.com/index.php/Api/Pic/uploadPic",
                        UploadUtils.uploadImage(filePath))
                .compose(Transformer.<ResponseBody>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<ResponseBody>() {
                    @Override
                    protected String setTag() {
                        return "uploadImg";
                    }

                    @Override
                    protected void onError(String errorMsg) {
                        LogUtils.e("liuzq", "上传失败: " + errorMsg);
                        ToastUtils.show(errorMsg);
                    }

                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                        try {
                            String msg = responseBody.string();
                            LogUtils.e("liuzq", "上传完毕: " + msg);
                            ToastUtils.show(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    PageLayout mPageLayout;

    private void pageLayout() {
        View ll_default = findViewById(R.id.ll_default);
        View layout_custom = LayoutInflater.from(this).inflate(R.layout.layout_custom, null);
        ImageView iv_custom = layout_custom.findViewById(R.id.iv_custom);
        iv_custom.setImageResource(R.mipmap.icon_smile);
        PageLayout.Builder builder = new PageLayout.Builder(this);
        mPageLayout = builder.initPage(ll_default)
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
        new MenuInflater(this).inflate(R.menu.menus,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
