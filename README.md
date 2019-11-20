# ApplyModule
助力打造一个搭建android应用框架的工具模块，包含RxHttp(RxJava2 + Retrofit2 + OkHttp3)网络请求、Multistate Layout多状态布局（error、empty、loading、content）、
Bottom Bar一个自定义的视图组件，模仿新材料设计底部导航模式、非常实用的Utils类。

 ### 特点
   * 1.基于RxJava2和Retrofit2重构，便捷使用，例子是采用MVP模式，Activity和Fragment均进行封装处理。
   * 2.重写FrameLayout，构建多状态布局。
   * 3.重写LinearLayout，构建Bottom Bar。
   * 4.本着省事的原则，将平时用到的Utils类进行归类。

# 如何使用它

## Step 1.先在 build.gradle(Project:XXXX) 的 repositories 添加:

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

## Step 2. 然后在 build.gradle(Module:app) 的 dependencies 添加:

```gradle
dependencies {
	//基础工具库（必选）
 	implementation 'com.github.Lion7k.ApplyModule:commlibrary:v1.0.1'

	//rxhttp网络请求库（可选）
	implementation 'com.github.Lion7k.ApplyModule:rxhttp:v1.0.1'

	//多状态布局库（可选）
	implementation 'com.github.Lion7k.ApplyModule:statusview:v1.0.1'

	//定义的视图组件，模仿新材料设计底部导航模式（可选）
 	implementation 'com.github.Lion7k.ApplyModule:bottombar:v1.0.1'
}
```
## Step 3. 在application类里边进行初始化配置
> ##### 在自己的Application的onCreate方法中进行初始化配置
```
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
        PreferencesUtils.initPrefs(this);
        initRxHttpUtils();
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
}
```
