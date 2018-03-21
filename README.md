# RxImagePicker

## 简介

RxImagePicker存在的目的是：让开发者能够**简单**并且**灵活**的方式实现Android开发中**图片选择**的需求。

它与传统的其他三方的图片选择库不同的是，后者通过跳转到固定的Activity进行图片选择，并不能满足所有应用的需求；以QQ聊天会话界面的图片选择功能为例，这种特殊的UI需求很难依靠目前市面上主流的图片选择框架实现，开发者只能花费更多的时间成本去自己实现。

![screenshot_qq](https://github.com/qingmei2/RxImagePicker/blob/dev_qingmei2/screenshot/screenshot_qq.png){:height="50%" width="50%"}

事实上，其他传统的三方图片选择库并没有解决根本的问题：以更加灵活简单的方式实现**图片选择的功能**和**自定义UI**。

RxImagePicker是一个用于Android的响应式图片选择器，它将您的图片选择需求转换为一个接口进行配置，并在任何一个Activity或者Fragment中展示任何样式的图片选择UI。

### 支持的功能

- [x] Android Camera拍照
- [x] Android 图片选择
- [x] 以响应式数据流的格式返回数据（支持Observable/Flowable/Single/Maybe）
- [x] 动态配置响应式数据流的数据类型（File,Bitmap,或是Uri）

### UI的支持

除了Android系统默认的图片选择样式，你可以选择使用这些额外的UI支持：

- [x] 知乎主题图片选择器
- [x] 微信主题图片选择器
- [x] 自定义UI图片选择器

![screenshot_sysytem](https://github.com/qingmei2/RxImagePicker/blob/dev_qingmei2/screenshot/screenshot_sysytem.png){:height="50%" width="50%"}
![screenshot_zhihu_dracula](https://github.com/qingmei2/RxImagePicker/blob/dev_qingmei2/screenshot/screenshot_zhihu_dracula.png){:height="50%" width="50%"}
![screenshot_zhihu_normal](https://github.com/qingmei2/RxImagePicker/blob/dev_qingmei2/screenshot/screenshot_zhihu_normal.png){:height="50%" width="50%"}
![screenshot_result](https://github.com/qingmei2/RxImagePicker/blob/dev_qingmei2/screenshot/screenshot_result.png){:height="50%" width="50%"}

## <h2 id="Usage">基本使用</h2>

### 1. 添加依赖在Module的build.gradle文件中：

```groovy

//提供了系统默认的图片选择器和拍照功能
compile 'com.github.qingmei2:rximagepicker:0.1.0-beta4'

//提供了自定义UI图片选择器的基本组件，自定义UI的需求需要添加该依赖
compile 'com.github.qingmei2:rximagepicker_extension:0.1.0-beta4'

//如果需要额外的UI支持，请选择依赖对应的UI拓展库
compile 'com.github.qingmei2:rximagepicker_extension_zhihu:0.1.0-beta4'     //知乎图片选择器
compile 'com.github.qingmei2:rximagepicker_extension_wechat:0.1.0-beta4'    //微信图片选择器

```
### 2. 接口配置

声明一个接口，并进行基础的配置：

```java
public interface MyImagePicker {

    @Gallery
    @AsFile
    Observable<File> openGallery();

    @Camera
    @AsBitmap
    Observable<Bitmap> openCamera();

}
```

#### @Gallery/@Camera注解声明了对应的行为：

@Camera 打开相机拍照并返回图片  
@Gallery 打开相册选择并返回图片  

请注意，接口的每个方法都需要添加该类型的注解以声明对应的行为，若方法未配置@Gallery或@Camera，RxImagePicker会在运行时无法进行解析，并抛出对应的异常。

#### @AsFile/@AsBitmap/@AsUri注解声明了数据返回的格式

@AsFile 返回File类型的数据  
@AsBitmap  返回Bitmap类型的数据  
@AsUri  返回Uri类型的数据  

请注意，若方法未配置该类型的注解以声明数据应当返回的格式，则默认会以Uri的格式进行数据的返回。

### 3. 实例化接口并使用它

在您的Activity或者Fragment中实例化该接口：

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
