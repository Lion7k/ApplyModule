package com.liuzq.httplibrary.upload;

import com.liuzq.httplibrary.factory.ApiFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by liuzq on 2018/10/19.
 * desc    : 为上传单独建一个retrofit
 */

public class UploadHelper {

    /**
     * 上传一张图片
     *
     * @param uploadUrl 上传图片的服务器url
     * @param filePath 图片路径
     * @return List<MultipartBody.Part>
     */
    public static Observable<ResponseBody> uploadImage(String uploadUrl,String filePath) {
        List<String> filePaths = new ArrayList<>();
        filePaths.add(filePath);
        return uploadFilesWithParams(uploadUrl,"uploaded_file", null, filePaths);
    }

    /**
     * 只上传图片
     *
     * @param uploadUrl 上传图片的服务器url
     * @param filePaths 图片路径
     * @return List<MultipartBody.Part>
     */
    public static Observable<ResponseBody> uploadImages(String uploadUrl,List<String> filePaths) {
        return uploadFilesWithParams(uploadUrl,"uploaded_file", null, filePaths);
    }

    /**
     * 图片和参数同时上传的请求
     *
     * @param uploadUrl 上传图片的服务器url
     * @param fileName  后台协定的接受图片的name（没特殊要求就可以随便写）
     * @param paramsMap 普通参数
     * @param filePaths 图片路径
     * @return Observable<ResponseBody>
     */
    public static Observable<ResponseBody> uploadFilesWithParams(String uploadUrl, String fileName, Map<String, Object> paramsMap, List<String> filePaths){
        String DEFAULT_UPLOAD_KEY = "defaultUploadUrlKey";
        String DEFAULT_BASE_URL = "https://api.github.com/";
        List<MultipartBody.Part> parts = uploadFilesWithParams(fileName, paramsMap, filePaths);
        return ApiFactory.getInstance().createApi(DEFAULT_UPLOAD_KEY, DEFAULT_BASE_URL, UploadFileApi.class)
                .uploadFiles(uploadUrl, parts);
    }

    /**
     * 图片和参数同时上传的请求
     *
     * @param fileName  后台协定的接受图片的name（没特殊要求就可以随便写）
     * @param paramsMap 普通参数
     * @param filePaths 图片路径
     * @return List<MultipartBody.Part>
     */
    public static List<MultipartBody.Part> uploadFilesWithParams(String fileName, Map<String, Object> paramsMap, List<String> filePaths) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (null != paramsMap) {
            for (String key : paramsMap.keySet()) {
                builder.addFormDataPart(key, (String) paramsMap.get(key));
            }
        }

        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i));
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            //"fileName"+i 后台接收图片流的参数名
            builder.addFormDataPart(fileName, file.getName(), imageBody);
        }

        List<MultipartBody.Part> parts = builder.build().parts();
        return parts;
    }
}
