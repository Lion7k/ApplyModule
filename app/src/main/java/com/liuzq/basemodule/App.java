package com.liuzq.basemodule;

import android.content.Context;
import android.text.TextUtils;

import com.liuzq.basemodule.url.AppUrlConfig;
import com.liuzq.commlibrary.base.BaseApplication;
import com.liuzq.commlibrary.utils.PreferencesUtils;
import com.liuzq.commlibrary.utils.ToastUtils;
import com.liuzq.rxhttp.RxHttpUtils;
import com.liuzq.rxhttp.config.OkHttpConfig;
import com.liuzq.rxhttp.cookie.store.SPCookieStore;
import com.liuzq.rxhttp.interfaces.BuildHeadersListener;
import com.liuzq.rxhttp.manager.RxUrlManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.zxy.tiny.Tiny;
//import com.zxy.tiny.Tiny;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by liuzq on 2018/10/18.
 */

public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
        PreferencesUtils.initPrefs(this);
        Tiny.getInstance().init(this);
        initRxHttpUtils();
        crashReport();
    }

    /**
     * 全局请求的统一配置（以下配置根据自身情况选择性的配置即可）
     */
    private void initRxHttpUtils() {

        //一个项目多url的配置方法
        RxUrlManager.getInstance().setMultipleUrl(AppUrlConfig.getAllUrl());

        RxHttpUtils
                .getInstance()
                .init(this)
                .config()
                //自定义factory的用法
                //.setCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //.setConverterFactory(ScalarsConverterFactory.create(),GsonConverterFactory.create(GsonAdapter.buildGson()))
                //配置全局baseUrl
                .setBaseUrl("https://www.wanandroid.com/")
                //开启全局配置
                .setOkClient(createOkHttp());

//        TODO: 2018/5/31 如果以上OkHttpClient的配置满足不了你，传入自己的 OkHttpClient 自行设置
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//
//        builder
//                .addInterceptor(log_interceptor)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .connectTimeout(10, TimeUnit.SECONDS);
//
//        RxHttpUtils
//                .getInstance()
//                .init(this)
//                .config()
//                .setBaseUrl(BuildConfig.BASE_URL)
//                .setOkClient(builder.build());

    }

    private OkHttpClient createOkHttp() {
        //        获取证书
//        InputStream cerInputStream = null;
//        InputStream bksInputStream = null;
//        try {
//            cerInputStream = getAssets().open("YourSSL.cer");
//            bksInputStream = getAssets().open("your.bks");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        OkHttpClient okHttpClient = new OkHttpConfig
                .Builder(this)
                //添加公共请求头
                .setHeaders(new BuildHeadersListener() {
                    @Override
                    public Map<String, String> buildHeaders() {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("appVersion", BuildConfig.VERSION_NAME);
                        hashMap.put("client", "android");
                        hashMap.put("token", "your_token");
                        hashMap.put("other_header", URLEncoder.encode("中文需要转码"));
                        return hashMap;
                    }
                })
                //添加自定义拦截器
                //.setAddInterceptor()
                //开启缓存策略(默认false)
                //1、在有网络的时候，先去读缓存，缓存时间到了，再去访问网络获取数据；
                //2、在没有网络的时候，去读缓存中的数据。
                .setCache(true)
                .setHasNetCacheTime(10)//默认有网络时候缓存60秒
                //全局持久话cookie,保存到内存（new MemoryCookieStore()）或者保存到本地（new SPCookieStore(this)）
                //不设置的话，默认不对cookie做处理
                .setCookieType(new SPCookieStore(this))
                //可以添加自己的拦截器(比如使用自己熟悉三方的缓存库等等)
                //.setAddInterceptor(null)
                //全局ssl证书认证
                //1、信任所有证书,不安全有风险（默认信任所有证书）
                //.setSslSocketFactory()
                //2、使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(cerInputStream)
                //3、使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(bksInputStream,"123456",cerInputStream)
                //设置Hostname校验规则，默认实现返回true，需要时候传入相应校验规则即可
                //.setHostnameVerifier(null)
                //全局超时配置
                .setReadTimeout(10)
                //全局超时配置
                .setWriteTimeout(10)
                //全局超时配置
                .setConnectTimeout(10)
                //全局是否打开请求log日志
                .setDebug(BuildConfig.DEBUG)
                .build();

        return okHttpClient;
    }

    private void crashReport(){
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "99599d1225", BuildConfig.DEBUG, strategy);
        // 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
        // CrashReport.initCrashReport(context, strategy);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
