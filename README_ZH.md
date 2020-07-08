# RxImagePicker

#### [English Documentation](https://github.com/qingmei2/RxImagePicker) | 中文文档

<p/>

<a target="_blank" href="https://bintray.com/mq2553299/maven/rximagepicker"><img
        src="https://api.bintray.com/packages/mq2553299/maven/rximagepicker/images/download.svg?version=3.0.0-beta02"></a>(RxJava3)
<a target="_blank" href='https://bintray.com/mq2553299/maven/rximagepicker/2.5.4/link'><img
        src='https://api.bintray.com/packages/mq2553299/maven/rximagepicker/images/download.svg?version=2.5.6'></a>(RxJava2)
<a target="_blank"><img src="https://img.shields.io/badge/API-16+-green.svg"></a>
<a target="_blank" href="https://github.com/qingmei2/RxImagePicker/blob/2.x/LICENSE"><img src="https://img.shields.io/badge/License-MIT-blue.svg"></a>

## 通知（重要）

* **[详细文档，包括进阶使用和拓展功能,请点击查看wiki！](https://github.com/qingmei2/RxImagePicker/wiki)**
* **[常见问题](https://github.com/qingmei2/RxImagePicker/wiki/常见问题)** : 提issue之前查看一下，也许能节省您很多的时间！  
* **[更新日志](https://github.com/qingmei2/RxImagePicker/wiki/ChangeLog)** : 升级库之前，请查看新版本关于API有可能出现的变动。

> 自`2.3.0`版本之后，库只提供对官方`AndroidX`的支持，详情请参考Android官方文档[AndroidX Overview](https://developer.android.com/jetpack/androidx/).

> `RxImagePicker`提供了对`RxJava2`和`RxJava3`的支持：

* 2.x版本: Kotlin实现，支持AndroidX和RxJava2;
* 3.x版本: Kotlin实现，支持AndroidX和RxJava3;

最新版本号请参考顶部标签信息。

<h2 id="overview">简介</h2>

RxImagePicker存在的目的是：让开发者能够**简单**并且**灵活**的方式实现Android开发中**图片选择**的需求。

它与传统的其他三方的图片选择库不同的是，后者通过跳转到固定的Activity进行图片选择，并不能满足所有应用的需求；以QQ聊天会话界面的图片选择功能为例，这种特殊的UI需求很难依靠图片选择库实现，开发者只能去自己实现。

一旦和目前项目的需求有了冲突（修改或者添加），开发者考虑的不应该是【这个库实现不了，干脆换一个库吧】或者【不管这个库，我去再单独实现一个】，而是，**基于同一个图片选择框架，修改或者添加对应配置的接口**。

![screenshot_qq](https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_qq.png)

RxImagePicker是一个用于Android的响应式图片选择器，它将您的图片选择需求转换为一个接口进行配置，并在任何一个Activity或者Fragment中展示任何样式的图片选择UI。

RxImagePicker的设计起源，请参考：

> [RxImagePicker:从零实现灵活且可高度定制的Android图片选择架构](https://www.jianshu.com/p/fecf3a13e615)

RxImagePicker的UI自动化测试，请参考：

> [全副武装！AndroidUI自动化测试在RxImagePicker中的实践历程](https://www.jianshu.com/p/6b78f6f93430)


### 支持的功能

- [x] Android Camera拍照
- [x] Android 图片选择
- [x] 以响应式数据流的格式返回数据（支持Observable/Flowable/Single/Maybe）
- [x] 支持拓展数据，比如用于实现微信 **发送原图** 等功能，完全由您自定义灵活进行配置
- [x] 覆盖全面的**UI自动化测试**，详见下方示例截图。
- [x] 对[AndroidX](https://developer.android.com/jetpack/androidx/)的支持（after v2.3.0+）。

### UI的支持

除了Android系统默认的图片选择样式，你可以选择使用这些额外的UI支持：

- [x] 系统级别图片选择器
- [x] 知乎主题图片选择器
- [x] 微信主题图片选择器
- [x] 自定义UI图片选择器

<h3 id="sample_screenshots">示例截图</h2>

### 系统图片选择和结果展示

<div align:left;display:inline;>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_sysytem.png"/>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_result.png"/>
</div>

### 知乎主题

<div align:left;display:inline;>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_zhihu_dracula.png"/>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_zhihu_normal.png"/>
</div>


### 微信主题
<div align:left;display:inline;>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_wechat.png"/>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_wechat_expand.png"/>
</div>

想要 **快速实现上图所示效果** 请点击[这里](https://github.com/qingmei2/RxImagePicker/wiki)查看wiki中的详细文档。

### UI自动化测试

<div align:left;display:inline;>
<img width="235" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/rximagepicker_test.gif"/>
</div>

<div align:left;display:inline;>
<img width="608" height="175" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/test_result.png"/>
</div>

测试代码的覆盖，保证了每个新版本的稳定性，以及程序的健壮性。

<h2 id="usage">基础使用</h2>

### 1. 添加依赖在Module的build.gradle文件中：

如果您的项目已经迁移了`AndroidX`，建议依赖最新版本,`last_version`版本号请参考文档顶部版本信息标签；

```groovy
// 最基础的架构，仅提供了系统默认的图片选择和拍照功能
compile 'com.github.qingmei2:rximagepicker:${last_version}'

// 提供了自定义UI图片选择器的基本组件，自定义UI的需求需要添加该依赖
compile 'com.github.qingmei2:rximagepicker_support:${last_version}'

// 如果需要额外的UI支持，请选择依赖对应的UI拓展库
compile 'com.github.qingmei2:rximagepicker_support_zhihu:${last_version}'     // 知乎图片选择器
compile 'com.github.qingmei2:rximagepicker_support_wechat:${last_version}'    // 微信图片选择器
```

> 如果您的项目没有迁移`AndroidX`(即v7包的包名是`com.android.support`而非`androidx.appcompat`),请继续使用`2.2.0`的稳定版本，否则包名不同会导致编译错误！

### 2. 接口配置

声明一个接口，并进行基础的配置：

```java
public interface MyImagePicker {

    @Gallery    // 打开相册选择图片
    Observable<Result> openGallery(Context context);

    @Camera     // 打开相机拍照
    Observable<Result> openCamera(Context context);
}
```

### 3. 实例化接口并使用它

在您的Activity或者Fragment中实例化该接口，以打开系统默认的相册/相机界面：

```java
//打开系统默认的图片选择器
private void onButtonClick() {
    RxImagePicker
            .create(MyImagePicker.class)
            .openGallery(this)
            .subscribe(new Consumer<Result>() {
                @Override
                public void accept(Result result) throws Exception {
                    Uri uri = result.getUri();
                    // 对图片进行处理，比如加载到ImageView中
                    GlideApp.with(this)
                             .load(uri)
                             .into(ivPickedImage);
                }
            });
}
```

## 详细文档

**[关于RxImagePicker更详细的使用说明，进阶使用和拓展功能,请参考wiki！](https://github.com/qingmei2/RxImagePicker/wiki)**

## 更多疑问

如果对于RxImagePicker的使用有疑问或者建议，**请提issue**.

## 其他RxJava项目

* [RxWeaver: A lightweight and flexible error handler tools of RxJava2.](https://github.com/qingmei2/RxWeaver)

* [MVVM-Rhine: The MVVM using RxJava and Android databinding.](https://github.com/qingmei2/MVVM-Rhine)

## 参考

在 [RxImagePicker](https://github.com/qingmei2/RxImagePicker) 的设计过程中，我参考了很多的三方库，它们源码中优秀的  **设计思想** 或者 **架构设计** ，都给与了我很大的启示，在此深表感谢，感谢诸多开源爱好者前辈们的无私精神：

* [Retrofit:Type-safe HTTP client for Android and Java by Square, Inc. ](https://github.com/square/retrofit)  
	Retrofit是聚集了诸多优秀的 **设计思想** 和 **架构设计** 于一身的库，它将复杂多样的 **网络请求** 需求转换成了接口供开发者配置，我认为RxImagePicker也可以这样做。

* [RxJava:a library for composing asynchronous and event-based programs using observable sequences for the Java VM.](https://github.com/ReactiveX/RxJava)  
	一个在 Java VM 上使用可观测的序列来组成异步的、基于事件的程序的库, 随着程序逻辑变得越来越复杂，它依然能够保持简洁。它是构成RxImagePicker的 **基础组件** 。

* [RxAndroid:RxJava bindings for Android](https://github.com/ReactiveX/RxAndroid)  
	RxJava的一个扩展库，更好的兼容了Android特性，比如主线程，UI事件等。它是构成RxImagePicker的 **基础组件** 。

* [RxImagePicker:Android. Pick image from camera or gallery using RxJava2](https://github.com/MLSDev/RxImagePicker)  
* [RxPermissions:Android runtime permissions powered by RxJava2](https://github.com/tbruyelle/RxPermissions)  
* [RxLifecycle:Lifecycle handling APIs for Android apps using RxJava](https://github.com/trello/RxLifecycle)  
	这三个优秀的RxJava拓展库，在数据的传递方案上，给与了我很大的启示。
* [RxCache:Reactive caching library for Android and Java](https://github.com/VictorAlbertos/RxCache)  
	优秀的RxJava拓展库，用于处理RxJava的数据缓存，库底层通过Dagger完成对配置的处理，我借鉴在了RxImagePicker中。
* [Dagger2：A fast dependency injector for Android and Java.](https://github.com/google/dagger)  
	Google优秀的依赖注入框架，0.4.0版本之前，它是构成RxImagePicker的 **基础组件** 。
* [Matisse:A well-designed local image and video selector for Android](https://github.com/zhihu/Matisse)  
	知乎开源，优秀的Android图片选择库，拥有非常 **Material Design** 的设计。在RxImagePicker中，Matisse被抽出来放入了RxImagePicker_Support，成为了 **UI层的基础组件** 。

## 混淆

> -keep class com.qingmei2.rximagepicker.**{*;}

## Contributors

* [13kmsteady](https://github.com/13kmsteady)
* [guoleifei](https://github.com/guoleifei)

License
-------

    The RxImagePicker：MIT License

    Copyright (c) 2018 qingmei2

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
