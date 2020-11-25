# ApplyModule
助力打造一个搭建android应用框架的工具模块组，包含RxHttp(RxJava2 + Retrofit2 + OkHttp3)网络请求、Multistate Layout多状态布局（error、empty、loading、content）、Bottom Bar一个自定义的视图组件，模仿新材料设计底部导航模式、类似商城加减控件、流式布局单选多选、搜索控件、指纹识别、非常实用的Utils类。

 ### 特点
   * 1.基于RxJava2和Retrofit2重构，便捷使用，例子是采用MVP模式，Activity和Fragment均进行封装处理。
   * 2.重写FrameLayout，构建多状态布局。
   * 3.重写LinearLayout，构建Bottom Bar底部导航模式。
   * 4.重写LinearLayout，构建类似商城加减控件。
   * 5.继承ViewGroup，自定义FlowLayout构建实用单选、多选组件。
   * 6.重写LinearLayout，构建常用搜索功能控件。
   * 7.适配大部分机型指纹识别功能（三星、魅族厂商提供专有SDK）。
   * 8.本着省事的原则，将平时用到的Utils类进行归类。

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
 	implementation 'com.github.Lion7k.ApplyModule:commlibrary:v1.0.2'

	//rxhttp网络请求库（可选）
	implementation 'com.github.Lion7k.ApplyModule:rxhttp:v1.0.2'

	//多状态布局库（可选）
	implementation 'com.github.Lion7k.ApplyModule:statusview:v1.0.2'

	//定义的视图组件，模仿新材料设计底部导航模式（可选）
 	implementation 'com.github.Lion7k.ApplyModule:bottombar:v1.0.2'
	
	//定义的视图组件，类似商城加减控件（可选）
 	implementation 'com.github.Lion7k.ApplyModule:addsubtractview:v1.0.2'
	
	//定义的视图组件，流式布局构建单选、多选功能（可选）
 	implementation 'com.github.Lion7k.ApplyModule:flowlayout:v1.0.2'
	
	//定义的视图组件，搜索功能控件（可选）
 	implementation 'com.github.Lion7k.ApplyModule:searchbox:v1.0.2'
	
	//适配大部分机型指纹识别（可选）
 	implementation 'com.github.Lion7k.ApplyModule:fingerprintidentify:v1.0.2'
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
    }
}
```

### 详细描述各个模块使用方式:

