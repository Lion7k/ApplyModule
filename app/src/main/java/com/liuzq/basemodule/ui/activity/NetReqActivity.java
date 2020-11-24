package com.liuzq.basemodule.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.liuzq.basemodule.R;
import com.liuzq.basemodule.bean.BannerBean;
import com.liuzq.basemodule.bean.Top250Bean;
import com.liuzq.basemodule.contract.RxContract;
import com.liuzq.basemodule.presenter.RxPresenterImpl;
import com.liuzq.basemodule.ui.activity.base.CommActivity;
import com.liuzq.commlibrary.filter.GifSizeFilter;
import com.liuzq.commlibrary.utils.DataUtils;
import com.liuzq.commlibrary.utils.LogUtils;
import com.liuzq.rxhttp.RxHttpUtils;
import com.liuzq.rxhttp.utils.GsonUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;

/**
 * @author liuzq
 * dec: rxhttp 网络请求
 */
public class NetReqActivity extends CommActivity<RxPresenterImpl> implements RxContract.View
        , CommonTitleBar.OnTitleBarListener {

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.response)
    TextView response;

    String url = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";
    final String fileName = "alipay.apk";
    String uploadUrl = "http://t.xinhuo.com/index.php/Api/Pic/uploadPic";
//    private int position = 0;

    @Override
    public void initData(Bundle bundle) {
//        position = bundle.getInt("position");
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_net_req;
    }

    @Override
    public void initPresenter() {
        mPresenter = new RxPresenterImpl();
    }

    @Override
    public void initListener() {
        super.initListener();
        title_bar.setListener(this);
    }

    @Override
    public void doBusiness() {
//        switch (position) {
//            case 0:
//                // 统一请求---使用全局配置的相关参数
//                mPresenter.postGlobalHttp();
//                break;
//            case 1:
//                // 统一请求---接受string
//                mPresenter.postGlobalStringHttp();
//                break;
//            case 2:
//                // 链式发送多个请求
//                mPresenter.postMultipleHttp();
//                break;
//            case 3:
//                // 使用豆瓣baseUrl请求数据
//                mPresenter.postChangeUrl();
//                break;
//        }
    }

    private boolean IS_DOWNLOAD_FILE = true;
    private int REQUEST_CODE_CHOOSE = 1;
    private int MAX_SELECTABLE = 1;
    private boolean IS_USE_GLOBAL_CONFIG = false;

    @SuppressLint("CheckResult")
    private void selectPhotoWithPermission() {
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void applyPermissionResult() {
        super.applyPermissionResult();
        // All requested permissions are granted
        selectPhoto(MAX_SELECTABLE);
    }

    /**
     * 选择图片
     */
    private void selectPhoto(int maxSelectable) {
        Matisse
                .from(mActivity)
                //选择视频和图片
                .choose(MimeType.ofAll())
                //选择图片
//                .choose(MimeType.ofImage())
                //选择视频
//                .choose(MimeType.ofVideo())
                //自定义选择选择的类型
//                .choose(MimeType.of(MimeType.JPEG,MimeType.AVI))
                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
//                .showSingleMediaType(true)
                //这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 FileProvider
                .capture(true)
                .captureStrategy(new CaptureStrategy(true,"PhotoPicker"))
                //有序选择图片 123456...
                .countable(true)
                //最大选择数量为9
                .maxSelectable(maxSelectable)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                //选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //界面中缩略图的质量
                .thumbnailScale(0.8f)
                //蓝色主题
                .theme(R.style.Matisse_Zhihu)
                //黑色主题
//                .theme(R.style.Matisse_Dracula)
                //Glide加载方式
                .imageEngine(new GlideEngine())
                //Picasso加载方式
//                .imageEngine(new PicassoEngine())
                //请求码
                .forResult(REQUEST_CODE_CHOOSE);
    }

    private List<Uri> mSelected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            final List<String> paths = new ArrayList<>();
            LogUtils.d("Matisse", "mSelected: " + mSelected);

            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            for (int i = 0; i < mSelected.size(); i++) {
                //异步压缩文件
                Tiny.getInstance().source(mSelected.get(i)).asFile().withOptions(options).compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile, Throwable t) {
                        paths.add(outfile);
                        if (paths.size() == mSelected.size()) {
                            if (IS_USE_GLOBAL_CONFIG) {
                                /**
                                 * 使用全局配置上传图片  demo
                                 *
                                 * @param filePaths 图片路径
                                 */
                                mPresenter.postUploadImgWithGlobalConfig(paths, uploadUrl);
                            } else {
                                /**
                                 * 一次上传多张图片
                                 *
                                 * @param uploadPaths 图片路径
                                 */
                                mPresenter.postUploadImgs(paths, uploadUrl);
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void globalHttpSuccess(List<BannerBean> data) {
        String s = GsonUtils.toJson(data);
        LogUtils.e(TAG, DataUtils.strFormat("统一请求---使用全局配置的相关参数，success：\n%s", s));
        response.setText(DataUtils.strFormat("统一请求---使用全局配置的相关参数，success：\n%s", s));
    }

    @Override
    public void globalStringHttpSuccess(String data) {
        LogUtils.e(TAG, DataUtils.strFormat("统一请求---接受string，success：\n%s", data));
        response.setText(DataUtils.strFormat("统一请求---接受string，success：\n%s", data));
    }

    @Override
    public void multipleHttpSuccess(String data) {
        LogUtils.e(TAG, DataUtils.strFormat("链式发送多个请求，success：\n%s", data));
        response.setText(DataUtils.strFormat("链式发送多个请求，success：\n%s", data));
    }

    @Override
    public void changeUrlSuccess(Top250Bean top250Bean) {
        String s = GsonUtils.toJson(top250Bean);
        LogUtils.e(TAG, DataUtils.strFormat("使用豆瓣baseUrl请求数据，success：\n%s", s));
        response.setText(DataUtils.strFormat("使用豆瓣baseUrl请求数据，success：\n%s", s));
    }

    @Override
    public void downloadHttpSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
        IS_DOWNLOAD_FILE = false;
        response.setText("文件下载，success：\n下载中："+ progress +"%\n下载文件路径：" + filePath);

        if (done) {
            IS_DOWNLOAD_FILE = true;
            showToast("文件下载完成");
        }
    }

    @Override
    public void uploadImgWithGlobalConfigSuccess(String data) {
        response.setText(String.format("使用全局配置-上传图片，success:\n%s", data));
    }

    @Override
    public void uploadImgsSuccess(ResponseBody data) {
        String s = "";
        try {
            s = data.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtils.d(String.format("上传多张图片，success：\n%s",s));
        response.setText(String.format("上传单、多张图片，success：\n%s",s));
    }

    @Override
    public void globalHttpFail(int code, String message) {
        response.setText(DataUtils.strFormat("统一请求---使用全局配置的相关参数，error:\n%s", message));
    }

    @Override
    public void globalStringHttpFail(int code, String message) {
        response.setText(DataUtils.strFormat("统一请求---接受string，error:\n%s", message));
    }

    @Override
    public void multipleHttpFail(int code, String message) {
        response.setText(DataUtils.strFormat("链式发送多个请求，error:\n%s", message));
    }

    @Override
    public void changeUrlFail(int code, String message) {
        response.setText(DataUtils.strFormat("使用豆瓣baseUrl请求数据，error:\n%s", message));
    }

    @Override
    public void downloadHttpFail(int code, String message) {
        IS_DOWNLOAD_FILE = true;
        response.setText(DataUtils.strFormat("文件下载，error：\n%s", message));
    }

    @Override
    public void uploadImgWithGlobalConfigFail(int code, String message) {
        response.setText(DataUtils.strFormat("使用全局配置-上传图片，error：\n%s", message));
    }

    @Override
    public void uploadImgsFail(int code, String message) {
        response.setText(DataUtils.strFormat("上传单、多张图片，error：\n%s", message));
    }

    @Override
    public void onClicked(View v, int action, String extra) {
        if (action == CommonTitleBar.ACTION_RIGHT_BUTTON || action == CommonTitleBar.ACTION_RIGHT_TEXT) {
            new XPopup.Builder(this)
                    .hasShadowBg(false)
//                        .popupAnimation(PopupAnimation.NoAnimation) //NoAnimation表示禁用动画
//                        .isCenterHorizontal(true) //是否与目标水平居中对齐
//                        .offsetY(-10)
//                        .popupPosition(PopupPosition.Top) //手动指定弹窗的位置
                    .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                    .asAttachList(new String[]{"global http", "global string http", "multiple http", "douan api http",
                                    "download file", "download cancel", "upload one pic",
                                    "upload more pic", "upload pic with global config"},
                            new int[]{R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round,
                                    R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round,
                                    R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    LogUtils.e(TAG, "popup position：" + position);

                                    switch (position) {
                                        case 0:
                                            // 统一请求---使用全局配置的相关参数
                                            mPresenter.postGlobalHttp();
                                            break;
                                        case 1:
                                            // 统一请求---接受string
                                            mPresenter.postGlobalStringHttp();
                                            break;
                                        case 2:
                                            // 链式发送多个请求
                                            mPresenter.postMultipleHttp();
                                            break;
                                        case 3:
                                            // 使用豆瓣baseUrl请求数据
                                            mPresenter.postChangeUrl();
                                            break;
                                        case 4:
                                            if (IS_DOWNLOAD_FILE) {
                                                mPresenter.postDownloadHttp(url, fileName);
                                            }
                                            break;
                                        case 5:
                                            if (!IS_DOWNLOAD_FILE) {
                                                RxHttpUtils.cancel("download");
//                                                RxHttpUtils.cancelAll();
                                            }
                                            break;
                                        case 6:
                                            MAX_SELECTABLE = 1;
                                            IS_USE_GLOBAL_CONFIG = false;
                                            selectPhotoWithPermission();
                                            break;
                                        case 7:
                                            MAX_SELECTABLE = 9;
                                            IS_USE_GLOBAL_CONFIG = false;
                                            selectPhotoWithPermission();
                                            break;
                                        case 8:
                                            MAX_SELECTABLE = 9;
                                            IS_USE_GLOBAL_CONFIG = true;
                                            selectPhotoWithPermission();
                                            break;
                                    }
                                }
                            })
                    .show();
        }
    }
}
