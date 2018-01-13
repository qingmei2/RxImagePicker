# RxImagePicker2

**The library choosing pictures from your camera or gallery and load it on your Android device screen.** 

It is based on [MLSDev's](https://github.com/MLSDev) library [RxImagePicker](https://github.com/MLSDev/RxImagePicker) for secondary development

## <h2 id="Usage">Usage</h2>
### ~~1. Add code into your Project Build.gradle:~~
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
### 2.~~Add code into your Module Build.gradle:~~

```
dependencies {
     
     compile 'com.github.qingmei2:RxImagePicker2:x.x.x'
}
```

This Library currently in development mode, please refer to the source code.

### 3.Add the Interface to config your RxImagePicker:

```Java
public interface IRxImagePicker {

    @Gallery
    Observable<Uri> openGallery();

    @Camera
    Observable<Uri> openCamera();
}
```

### 4.Initialize your RxImagePicker and use it:

```Java
public void openCamera(){
    new RxImagePicker2.Builder()
                    .with(MainActivity.this)
                    .build()
                    .create(IRxImagePicker.class)
                    .openGallery()
                    .subscribe(uri -> {
                        //do what you want
                     });
}
```

Lisence
-------
        The RxImagePicker2 License：
        
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
