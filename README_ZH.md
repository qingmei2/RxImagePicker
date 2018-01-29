# RxImagePicker中文文档

**该库支持Android设备拍照或相册选取照片，以 [响应式数据(Observable<?>)](https://github.com/ReactiveX/RxJava) 流的形式将数据返回并进行处理。**

该库为基于 [MLSDev's](https://github.com/MLSDev) 的 [RxImagePicker](https://github.com/MLSDev/RxImagePicker) 库进行的二次开发。

## <h2 id="Usage">使用步骤</h2>

### 1.  在您 App Moudle 下的 build.gradle 添加依赖:

```gradle
compile 'com.github.qingmei2:rximagepicker:0.1.0-alpha'
```

### 2. 自定义您的 ImagePicker 接口，用以配置您自定义的方法和返回值的响应式数据类型:

```java
public interface MyRxImagePicker {

    @Gallery       // 该注解用于进行图片选择
    @AsFile        // 返回值类型为 File
    Observable<File> openGallery();

    @Camera        // 该注解用于进行相机拍照
    @AsBitmap      // 返回值类型为 Bitmap
    Observable<Bitmap> openCamera();

}
```
需要注意的：

* **SourceFrom** 注解:

    **@Gallery**、**@Camera** 您接口中的方法 **必须** 且 **至多** 添加其中的一个注解，用于告诉 ImagePicker 所需进行的行为；

* **ObserverAs** 注解:

    **@AsBitmap**、 **@AsFile**、**@AsUri** 您接口中的方法 **至多** 添加其中的一个注解，用于告诉ImagePicker所返回的响应式数据中的数据类型；

如果没有使用 **ObserverAs** 注解，默认返回 Uri 类型的响应式数据 **(Observable<Uri>)**。

### 3. 在您的 Activity 或者 Fragment 中注册您自定义的 ImagePicker:

```java
private void onButtonClick() {
        new RxImagePicker.Builder()
                .with(MainActivity.this)       
                .build()
                .create(MyRxImagePicker.class)  // 注册您的自定义 ImagePicker 接口
                .openGallery()                  // 使用您自定义的方法「拍照」或者「选择图片」
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        // 做您想做的
                    }
                });
}
```
### 4. 您也可以选择不进行自定义 ImagePicker 并进行注册，这样库会隐式地选择 RxDefaultImagePicker 进行注册：

```java
private void openGallery() {
        new RxImagePicker.Builder()
                .with(MainActivity.this)
                .build()
                .create()   // 隐式地选择 RxDefaultImagePicker.class
                .openGallery()
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) throws Exception {
                        // 做您想做的
                    }
                });

        // RxImagePicker 库中提供的接口，无需自己定义
        public interface RxDefaultImagePicker {
        
            @Gallery
            @AsUri
            Observable<Uri> openGallery();
        
            @Camera
            @AsUri
            Observable<Uri> openCamera();
        }
}
```
## 屏幕截图

![sample_02](https://github.com/qingmei2/RxImagePicker/blob/master/screenshot/sample_screenshot.png)

## 贡献者

* [13kmsteady](https://github.com/13kmsteady)

## 即将支持：

* 1、更多响应式数据类型的支持（Flowable、Single...）
* 2、自定义图片选择器 UI 的支持
* 3、视频文件的选择
* n 开发中...

License
-------

The RxImagePicker2 License：
​    
    MIT License

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