package com.liuzq.basemodule.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.liuzq.basemodule.R;
import com.liuzq.basemodule.api.ApiService;
import com.liuzq.basemodule.bean.BookBean;
import com.liuzq.commlibrary.utils.LogUtils;
import com.liuzq.commlibrary.utils.ToastUtils;
import com.liuzq.httplibrary.RxHttpUtils;
import com.liuzq.httplibrary.download.DownloadObserver;
import com.liuzq.httplibrary.interceptor.Transformer;
import com.liuzq.httplibrary.observer.CommonObserver;
import com.liuzq.httplibrary.upload.UploadUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class NetActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    public static void startActivity(Context context){
        Intent intent = new Intent(context,NetActivity.class);
        context.startActivity(intent);
    }

    private Dialog loading_dialog;
    String url = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";
    final String fileName = "alipay.apk";
    String uploadUrl = "http://t.xinhuo.com/index.php/Api/Pic/uploadPic";
    String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + "meinv.jpg";  //需要在手机Root目录下存放一张图片
    TextView response;
    private RxPermissions permissions;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        initView();
    }

    private void initView(){
        loading_dialog = new AlertDialog.Builder(this).setMessage("loading...").create();
        permissions = new RxPermissions(this);
        response = findViewById(R.id.response);
    }

    public void request(View view) {
        switch (view.getId()) {
            case R.id.global_request:
                globalRequest();
                break;
            case R.id.single_download:
                singleDownload();
                break;
            case R.id.global_upload:
                mType = 1;
                upload();
                break;
            case R.id.single_upload:
                mType = 2;
                upload();
                break;
        }
    }

    /**
     * 全局方式请求
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
                        response.setText("全局方式请求，error:" + errorMsg);
                    }

                    @Override
                    protected void onSuccess(BookBean bookBean) {
                        LogUtils.e(TAG, bookBean.getSummary());
                        response.setText("全局方式请求，success：" + bookBean.getSummary());
                    }
                });
    }

    /**
     * 单个方式下载
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
                        response.setText("单个方式下载，error:" + errorMsg);
                    }

                    @Override
                    protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                        LogUtils.e("下载中：" + progress + "% == 下载文件路径：" + filePath);
                        response.setText("单个方式下载，success：" + "下载中：" + progress + "% == 下载文件路径：" + filePath);
                    }
                });
    }

    private void upload(){
        permissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean){
                            // All requested permissions are granted
                            switch (mType){
                                case 1:
                                    globalUpload();
                                    break;
                                case 2:
                                    singleUpload();
                                    break;
                            }
                        } else {
                            // At least one permission is denied
                            ToastUtils.show("请授权");
                        }
                    }
                });
    }

    /**
     * 全局方式上传图片
     */
    private void globalUpload() {
        RxHttpUtils
                .createApi(ApiService.class)
                .uploadFiles(uploadUrl, UploadUtils.uploadImage(filePath))
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
                        response.setText("全局方式上传图片，error:" + errorMsg);
                    }

                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                        try {
                            String msg = responseBody.string();
                            LogUtils.e("liuzq", "上传完毕: " + msg);
                            ToastUtils.show(msg);
                            response.setText("全局方式上传图片，success:" + "上传完毕: " + msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 单个方式上传图片
     */
    private void singleUpload() {
        RxHttpUtils.uploadImg(uploadUrl, filePath)
                .compose(Transformer.<ResponseBody>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<ResponseBody>() {
                    @Override
                    protected void onError(String errorMsg) {
                        LogUtils.e("liuzq", "上传失败: " + errorMsg);
                        ToastUtils.show(errorMsg);
                        response.setText("单个方式上传图片，error:" + errorMsg);
                    }

                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                        try {
                            String msg = responseBody.string();
                            LogUtils.e("liuzq", "上传完毕: " + msg);
                            ToastUtils.show(msg);
                            response.setText("单个方式上传图片，success:" + msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
