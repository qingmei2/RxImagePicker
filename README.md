# RxImagePicker

<div style="width:100%;display: flex;height:30px;">
    <img style="margin-right:20px;" src="https://api.bintray.com/packages/mq2553299/maven/rximagepicker/images/download.svg"/>
</div>

RxImagePicker的设计起源，请参考我的[这篇文章](https://www.jianshu.com/p/fecf3a13e615)

## 通知（重要）
  
* **[详细文档，包括进阶使用和拓展功能,请点击查看wiki！](https://github.com/qingmei2/RxImagePicker/wiki)**
* **[常见问题](https://github.com/qingmei2/RxImagePicker/wiki/常见问题)** : 提issue之前查看一下，也许能节省您很多的时间！  
* **[更新日志](https://github.com/qingmei2/RxImagePicker/wiki/ChangeLog)** : 升级库之前，请查看新版本关于API有可能出现的变动,**最新版本v2.1.1**。

<h2 id="overview">简介</h2>

RxImagePicker存在的目的是：让开发者能够**简单**并且**灵活**的方式实现Android开发中**图片选择**的需求。

它与传统的其他三方的图片选择库不同的是，后者通过跳转到固定的Activity进行图片选择，并不能满足所有应用的需求；以QQ聊天会话界面的图片选择功能为例，这种特殊的UI需求很难依靠图片选择库实现，开发者只能去自己实现。

一旦和目前项目的需求有了冲突（修改或者添加），开发者考虑的不应该是【这个库实现不了，干脆换一个库吧】或者【不管这个库，我去再单独实现一个】，而是，**基于同一个图片选择框架，修改或者添加对应配置的接口**。

![screenshot_qq](https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_qq.png)

RxImagePicker是一个用于Android的响应式图片选择器，它将您的图片选择需求转换为一个接口进行配置，并在任何一个Activity或者Fragment中展示任何样式的图片选择UI。

### 支持的功能

- [x] Android Camera拍照
- [x] Android 图片选择
- [x] 以响应式数据流的格式返回数据（支持Observable/Flowable/Single/Maybe）
- [x] 支持拓展数据，比如用于实现微信 **发送原图** 等功能，完全由您自定义灵活进行配置

### UI的支持

除了Android系统默认的图片选择样式，你可以选择使用这些额外的UI支持：

- [x] 系统级别图片选择器
- [x] 知乎主题图片选择器
- [x] 微信主题图片选择器
- [x] 自定义UI图片选择器

<h3 id="sample_screenshots">示例截图</h2>

#### 系统图片选择和结果展示

<div align:left;display:inline;>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_sysytem.png"/>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_result.png"/>
</div>

#### 知乎主题

<div align:left;display:inline;>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_zhihu_dracula.png"/>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_zhihu_normal.png"/>
</div>


#### 微信主题
<div align:left;display:inline;>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_wechat.png"/>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_wechat_expand.png"/>
</div>

想要 **快速实现上图所示效果** 请点击[这里](https://github.com/qingmei2/RxImagePicker/wiki)查看wiki中的详细文档。

<h2 id="usage">基础使用</h2>

### 1. 添加依赖在Module的build.gradle文件中：

```groovy
// 最基础的架构，仅提供了系统默认的图片选择器和拍照功能
compile 'com.github.qingmei2:rximagepicker:2.1.1'

// 提供了自定义UI图片选择器的基本组件，自定义UI的需求需要添加该依赖
compile 'com.github.qingmei2:rximagepicker_support:2.1.1'

// 如果需要额外的UI支持，请选择依赖对应的UI拓展库
compile 'com.github.qingmei2:rximagepicker_support_zhihu:2.1.1'     // 知乎图片选择器
compile 'com.github.qingmei2:rximagepicker_support_wechat:2.1.1'    // 微信图片选择器
```

### 2. 接口配置

声明一个接口，并进行基础的配置：

```java
public interface MyImagePicker {

    @Gallery    //打开相册选择图片
    Observable<Result> openGallery();

    @Camera    //打开相机拍照
    Observable<Result> openCamera();
}
```

### 3. 实例化接口并使用它

在您的Activity或者Fragment中实例化该接口，以打开系统默认的相册/相机界面：

```java
//打开系统默认的图片选择器
private void onButtonClick() {
    new RxImagePicker.Builder()
            .with(this)
            .build()
            .create(MyImagePicker.class)
            .openGallery()
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

如果对于RxImagePicker的使用有疑问或者建议，**请提issue**，或者加QQ群**391638630**共同探讨.

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

## Contributor

* [13kmsteady](https://github.com/13kmsteady)

## Another author's libraries using RxJava:

* **[RxSchedulers](https://github.com/qingmei2/RxSchedulers)**: The schedulers tools for RxJava2 in Android.   
* **[RxDialog](https://github.com/qingmei2/RxDialog)**:  An easy-to-use Dialog management tool (Building...).

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
