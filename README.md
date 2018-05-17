Varys - Whisper
=====
<!-- MarkdownTOC -->

- [介绍](#%E4%BB%8B%E7%BB%8D)
    - [Varys](#varys)
    - [Whisper](#whisper)
        - [已实现功能](#%E5%B7%B2%E5%AE%9E%E7%8E%B0%E5%8A%9F%E8%83%BD)
        - [未实现部分](#%E6%9C%AA%E5%AE%9E%E7%8E%B0%E9%83%A8%E5%88%86)
- [使用](#%E4%BD%BF%E7%94%A8)
    - [引入 Whisper](#%E5%BC%95%E5%85%A5-whisper)
        - [引入包](#%E5%BC%95%E5%85%A5%E5%8C%85)
        - [指定数据上传服务器](#%E6%8C%87%E5%AE%9A%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BC%A0%E6%9C%8D%E5%8A%A1%E5%99%A8)
        - [引入 Whisper 依赖的第三方库配置](#%E5%BC%95%E5%85%A5-whisper-%E4%BE%9D%E8%B5%96%E7%9A%84%E7%AC%AC%E4%B8%89%E6%96%B9%E5%BA%93%E9%85%8D%E7%BD%AE)
            - [realm](#realm)
        - [Proguard](#proguard)
    - [使用 Whisper](#%E4%BD%BF%E7%94%A8-whisper)
- [已知 Issue](#%E5%B7%B2%E7%9F%A5-issue)
    - [改进](#%E6%94%B9%E8%BF%9B)
        - [包大小](#%E5%8C%85%E5%A4%A7%E5%B0%8F)
        - [Https 的使用](#https-%E7%9A%84%E4%BD%BF%E7%94%A8)
    - [Bug](#bug)
        - [Whisper 调用多次](#whisper-%E8%B0%83%E7%94%A8%E5%A4%9A%E6%AC%A1)
- [依赖第三方库](#%E4%BE%9D%E8%B5%96%E7%AC%AC%E4%B8%89%E6%96%B9%E5%BA%93)
- [References](#references)

<!-- /MarkdownTOC -->

## 介绍
 
__最新版本__： 0.1.0-SNAPSHOT  


### Varys

Varys - 监控平台  
Whisper - 移动端的数据收集 SDK

运维场景 （APM）

1. 业务上
    - 用户画像
    - 灰度包、A/B 测试结果 收集
    - App 包监控：稳定性、性能体验、用户舆情、核心业务
2. 开发支持
    - 版本修复 (Hotfix 支持等)
    - 监控报警 (Crash 平台)
    - 性能指标记录

### Whisper

Whisper 是用作埋点的客户端 lib ／ SDK。  

#### 已实现功能  

1. 数据定义
* 数据存储
* 特定数据收集
    - 设备信息
    - Business code
* 数据定期发送
* Crash 信息收集

#### 未实现部分

1. 现在使用的是固定 Token 放在 Http 头中做验证，需要改进
* 需要在应用中显式调用，没有实现自动埋点

## 使用
### 引入 Whisper

#### 引入包  
app module 的 `build.gradle` 中引入  
```groovy
repositories {
    maven {
        url "https://oss.sonatype.org/content/groups/public/"
    }
}

dependencies {
    compile 'io.buzznerd:whisper:0.1.0-SNAPSHOT'
}
```

#### 指定数据上传服务器
在 `AndroidManifest.xml` 中添加
```xml
<!-- whisper server address -->
<meta-data
    android:name="WHISPER_SERVER_ADDRESS"
    android:value="https://120.76.128.139:8463/device/"/>
```

#### 引入 Whisper 依赖的第三方库配置
使用了第三方的开源库： Realm、Retrofit
##### realm
工程的 `build.gradle` 中引入  
```groovy
buildscript {
    dependencies {
        classpath "io.realm:realm-gradle-plugin:2.1.1"
    }
}
```

#### Proguard
在 AAR 中包含有 proguard 文件，会自动集成到项目中。所以不需要单独指定 proguard 配置.

### 使用 Whisper
1） 初始化  
在使用前使用 `Whisper.Builder` 初始化 Whisper，并调用 init(context) 完成初始化，之后就可以直接使用。  

最通常的做法是在 Application 的 `OnCreate` 方法中完成。  
```java
Whisper.Builder whisperBuilder = new Whisper.Builder();
if (!BuildConfig.DEBUG) {
    whisperBuilder.setLogLevel(Whisper.LogLevel.NONE);
}
Whisper whisper = whisperBuilder.build();
whisper.init(this); 
```

2) 记录日志

预定义的事件:  
```java
Whisper.getDefaultInstance().logAppStart(); // app start

Whisper.getDefaultInstance().logUserLogin(userName); // login
Whisper.getDefaultInstance().logUserLogout(userName); // logout

// best is put in base activity onResume and onPause method, to track page viewing events
Whisper.getDefaultInstance().logOnResume(context); // page show
Whisper.getDefaultInstance().logOnPause(context); // page hide

// self-defined session, can only start one at the same time
Whisper.getDefaultInstance().startSession(context, userName); // start session
Whisper.getDefaultInstance().endSession(); // end session

// log business code, code definition refer to Handover code definition
Whisper.getDefaultInstance().logOkchemBizInApplication(String okchemBizHitCode); // none session
Whisper.getDefaultInstance().logOkchemBizInSession(String okchemBizHitCode));

// self-defined log info
Whisper.getDefaultInstance().logApplicationEvent(String target, String action, int actionType, String extra); // none session event
Whisper.getDefaultInstance().logSessionEvent(String target, String action, int actionType, String extra); // current session event
```

其他调用：
```java
// 同步数据，默认会过段时间自动同步
Whisper.getDefaultInstance().doUpload(null); // 立即发送本地log, 实现参数可以处理返回结果.
```

3) 记录查看  
web 端可以查看统计

4) 数据定义
ActionType Code

```java
/**
 * default hit action type
 */
public static final int ACTION_TYPE_NORMAL = 0;

/**
 * hit action: app start
 */
public static final int ACTION_TYPE_START_APP = 100;

/**
 * hit action: page resume
 */
public static final int ACTION_PAGE_RESUME = 101;

/**
 * hit action: page pause
 */
public static final int ACTION_PAGE_PAUSE = 102;

/**
 * hit action: start a new session
 */
public static final int ACTION_START_SESSION = 103;

/**
 * hit action: end current session
 */
public static final int ACTION_END_SESSION = 104;

/**
 * hit action: user login
 */
public static final int ACTION_USER_LOGIN = 105;

/**
 * hit action: user logout
 */
public static final int ACTION_USER_LOGOUT = 106;

/**
 * hit action: okchem biz event
 */
public static final int ACTION_OKCHEM_HIT = 200;

/**
 * hit action: exception caught
 */
public static final int ACTION_EXCEPTION = 400;
```

## 已知 Issue

### 改进
#### 包大小
由于依赖 realm，包含 .so 文件后，应用打包之后会有 6.7mb  

解决:  
在 app 模块中 build.gradle 中指定 .so 种类，去除几种。可以降到 3.3mb （如果再去掉 x86, 可降到 2.1mb）
```groovy
android {
    buildTypes {
        release {
            ndk {
                abiFilters 'armeabi', 'armeabi-v7a', 'armeabi-v8a', 'x86' 
                // available option:  'x86_64', 'mips', 'mips64'
            }
        }
    }
}
```

#### Https 的使用
现在自动屏蔽了 Https 的验证，方便快速使用 

解决：server 地址使用真正的 Https 然后客户端 Whisper 去掉屏蔽。

### Bug
#### Whisper 调用多次
当应用添加有推送时，Whisper 会同时调用两次 init

## 依赖第三方库

1. Retrofit
2. Realm Android

## References
参考／借鉴了 

1. [Umeng 应用统计](http://mobile.umeng.com/analytics)
2. [Google Analytics Platform Principles](https://analyticsacademy.withgoogle.com/course/2/unit/2/lesson/3)

