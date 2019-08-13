package com.liuzq.basemodule.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.liuzq.basemodule.R;
import com.liuzq.basemodule.api.ApiHelper;
import com.liuzq.basemodule.api.DouBanApi;
import com.liuzq.basemodule.api.WanAndroidApi;
import com.liuzq.basemodule.bean.BannerBean;
import com.liuzq.basemodule.bean.Top250Bean;
import com.liuzq.commlibrary.utils.LogUtils;
import com.liuzq.commlibrary.utils.ToastUtils;
import com.liuzq.httplibrary.RxHttpUtils;
import com.liuzq.httplibrary.bean.BaseData;
import com.liuzq.httplibrary.download.DownloadObserver;
import com.liuzq.httplibrary.interceptor.Transformer;
import com.liuzq.httplibrary.observer.CommonObserver;
import com.liuzq.httplibrary.observer.DataObserver;
import com.liuzq.httplibrary.observer.StringObserver;
import com.liuzq.httplibrary.upload.UploadHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;
//import com.zhihu.matisse.Matisse;
//import com.zhihu.matisse.MimeType;
//import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

//import com.zxy.tiny.Tiny;
//import com.zxy.tiny.callback.FileCallback;

public class NetActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    public static void startActivity(Context context){
        Intent intent = new Intent(context, NetActivity.class);
        context.startActivity(intent);
    }

    private Button download_http, download_cancel_http;

    private Dialog loading_dialog;
    String url = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";
    final String fileName = "alipay.apk";
    String uploadUrl = "http://t.xinhuo.com/index.php/Api/Pic/uploadPic";
    String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + "meinv.jpg";  //需要在手机Root目录下存放一张图片
    TextView response;
    private RxPermissions permissions;

    private int REQUEST_CODE_CHOOSE = 1;
    private int MAX_SELECTABLE = 1;
    private boolean IS_USE_GLOBAL_CONFIG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        initView();
    }

    private void initView(){
        download_http = (Button) findViewById(R.id.download_http);
        download_cancel_http = (Button) findViewById(R.id.download_cancel_http);

        loading_dialog = new AlertDialog.Builder(this).setMessage("loading...").create();
        permissions = new RxPermissions(this);
        response = findViewById(R.id.response);
    }

    public void request(View view) {
        switch (view.getId()) {
            case R.id.global_http:
                globalHttp();
                break;
            case R.id.global_string_http:
                globalStringHttp();
                break;
            case R.id.multiple_http:
                multipleHttp();
                break;
            case R.id.download_http:
                downloadHttp();
                download_http.setEnabled(false);
                break;
            case R.id.download_cancel_http:
                RxHttpUtils.cancel("download");
                break;
            case R.id.upload_one_pic:
                MAX_SELECTABLE = 1;
                IS_USE_GLOBAL_CONFIG = false;
                selectPhotoWithPermission(MAX_SELECTABLE);
                break;
            case R.id.upload_more_pic:
                MAX_SELECTABLE = 9;
                IS_USE_GLOBAL_CONFIG = false;
                selectPhotoWithPermission(MAX_SELECTABLE);
                break;
            case R.id.douan_api_http:
                changeUrl();
                break;
        }
    }

    /**
     * 全局方式请求
     */
    private void globalHttp() {
        RxHttpUtils
                .createApi(WanAndroidApi.class)
                .getBanner()
                .compose(Transformer.<BaseData<List<BannerBean>>>switchSchedulers(loading_dialog))
                .subscribe(new DataObserver<List<BannerBean>>() {
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
                    protected void onSuccess(List<BannerBean> data) {
                        String s = data.get(0).toString();
                        LogUtils.e(TAG, s);
                        response.setText("全局方式请求，success：" + s);
                    }
                });
    }

    private void globalStringHttp(){
        RxHttpUtils.createApi(WanAndroidApi.class)
                .getHotSearchStringData()
                .compose(Transformer.<String>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<String>() {
                    @Override
                    protected String setTag() {
                        return "tag1";
                    }

                    @Override
                    protected void onError(String errorMsg) {
                        response.setText("全局方式请求，error:" + errorMsg);
                    }

                    @Override
                    protected void onSuccess(String s) {
                        response.setText("全局方式请求，success：" + s);
                    }
                });
    }

    private void multipleHttp(){
        RxHttpUtils.createApi(WanAndroidApi.class)
                .getBanner()
                .flatMap(new Function<BaseData<List<BannerBean>>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(BaseData<List<BannerBean>> listBaseData) throws Exception {
                        return RxHttpUtils
                                .createApi(WanAndroidApi.class)
                                .getHotSearchStringData();
                    }
                })
                .compose(Transformer.<String>switchSchedulers(loading_dialog))
                .subscribe(new StringObserver() {
                    @Override
                    protected void onError(String errorMsg) {
                        response.setText("全局方式请求，error:" + errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        response.setText("全局方式请求，success：" + data);
                    }
                });
    }

    private void downloadHttp(){
        RxHttpUtils.downloadFile(url)
                .subscribe(new DownloadObserver(fileName) {
                    //可以去下下载
                    @Override
                    protected String setTag() {
                        return "download";
                    }

                    @Override
                    protected void onError(String errorMsg) {
                        ToastUtils.show(errorMsg);
                        download_cancel_http.setEnabled(false);
                        download_http.setEnabled(true);
                        download_http.setText("文件下载");

                        response.setText("下载，error:" + errorMsg);
                    }

                    @Override
                    protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                        LogUtils.e("下载中：" + progress + "% == 下载文件路径：" + filePath);
                        download_cancel_http.setEnabled(true);
                        response.setText("下载，success：" + "下载中：" + progress + "% == 下载文件路径：" + filePath);
                        if (done) {
                            download_http.setEnabled(true);
                            download_http.setText("文件下载");
                        }
                    }
                });
    }

    /**
     * 使用全局配置上传图片  demo
     *
     * @param filePaths 图片路径
     */
    private void uploadImgWithGlobalConfig(List<String> filePaths) {

        //以下使用的是全局配置
        RxHttpUtils.createApi(DouBanApi.class)
                .uploadFiles(uploadUrl, UploadHelper.uploadFilesWithParams("uploaded_file", null, filePaths))
                .compose(Transformer.<String>switchSchedulers(loading_dialog))
                .subscribe(new StringObserver() {

                    @Override
                    protected String setTag() {
                        return "uploadImg";
                    }

                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(String data) {
                        response.setText(String.format("上传完毕，success:%s", data));
                    }
                });
    }

    /**
     * 一次上传多张图片
     *
     * @param uploadPaths 图片路径
     */
    private void uploadImgs(List<String> uploadPaths) {
        RxHttpUtils.uploadImages(uploadUrl, uploadPaths)
                .compose(Transformer.<ResponseBody>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<ResponseBody>() {
                    @Override
                    protected void onError(String errorMsg) {
                        response.setText(String.format("请求，error:%s", errorMsg));
                    }

                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                        String s = "";
                        try {
                            s = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LogUtils.d(String.format("全局方式请求，success：%s",s));
                        response.setText(String.format("全局方式请求，success：%s",s));
                    }
                });
    }

    private void changeUrl(){
        ApiHelper.getDouBanApi()
                .getTop250(5)
                .compose(Transformer.<Top250Bean>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<Top250Bean>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(Top250Bean top250Bean) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(top250Bean.getTitle() + "\n");

                        for (Top250Bean.SubjectsBean s : top250Bean.getSubjects()) {
                            sb.append(s.getTitle() + "\n");
                        }
                        response.setText(sb.toString());
                        //请求成功
                        Toast.makeText(NetActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void selectPhotoWithPermission(final int maxSelectable) {
        permissions.request(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            // All requested permissions are granted
                            selectPhoto(maxSelectable);
                        } else {
                            // At least one permission is denied
                            Toast.makeText(NetActivity.this, "请授权", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * 选择图片
     */
    private void selectPhoto(int maxSelectable) {
        Matisse.from(NetActivity.this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(maxSelectable)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    private List<Uri> mSelected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            final List<String> paths = new ArrayList<>();
            Log.d("Matisse", "mSelected: " + mSelected);

            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            for (int i = 0; i < mSelected.size(); i++) {
                //异步压缩文件
                Tiny.getInstance().source(mSelected.get(i)).asFile().withOptions(options).compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile, Throwable t) {
                        paths.add(outfile);
                        if (paths.size() == mSelected.size()) {
                            if (IS_USE_GLOBAL_CONFIG) {
                                uploadImgWithGlobalConfig(paths);
                            } else {
                                uploadImgs(paths);
                            }
                        }
                    }
                });
            }
        }
    }
}
