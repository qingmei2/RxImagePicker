package com.qingmei2.rximagepicker.delegate

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.whenever
import com.qingmei2.rximagepicker.entity.observeras.AsBitmap
import com.qingmei2.rximagepicker.entity.observeras.AsFile
import com.qingmei2.rximagepicker.entity.observeras.AsUri
import com.qingmei2.rximagepicker.entity.observeras.ObserverAs
import com.qingmei2.rximagepicker.entity.sources.Camera
import com.qingmei2.rximagepicker.entity.sources.Gallery
import com.qingmei2.rximagepicker.entity.sources.SourcesFrom
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Method

class ProxyTranslatorTest {

    lateinit var translator: ProxyTranslator

    @Before
    fun setUp() {
        translator = ProxyTranslator()
                .let { spy(it) }
    }

    @Test
    fun processMethodTest() {
        val method: Method = mock()
        val objs: Array<Int> = Array(5, { i -> i * i })

        val sourcesFrom: SourcesFrom = mock()
        val observerAs: ObserverAs = mock()

        doReturn(sourcesFrom).whenever(translator).getStreamSourcesFrom(method)
        doReturn(observerAs).whenever(translator).getStreamObserverAs(method)

        val configProvider = translator.processMethod(method, objs)

        assertEquals(configProvider.sourcesFrom, sourcesFrom)
        assertEquals(configProvider.observerAs, observerAs)
    }

    @Test
    fun getStreamSourcesFromTestGallerySuccess() {
        val method: Method = mock()
        val gallery: Gallery = mock()

        whenever(method.getAnnotation(Gallery::class.java)).thenReturn(gallery)
        whenever(method.getAnnotation(Camera::class.java)).thenReturn(null)

        val sourcesFrom = translator.getStreamSourcesFrom(method)

        assertEquals(sourcesFrom, SourcesFrom.GALLERY)
    }

    @Test
    fun getStreamSourcesFromTestCameraSuccess() {
        val method: Method = mock()
        val camera: Camera = mock()

        whenever(method.getAnnotation(Camera::class.java)).thenReturn(camera)
        whenever(method.getAnnotation(Gallery::class.java)).thenReturn(null)

        val sourcesFrom = translator.getStreamSourcesFrom(method)

        assertEquals(sourcesFrom, SourcesFrom.CAMERA)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getStreamSourcesFromTestFailedNoAnnotations() {
        val method: Method = mock()

        whenever(method.getAnnotation(Camera::class.java)).thenReturn(null)
        whenever(method.getAnnotation(Gallery::class.java)).thenReturn(null)

        translator.getStreamSourcesFrom(method)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getStreamSourcesFromTestFailedRepeat() {
        val method: Method = mock()
        val camera: Camera = mock()
        val gallery: Gallery = mock()

        whenever(method.getAnnotation(Camera::class.java)).thenReturn(camera)
        whenever(method.getAnnotation(Gallery::class.java)).thenReturn(gallery)

        translator.getStreamSourcesFrom(method)
    }

    @Test
    fun getStreamObserverAsTestAsBitmapSuccess() {
        val method: Method = mock()
        val bitmap: AsBitmap = mock()

        whenever(method.getAnnotation(AsBitmap::class.java)).thenReturn(bitmap)
        whenever(method.getAnnotation(AsFile::class.java)).thenReturn(null)
        whenever(method.getAnnotation(AsUri::class.java)).thenReturn(null)

        val observerAs = translator.getStreamObserverAs(method)

        assertEquals(observerAs, ObserverAs.BITMAP)
    }

    @Test
    fun getStreamObserverAsTestAsFilepSuccess() {
        val method: Method = mock()
        val file: AsFile = mock()

        whenever(method.getAnnotation(AsBitmap::class.java)).thenReturn(null)
        whenever(method.getAnnotation(AsFile::class.java)).thenReturn(file)
        whenever(method.getAnnotation(AsUri::class.java)).thenReturn(null)

        val observerAs = translator.getStreamObserverAs(method)

        assertEquals(observerAs, ObserverAs.FILE)
    }

    @Test
    fun getStreamObserverAsTestAsUriSuccess() {
        val method: Method = mock()
        val uri: AsUri = mock()

        whenever(method.getAnnotation(AsBitmap::class.java)).thenReturn(null)
        whenever(method.getAnnotation(AsFile::class.java)).thenReturn(null)
        whenever(method.getAnnotation(AsUri::class.java)).thenReturn(uri)

        val observerAs = translator.getStreamObserverAs(method)

        assertEquals(observerAs, ObserverAs.URI)
    }

    @Test
    fun getStreamObserverAsTestAsBitmapSuccessDefaultUri() {
        val method: Method = mock()

        whenever(method.getAnnotation(AsBitmap::class.java)).thenReturn(null)
        whenever(method.getAnnotation(AsFile::class.java)).thenReturn(null)
        whenever(method.getAnnotation(AsUri::class.java)).thenReturn(null)

        val observerAs = translator.getStreamObserverAs(method)

        assertEquals(observerAs, ObserverAs.URI)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getStreamObserverAsTestAsBitmapFailedRepeat() {
        val method: Method = mock()
        val bitmap: AsBitmap = mock()
        val uri: AsUri = mock()

        whenever(method.getAnnotation(AsBitmap::class.java)).thenReturn(bitmap)
        whenever(method.getAnnotation(AsFile::class.java)).thenReturn(null)
        whenever(method.getAnnotation(AsUri::class.java)).thenReturn(uri)

        translator.getStreamObserverAs(method)
    }
}