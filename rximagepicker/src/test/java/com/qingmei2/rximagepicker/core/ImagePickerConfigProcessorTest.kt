package com.qingmei2.rximagepicker.core

class ImagePickerConfigProcessorTest {

//    @Rule
//    @JvmField
//    val rxRule = TestSchedulerRule()
//
//    private val mockContext: Context = mock()
//    private val mockCameraPickerViews: Map<String, ICameraCustomPickerView> = mock()
//    private val mockGalleryPickerViews: Map<String, IGalleryCustomPickerView> = mock()
//    private val schedulers = RxImagePickerTestSchedulers()
//
//    private val mockUri: Uri = mock()
//    private val mockUri2: Uri = mock()
//    private val mockBitmap: Bitmap = mock()
//
//    lateinit var processor: ConfigProcessor
//
//    @Before
//    fun setUp() {
//        processor = ConfigProcessor(mockContext,
//                mockCameraPickerViews,
//                mockGalleryPickerViews,
//                schedulers)
//    }
//
//    @Test
//    fun sourceFromTestCamera() {
////        whenever(mockCameraPickerViews.pickImage()).thenReturn(Observable.just(mockUri))
////
////        val observable = processor.sourceFrom(processor.cameraViews, processor.galleryViews)
////                .apply(instanceProvider(SourcesFrom.CAMERA)) as Observable<Uri>
////
////        verify(processor.cameraViews, times(1)).takePhoto()
////        verify(processor.galleryPickerView, never()).pickImage()
////        observable.test()
////                .assertComplete()
////                .assertNoErrors()
////                .assertValueCount(1)
////                .assertValueAt(0, mockUri)
//    }
//
//    @Test
//    fun sourceFromTestGallery() {
////        whenever(mockGalleryPickerViews.pickImage()).thenReturn(Observable.just(mockUri))
////
////        val observable = processor.sourceFrom(processor.cameraViews, processor.galleryViews)
////                .apply(instanceProvider(SourcesFrom.GALLERY)) as Observable<Uri>
////
////        verify(processor.cameraPickerView, never()).takePhoto()
////        verify(processor.galleryPickerView, times(1)).pickImage()
////        observable.test()
////                .assertComplete()
////                .assertNoErrors()
////                .assertValueCount(1)
////                .assertValueAt(0, mockUri)
//    }
//
//    @Test
//    fun observerAsTest() {
//        val provider = instanceProvider()
//        val function = processor.observerAs(provider, processor.context) as ObserverAsConverter
//
//        assertEquals(function.context, processor.context)
//        assertEquals(function.observerAs, provider.observerAs)
//    }
//
//    @Test
//    fun testProcessCameraAndUri() {
//        val spy = processor.let { spy(it) }
//        val configProvider = instanceProvider()
//
//        doReturn(testSourceFrom())
//                .whenever(spy).sourceFrom(mockCameraPickerViews, mockGalleryPickerViews)
//        doReturn(testObserverAsUri())
//                .whenever(spy).observerAs(configProvider, mockContext)
//
//        val value: Observable<Uri> = spy.process(configProvider) as Observable<Uri>
//
//        TestObserver<Uri>().apply {
//            value.subscribe(this)
//            rxRule.triggerActions()
//
//            assertComplete()
//                    .assertNoErrors()
//                    .assertValueCount(1)
//                    .assertValueAt(0, mockUri2)
//        }
//    }
//
//    @Test
//    fun testProcessGalleryAndBitmap() {
//        val spy = processor.let { spy(it) }
//        val configProvider = instanceProvider()
//
//        doReturn(testSourceFrom())
//                .whenever(spy).sourceFrom(mockCameraPickerViews, mockGalleryPickerViews)
//        doReturn(testObserverAsbBitmap())
//                .whenever(spy).observerAs(configProvider, mockContext)
//
//        val value: Observable<Bitmap> = spy.process(configProvider) as Observable<Bitmap>
//
//        TestObserver<Bitmap>().apply {
//            value.subscribe(this)
//            rxRule.triggerActions()
//
//            assertComplete()
//                    .assertNoErrors()
//                    .assertValueCount(1)
//                    .assertValueAt(0, mockBitmap)
//        }
//    }
//
//    private fun instanceProvider(sourcesFrom: SourcesFrom = SourcesFrom.CAMERA,
//                                 observerAs: ObserverAs = ObserverAs.URI)
//            : ConfigProvider {
//        return ConfigProvider(false, "", sourcesFrom, observerAs, mock(), 0, mock())
//    }
//
//    private fun testSourceFrom(): Function<ConfigProvider, ObservableSource<Uri>> {
//        return Function { provider -> Observable.just(mockUri) }
//    }
//
//    private fun testObserverAsUri(): Function<Uri, ObservableSource<Any>> {
//        return Function { uri -> Observable.just(mockUri2) }
//    }
//
//    private fun testObserverAsbBitmap(): Function<Uri, ObservableSource<Any>> {
//        return Function { uri -> Observable.just(mockBitmap) }
//    }
}