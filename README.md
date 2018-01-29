# RxImagePicker

[中文文档](https://github.com/qingmei2/RxImagePicker/blob/master/README_ZH.md)

**The library choosing pictures from your camera or gallery and load it on your Android device screen.** 

It is based on [MLSDev's](https://github.com/MLSDev) library [RxImagePicker](https://github.com/MLSDev/RxImagePicker) for secondary development

## <h2 id="Usage">Usage</h2>

### 1. Add Code into your Moudle build.gradle:

```gradle
compile 'com.github.qingmei2:rximagepicker:0.1.0-alpha'
```
### 2. Customize your ImagePicker interface ，to configure your own custom methods and response data types for return values:

```java
public interface MyRxImagePicker {

    @Gallery       // this annotation is used to picture selection
    @AsFile        // the return type is File
    Observable<File> openGallery();

    @Camera        // this annotation is used to take photo
    @AsBitmap      // the return type is Bitmap
    Observable<Bitmap> openCamera();

}
```
Precautions:

* **SourceFrom** annotation :

**@Gallery**, **@ Camera** Methods in your interface **Must** and **At most** Add one of these annotations ，to tell the ImagePicker what to do.

* **ObserverAs** annotation:

**@AsBitmap**, **@AsFile**, **@AsUri** Methods in your interface **Up to** Add one of these annotations ，to tell the ImagePicker the type of data in the response data returned by.

If you do not use the **ObserverAs** annotation, Uri type of response data **(Observable <Uri>)** is returned by default.

### 3. Register your custom ImagePicker in your Activity or Fragment:

```java
private void onButtonClick() {
        new RxImagePicker.Builder()
                .with(MainActivity.this)       
                .build()
                .create(MyRxImagePicker.class)  //  register your custom imagePicker interface
                .openGallery()                  // use your own custom method 「take photo」or 「picture selection」
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        // do what you want to do
                    }
                });
}
```
### 4. You can also don't custom ImagePicker and register, this library will implicitly select RxDefaultImagePicker to register:

```java
private ovid openGallery(){
        new RxImagePicker.Builder()
                .with(MainActivity.this)
                .build()
                .create()   // implicitly select RxDefaultImagePicker 
                .openGallery()
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(Uri uri) throws Exception {
                        // do what you want to do 
                    }
                });

         //  The interfaces provided in the RxImagePicker library，don't need to customize
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
## Contributor

* [13kmsteady](https://github.com/13kmsteady)

## Coming soon:

* 1. more responsive data type support (Flowable, Single ...)

* 2. custom image selector UI support

* 3. the choice of video files

* n.  under development...

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
