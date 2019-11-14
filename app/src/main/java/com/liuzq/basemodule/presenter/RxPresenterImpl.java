package com.liuzq.basemodule.presenter;

import android.content.Context;

import com.liuzq.basemodule.api.ApiHelper;
import com.liuzq.basemodule.api.DouBanApi;
import com.liuzq.basemodule.api.WanAndroidApi;
import com.liuzq.basemodule.bean.BannerBean;
import com.liuzq.basemodule.bean.Top250Bean;
import com.liuzq.basemodule.contract.RxContract;
import com.liuzq.basemodule.presenter.base.BasePresenterImpl;
import com.liuzq.commlibrary.utils.LogUtils;
import com.liuzq.rxhttp.RxHttpUtils;
import com.liuzq.rxhttp.bean.BaseData;
import com.liuzq.rxhttp.download.DownloadObserver;
import com.liuzq.rxhttp.interceptor.Transformer;
import com.liuzq.rxhttp.interfaces.ILoadingView;
import com.liuzq.rxhttp.observer.CommonObserver;
import com.liuzq.rxhttp.observer.DataObserver;
import com.liuzq.rxhttp.observer.StringObserver;
import com.liuzq.rxhttp.upload.UploadHelper;
import com.liuzq.rxhttp.widget.LoadingDialog;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class RxPresenterImpl extends BasePresenterImpl implements RxContract.Presenter {

    private final String TAG = this.getClass().getSimpleName();

    /**
     * 全局方式请求
     * 统一请求---使用全局配置的相关参数
     */
    @Override
    public void postGlobalHttp() {
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
                        mView.globalHttpFail(0, errorMsg);
                    }

                    @Override
                    protected void onSuccess(List<BannerBean> data) {
                        String s = data.get(0).toString();
                        LogUtils.e(TAG, s);
                        mView.globalHttpSuccess(data);
                    }
                });
    }

    /**
     * 统一请求---接受string
     */
    @Override
    public void postGlobalStringHttp() {
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
                        mView.globalStringHttpFail(0, errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        mView.globalStringHttpSuccess(data);
                    }
                });
    }

    /**
     * 链式发送多个请求
     */
    @Override
    public void postMultipleHttp() {
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
                        mView.multipleHttpFail(0, errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        mView.multipleHttpSuccess(data);
                    }
                });
    }

    /**
     * 使用豆瓣baseUrl请求数据
     */
    @Override
    public void postChangeUrl() {
        ApiHelper.getDouBanApi()
                .getTop250(5)
                .compose(Transformer.<Top250Bean>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<Top250Bean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        mView.changeUrlFail(0, errorMsg);
                    }

                    @Override
                    protected void onSuccess(Top250Bean top250Bean) {
                        mView.changeUrlSuccess(top250Bean);
                    }
                });
    }

    /**
     * 文件下载
     *
     * @param fileUrl
     * @param fileName
     */
    @Override
    public void postDownloadHttp(String fileUrl, String fileName) {
        RxHttpUtils.downloadFile(fileUrl)
                .subscribe(new DownloadObserver(fileName) {
                    //可以去下下载
                    @Override
                    protected String setTag() {
                        return "download";
                    }

                    @Override
                    protected void onError(String errorMsg) {
                        mView.downloadHttpFail(0, errorMsg);
                    }

                    @Override
                    protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                        mView.downloadHttpSuccess(bytesRead, contentLength, progress, done, filePath);
                    }
                });
    }

    /**
     * 使用全局配置上传图片  demo
     *
     * @param filePaths 图片路径
     */
    @Override
    public void postUploadImgWithGlobalConfig(List<String> filePaths, String uploadUrl) {
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
                        mView.uploadImgWithGlobalConfigFail(0, errorMsg);
                    }

                    @Override
                    protected void onSuccess(String data) {
                        mView.uploadImgWithGlobalConfigSuccess(data);
                    }
                });
    }

    /**
     * 一次上传多张图片
     *
     * @param uploadPaths 图片路径
     */
    @Override
    public void postUploadImgs(List<String> uploadPaths, String uploadUrl) {
        RxHttpUtils.uploadImages(uploadUrl, uploadPaths)
                .compose(Transformer.<ResponseBody>switchSchedulers(loading_dialog))
                .subscribe(new CommonObserver<ResponseBody>() {
                    @Override
                    protected void onError(String errorMsg) {
                        mView.uploadImgsFail(0, errorMsg);
                    }

                    @Override
                    protected void onSuccess(ResponseBody responseBody) {
                        mView.uploadImgsSuccess(responseBody);
                    }
                });
    }


    protected RxContract.View mView;
    protected Context mContext;
    protected ILoadingView loading_dialog;

    @Override
    public void attachView(RxContract.View  view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void attachContext(Context mContext) {
        loading_dialog = new LoadingDialog(mContext);
        this.mContext = mContext;
    }
}
