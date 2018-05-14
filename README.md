# RxImagePicker

![Download](https://api.bintray.com/packages/mq2553299/maven/rximagepicker/images/download.svg)

### RxImagePicker的设计起源，请参考我的这篇文章：

[RxImagePicker:从零实现灵活且可高度定制的Android图片选择架构](https://www.jianshu.com/p/fecf3a13e615)

## 目录

#### [简介](#overview)  
#### [示例截图](#sample_screenshots)  
#### [基本使用](#usage)  
#### [微信/知乎UI的配置步骤](#wechat_config)  

<h2 id="overview">简介</h2>

RxImagePicker存在的目的是：让开发者能够**简单**并且**灵活**的方式实现Android开发中**图片选择**的需求。

它与传统的其他三方的图片选择库不同的是，后者通过跳转到固定的Activity进行图片选择，并不能满足所有应用的需求；以QQ聊天会话界面的图片选择功能为例，这种特殊的UI需求很难依靠图片选择库实现，开发者只能去自己实现。

一旦和目前项目的需求有了冲突（修改或者添加），开发者考虑的不应该是【这个库实现不了，干脆换一个库吧】或者【不管这个库，我去再单独实现一个】，而是，**基于同一个图片选择框架，修改或者添加对应配置的接口**。

![screenshot_qq](https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_qq.png)

事实上，其他传统的三方图片选择库并没有解决根本的问题：以更加灵活简单的方式实现**图片选择的功能**和**自定义UI**。

RxImagePicker是一个用于Android的响应式图片选择器，它将您的图片选择需求转换为一个接口进行配置，并在任何一个Activity或者Fragment中展示任何样式的图片选择UI。

### 支持的功能

- [x] Android Camera拍照
- [x] Android 图片选择
- [x] 以响应式数据流的格式返回数据（支持Observable/Flowable/Single/Maybe）
- [x] 动态配置响应式数据流的数据类型（File,Bitmap,或是Uri）

### UI的支持

除了Android系统默认的图片选择样式，你可以选择使用这些额外的UI支持：

- [x] 系统级别图片选择器
- [x] 知乎主题图片选择器
- [x] 微信主题图片选择器
- [x] 自定义UI图片选择器

<h3 id="sample_screenshots">示例截图</h2>

#### 系统图片选择

![screenshot_sysytem](https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_sysytem.png)

#### 知乎主题
![screenshot_zhihu_dracula](https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_zhihu_dracula.png)
![screenshot_zhihu_normal](https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_zhihu_normal.png)

#### 微信主题
<div align="left"><img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_wechat.png"/></div>
<div><img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_wechat_expand.png"/></div>

#### 结果展示
![screenshot_result](https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/screenshot_result.png)

<h2 id="usage">基本使用</h2>

### 1. 添加依赖在Module的build.gradle文件中：

```groovy
// 最基础的架构，仅提供了系统默认的图片选择器和拍照功能
compile 'com.github.qingmei2:rximagepicker:0.2.2'

// 提供了自定义UI图片选择器的基本组件，自定义UI的需求需要添加该依赖
compile 'com.github.qingmei2:rximagepicker_support:0.2.2'

//如果需要额外的UI支持，请选择依赖对应的UI拓展库
compile 'com.github.qingmei2:rximagepicker_support_zhihu:0.2.2'     // 知乎图片选择器
compile 'com.github.qingmei2:rximagepicker_support_wechat:0.2.2'    // 微信图片选择器
```
### 2. 接口配置

声明一个接口，并进行基础的配置：

```java
public interface MyImagePicker {

    @Gallery    //打开相册选择图片
    @AsFile     //返回值为File类型
    Observable<File> openGallery();

    @Camera    //打开相机拍照
    @AsBitmap  //返回值为Bitmap类型
    Observable<Bitmap> openCamera();

}
```

#### @Gallery/@Camera 声明对应的行为：

**@Camera** 打开相机拍照并返回图片  
**@Gallery** 打开相册选择并返回图片  

请注意，接口的每个方法都需要添加该类型的注解以声明对应的行为，若方法未配置@Gallery或@Camera，RxImagePicker会在运行时无法进行解析，并抛出对应的异常。

#### @AsFile/@AsBitmap/@AsUri 声明数据返回的格式：

**@AsFile** 返回File类型的数据  
**@AsBitmap**  返回Bitmap类型的数据  
**@AsUri**  返回Uri类型的数据  

请注意，若方法未配置该类型的注解以声明数据应当返回的格式，则默认会以Uri的格式进行数据的返回。

### 3. 实例化接口并使用它

在您的Activity或者Fragment中实例化该接口，以打开系统默认的相册/相机界面：

```java
//打开系统默认的图片选择器
private void onButtonClick() {
    new RxImagePicker.Builder()
            .with(this)
            .build()
            .create(MyImagePicker.class)    //  register your custom imagePicker interface
            .openGallery()                  // use your own custom method 「take photo」or 「picture selection」
            .subscribe(new Consumer<File>() {
                @Override
                public void accept(File file) throws Exception {
                    // do what you want to do
                }
            });
}
```
上述步骤中，您也可以通过调用create()的无参方法，RxImagePicker会使用默认的DefaultImagePicker接口

```
public interface DefaultImagePicker {

    @Gallery
    @AsUri
    Observable<Uri> openGallery();

    @Camera
    @AsUri
    Observable<Uri> openCamera();
}
```

<h2 id="wechat_config">微信 / 知乎UI的配置步骤</h2>

RxImagePicker提供了 **知乎主题** 和 **微信主题** 的UI支持，其中知乎主题还包括日间和夜间的两种UI模式,详见[上文](#sample_screenshots)。

下面提供了实现 **微信主题UI** 的最基本方式，保证开发者能够在3分钟内成功实现 **仿微信图片选择器** 的UI效果：

#### 1、添加依赖

```groovy
implementation 'com.github.qingmei2:rximagepicker_support_wechat:0.2.2' //微信主题UI的支持
```

#### 2、新建并配置接口

```Java
public interface WechatImagePicker {

    String KEY_WECHAT_PICKER_ACTIVITY = "key_wechat_picker";

    @AsBitmap
    @Gallery(viewKey = KEY_WECHAT_PICKER_ACTIVITY)
    Observable<Bitmap> openGallery();

    @AsFile
    @Camera
    Single<File> openCamera();
}
```

#### 3、在Activity中配置并打开图片选择器：

请注意，在调用下述代码打开图片选择器之前，请确认您是否在manifest文件中已经配置好外部存储卡的权限。

```Java
new RxImagePicker.Builder()
      .with(this)
      .addCustomGallery(
              WechatImagePicker.KEY_WECHAT_PICKER_ACTIVITY, //key
              WechatImagePickerActivity.class,              //key对应的ActivityClass
              new WechatConfigrationBuilder(MimeType.ofAll(), false)
                      .maxSelectable(9)               //最大可选数量
                      .spanCount(4)                   //GridLayout每行的Item数量
                      .countable(false)               //不可计数的
                      .theme(R.style.Wechat)          //微信主题
                      .imageEngine(new GlideEngine()) //图片加载引擎
                      .build()
      )
      .build()
      .create(WechatImagePicker.class)
      .openGallery()  //打开图片选择器，或者调用openCamera()打开相机拍照
      .subscribe(new Consumer<Bitmap>() {
          @Override
          public void accept(Bitmap bitmap) throws Exception {
              // do what you want to do
          }
      });
```

通过以上三步，开发者就能实现 **微信图片选择** 的功能。

更详细的配置或者 **知乎主题UI** 使用方式，请参考sample，里面提供了文档中所有代码。

## Contributor

* [13kmsteady](https://github.com/13kmsteady)

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
