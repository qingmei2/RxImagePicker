# RxImagePicker

#### English Documentation | [中文文档](https://github.com/qingmei2/RxImagePicker/blob/2.x/README_ZH.md)

<p/>

<a target="_blank" href="https://bintray.com/mq2553299/maven/rximagepicker"><img
        src="https://api.bintray.com/packages/mq2553299/maven/rximagepicker/images/download.svg?version=3.0.0-beta02"></a>(RxJava3)
<a target="_blank" href='https://bintray.com/mq2553299/maven/rximagepicker/2.5.4/link'><img
        src='https://api.bintray.com/packages/mq2553299/maven/rximagepicker/images/download.svg?version=2.5.4'></a>(RxJava2)
<a target="_blank"><img src="https://img.shields.io/badge/API-16+-green.svg"></a>
<a target="_blank" href="https://github.com/qingmei2/RxImagePicker/blob/2.x/LICENSE"><img src="https://img.shields.io/badge/License-MIT-blue.svg"></a>


Support for `RxJava2`. Flexible picture selector of Android, provides the support for theme of Zhihu and WeChat.

> `Zhihu`: Famous Online Q&A Community APP in China.  
> `WeChat`: Most Used Instant Messaging Social Networking App in China.

## Introduction

**Purpose** of RxImagePicker: Let developers realize the demand of selecting picture in the development of Android in a simple and flexible way.

RxImagePicker is a **reactive** picture selector for Android, which converts your selection requirements of picture into an interface for configuration and displays any UI theme in `Activity` or `Fragment`.

## Support

- [x] Android Camera Photograph  
- [x] Android photo album picture selection  
- [x] Returns data in the format of reactive data stream ( such as `Observable/Flowable/Single/Maybe` )  
- [x] [AndroidX](https://developer.android.com/jetpack/androidx/) support ( after v2.3.0 )

## Support of UI

- [x] System Picture Selector
- [x] [Optional] Theme Picture Selector of Zhihu
- [x] [Optional] Theme Picture Selector of WeChat ()
- [x] [Optional] Custom UI Picture Selector

## Screenshots

### Selection and result display of system picture:

<div align:left;display:inline;>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_sysytem.png"/>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_result.png"/>
</div>

### Theme of Zhihu

<div align:left;display:inline;>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_zhihu_dracula.png"/>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_zhihu_normal.png"/>
</div>

### Theme of WeChat

<div align:left;display:inline;>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_wechat.png"/>
<img width="200" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/screenshot_wechat_expand.png"/>
</div>

### UI Test

<div align:left;display:inline;>
<img width="235" height="360" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/rximagepicker_test.gif"/>
</div>

<div align:left;display:inline;>
<img width="608" height="175" src="https://github.com/qingmei2/RxImagePicker/blob/2.x/screenshot/test_result.png"/>
</div>

## Basic Usage

The following code will show you how to use photo album or camera at the Android system-level :

### 1.Add the following depending on the file of build.gradle:

```groovy
// The most basic architecture, only provides the default
// picture selector and photographing function of system.

implementation 'com.github.qingmei2:rximagepicker:${last_version}'

// Provide the basic components of custom UI picture selector
// and this dependency needs to be added to the requirements of the custom UI
implementation 'com.github.qingmei2:rximagepicker_support:${last_version}'


// If you need additional UI support, choose to rely on the corresponding UI extension library

// Zhihu picture selector
implementation 'com.github.qingmei2:rximagepicker_support_zhihu:${last_version}'

// WeChat picture selector
implementation 'com.github.qingmei2:rximagepicker_support_wechat:${last_version}'
```

> **If your project not migrate to `androidx`, please use version `2.2.0`**.

### 2.Interface Configuration

Declare an interface and carry out the following configuration:

```java
public interface MyImagePicker {

    @Gallery    // open gallery
    Observable<Result> openGallery(Context context);

    @Camera     // take photos
    Observable<Result> openCamera(Context context);
}
```

### 3.Instantiate and use it

Instantiate the interface in your `Activity` or `Fragment` to open the default album and camera screen of system:

```Java
RxImagePicker
        .create(MyImagePicker.class)
        .openGallery(this)
        .subscribe(new Consumer<Result>() {
            @Override
            public void accept(Result result) throws Exception {
                // do something, ex:
                Uri uri = result.getUri();
                GlideApp.with(this)
                         .load(uri)
                         .into(ivPickedImage);
            }
        });
```

## Support UI theme Usage

### 1.Add the following depending on the file of build.gradle:

```groovy
// Zhihu picture selector
implementation 'com.github.qingmei2:rximagepicker_support_zhihu:${last_version}'
```

### 2.Interface Configuration

Declare an interface and carry out the following configuration:

```kotlin
interface ZhihuImagePicker {

    // normal style
    @Gallery(componentClazz = ZhihuImagePickerActivity::class,
            openAsFragment = false)
    fun openGalleryAsNormal(context: Context,
                            config: ICustomPickerConfiguration): Observable<Result>

    // dracula style                        
    @Gallery(componentClazz = ZhihuImagePickerActivity::class,
            openAsFragment = false)
    fun openGalleryAsDracula(context: Context,
                             config: ICustomPickerConfiguration): Observable<Result>

    // take photo                         
    @Camera
    fun openCamera(context: Context): Observable<Result>
}
```

### 3.Instantiate and use it

```Kotlin
val rxImagePicker: ZhihuImagePicker = RxImagePicker
                .create(ZhihuImagePicker::class.java)

rxImagePicker.openGalleryAsNormal(this,
                ZhihuConfigurationBuilder(MimeType.ofImage(), false)
                        .maxSelectable(9)
                        .countable(true)
                        .spanCount(4)
                        .theme(R.style.Zhihu_Normal)
                        .build())
               .subscribe {
                    Glide.with(this@ZhihuActivity)
                            .load(it.uri)
                            .into(imageView)
                }
```

see [sample](https://github.com/qingmei2/RxImagePicker/blob/2.x/sample/src/main/java/com/qingmei2/sample/zhihu/ZhihuActivity.kt) for more informations.

## Advanced Usage

### 1. Action Annotation

RxImagePicker provides two action annovation, respectively `@Gallery` and `@Camera`.

`@Camera` will declare the way annovated by the annovation  Open the camera to take a photo.

Please note, each method of the interface must add an action annotation to declare the corresponding action. If the method is not configured with `@Gallery` or `@Camera`, RxImagePicker will throw an `Exception` at runtime.

#### @Gallery

`@Gallery` will open the photo album to choose picture.

It has three parameters:

* `componentClazz：KClass<*>` The class object of UI component, opens `SystemGalleryPickerView::class`—— the system Gallery by default.

* `openAsFragment：Boolean` Whether UI component is displayed as `Fragment` in some parts of `Activity` is true by default.

* `containerViewId: Int`  If UI needs to be displayed as `Fragment` in ViewGroup, the id corresponding to the ViewGroup needs to be assigned to it.

At present UI component only supports two kinds: `FragmentActivity` or `Fragment`(support-v4).

**Taking turning on the system camera for example**, its principle is to instantiate an invisible `Fragment` in the current `Activity`, and `Fragment` opens the system photo album through `Intent` and processes the returned data through the method of `onActivityResult()`. The code is as the following:

```kotlin
interface SystemImagePicker {

    @Gallery   // by default,componentClazz：KClass = SystemGalleryPickerView::class，openAsFragment：Boolean=true
    fun openGallery(context: Context): Observable<Result>
}
```

When need to **custom UI**, taking Zhihu theme as an example, we will configure `ZhihuImagePickerActivity::class` to `componentClazz` and set the value of `openAsFragment` to `false`:

```kotlin
interface ZhihuImagePicker {

    @Gallery(componentClazz = ZhihuImagePickerActivity::class,
            openAsFragment = false)
    fun openGalleryAsNormal(context: Context,
                            config: ICustomPickerConfiguration): Observable<Result>

    @Gallery(componentClazz = ZhihuImagePickerActivity::class,
            openAsFragment = false)
    fun openGalleryAsDracula(context: Context,
                             config: ICustomPickerConfiguration): Observable<Result>

    @Camera
    fun openCamera(context: Context): Observable<Result>
}
```

For more information, please refer to the [ZhihuActivity](https://github.com/qingmei2/RxImagePicker/blob/2.x/sample/src/main/java/com/qingmei2/sample/zhihu/ZhihuActivity.kt) in sample.

At the same time, when UI needs to be displayed as `Fragment`, `RxImagePicker` needs to be informed of the `id` of the `ViewGroup` control so that `RxImagePicker` can be correctly displayed in the corresponding `ViewGroup`.

#### @Camera

`@Camera` will declare the way annotated by the annotation open the camera to take photo.

Please note, `@Camera` only provides the function of calling the system camera to take a photo at present. Although this annotation provides the same API as `@Gallery`, there is no sense to configure them at present.

### 2.ICustomPickerView

`ICustomPickerView` is an underneath interface, it is used for:

* 1. Show the picture selector interface
* 2. Obtain selection results of users

```kotlin
interface ICustomPickerView {

    fun display(fragmentActivity: FragmentActivity,
                @IdRes viewContainer: Int,
                configuration: ICustomPickerConfiguration?)

    fun pickImage(): Observable<Result>
}
```

It has several classic implementation classes, for example, [ImagePickerDisplayer](https://github.com/qingmei2/RxImagePicker/blob/2.x/rximagepicker/src/main/java/com/qingmei2/rximagepicker/core/ImagePickerDisplayer.kt) when open the corresponding activity through configuration;

### 3.Context: necessary parameters.

Each interface method of RxImagePicker must be configured with a `Context` as parameters. If the method does not have any parameters, it will throw out `NullPointerException`:

> ${method.name} requires just one instance of type: Context, but none.

This is understandable. Starting the UI component of a picture selector must depend on the instance of `Context`.

Note, This `Context` must be a `FragmentActivity`, not `Application` or others, or else throw out the abnormity of `IllegalArgumentException`!

### 4.ICustomPickerConfiguration: optional parameters

`ICustomPickerConfiguration` interface, similar to a tag. RxImagePicker will treat it as the configuration class.

As for opening the system photo album or system camera, it makes no sense to configure, but **it must be configured for custom UI** ( such as `WeChat` theme and `Zhihu` theme ).

The basic components of RxImagePicker do not provide the implementation class, please refer to [SelectionSpec](https://github.com/qingmei2/RxImagePicker/blob/2.x/rximagepicker_support/src/main/java/com/qingmei2/rximagepicker_extension/entity/SelectionSpec.kt) if any questions.

### 2.5 Complete full custom UI

Though RxImagePicker provides the UI style of `WeChat` picture selector and `Zhihu` picture selector, two sets of models are still not enough to cover more and more sophisticated UI demands of APP.

RxImagePicker provides enough degree of freedom interface to provide private customized UI for developers. Whether a new `Activity` is created or displayed in a `ViewGroup` container at present, it is enough.

The above WeChat theme and Zhihu theme are based on its to implement. Please refer to the source code for detailed implementation.

## Another author's libraries using RxJava:

* [RxWeaver: A lightweight and flexible error handler tools of RxJava2.](https://github.com/qingmei2/RxWeaver)

* [MVVM-Rhine: The MVVM using RxJava and Android databinding.](https://github.com/qingmei2/MVVM-Rhine)

## License
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
