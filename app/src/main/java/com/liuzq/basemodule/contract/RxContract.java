package com.liuzq.basemodule.contract;

import com.liuzq.basemodule.bean.BannerBean;
import com.liuzq.basemodule.bean.Top250Bean;
import com.liuzq.commlibrary.base.BasePresenter;
import com.liuzq.commlibrary.base.BaseView;

import java.util.List;

import okhttp3.ResponseBody;

/**
 * mvp 模式
 */
public class RxContract {

    public interface View extends BaseView {

        void globalHttpSuccess(List<BannerBean> data);

        void globalStringHttpSuccess(String data);

        void multipleHttpSuccess(String data);

        void changeUrlSuccess(Top250Bean top250Bean);

        void downloadHttpSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath);

        void uploadImgWithGlobalConfigSuccess(String data);

        void uploadImgsSuccess(ResponseBody data);

        void globalHttpFail(int code, String message);

        void globalStringHttpFail(int code, String message);

        void multipleHttpFail(int code, String message);

        void changeUrlFail(int code, String message);

        void downloadHttpFail(String message);

        void uploadImgWithGlobalConfigFail(int code, String message);

        void uploadImgsFail(int code, String message);
    }

    public interface Presenter extends BasePresenter<View> {

        void postGlobalHttp();

        void postGlobalStringHttp();

        void postMultipleHttp();

        void postChangeUrl();

        void postDownloadHttp(String fileUrl, String fileName);

        void postUploadImgWithGlobalConfig(List<String> filePaths, String uploadUrl);

        void postUploadImgs(List<String> uploadPaths, String uploadUrl);
    }
}
