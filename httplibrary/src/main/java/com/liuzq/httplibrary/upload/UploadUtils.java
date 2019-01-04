package com.liuzq.httplibrary.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by liuzq on 2018/10/19.
 * 上传图片 全局处理方式
 */

public class UploadUtils {

    /**
     * 上传一张图片
     *
     * @param filePath 图片路径
     * @return List<MultipartBody.Part>
     */
    public static List<MultipartBody.Part> uploadImage(String filePath) {
        List<String> filePaths = new ArrayList<>();
        filePaths.add(filePath);
        return uploadFilesWithParams("uploaded_file", null, filePaths);
    }

    /**
     * 只上传图片
     *
     * @param filePaths 图片路径
     * @return List<MultipartBody.Part>
     */
    public static List<MultipartBody.Part> uploadImages(List<String> filePaths) {
        return uploadFilesWithParams("uploaded_file", null, filePaths);
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
