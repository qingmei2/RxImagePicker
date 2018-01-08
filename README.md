# RxImagePicker

An easy way to get image from Gallery or Camera with request runtime permission on Android M using RxJava2

## Setup

To use this library your ` minSdkVersion` must be >= 16.

In your build.gradle :

```gradle
dependencies {
    compile 'com.mlsdev.rximagepicker:library:2.0.2'    
}
```

## Example

```java
RxImagePicker.with(context).requestImage(Sources.CAMERA).subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@NonNull Uri uri) throws Exception {
                        //Get image by uri using one of image loading libraries. I use Glide in sample app.
                    }
                });
```

Request multiple images on Android Api level 18+ :

```java
RxImagePicker.with(getContext()).requestMultipleImages().subscribe(new Consumer<List<Uri>>() {
                    @Override
                    public void accept(@NonNull List<Uri> uris) throws Exception {
                        //Get images by uris.
                    }
                });
```

### Using converters

```java
RxImagePicker.with(context).requestImage(Sources.GALLERY)
    .flatMap(new Function<Uri, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(@NonNull Uri uri) throws Exception {
                        return RxImageConverters.uriToBitmap(getContext(), uri);
                    }
                }).subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        // Do something with Bitmap
                    }
                });
```

```java
RxImagePicker.with(context).requestImage(Sources.GALLERY)
    .flatMap(new Function<Uri, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(@NonNull Uri uri) throws Exception {
                        return RxImageConverters.uriToFile(getContext(), uri, new File("YOUR FILE"));
                    }
                }).subscribe(new Consumer<File>() {
                    @Override
                    public void accept(@NonNull File file) throws Exception {
                        // Do something with your file copy
                    }
                });
```

## Sample App

<img src="https://cloud.githubusercontent.com/assets/1778155/11761109/cb70a420-a0bd-11e5-8cf1-e2b172745eab.png" width="400">

## Authors
* [Sergey Glebov](mailto:glebov@mlsdev.com) ([frederikos][github-frederikos]), MLSDev 

## License
RxImagePicker is released under the MIT license. See LICENSE for details.

## About MLSDev

[<img src="https://cloud.githubusercontent.com/assets/1778155/11761239/ccfddf60-a0c2-11e5-8f2a-8573029ab09d.png" alt="MLSDev.com">][mlsdev]

RxImagePicker is maintained by MLSDev, Inc. We specialize in providing all-in-one solution in mobile and web development. Our team follows Lean principles and works according to agile methodologies to deliver the best results reducing the budget for development and its timeline. 

Find out more [here][mlsdev] and don't hesitate to [contact us][contact]!

[mlsdev]: http://mlsdev.com
[contact]: http://mlsdev.com/contact_us
[github-frederikos]: https://github.com/frederikos

