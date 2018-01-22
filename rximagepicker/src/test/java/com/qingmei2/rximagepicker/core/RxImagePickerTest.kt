package com.qingmei2.rximagepicker.core

import android.app.Fragment
import android.app.FragmentManager
import android.support.v4.app.SupportActivity
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RxImagePickerTest {

    private val mockFragment: Fragment = mock()
    private val mockActivity: SupportActivity = mock()
    private val mockFragmentManager: FragmentManager = mock()

    @Before
    fun setUp() {
        doReturn(mockActivity).whenever(mockFragment).activity
        doReturn(mockFragmentManager).whenever(mockFragment).fragmentManager
        doReturn(mockFragmentManager).whenever(mockActivity).fragmentManager
    }

    @Test
    @Throws(Exception::class)
    fun builderWithActivitySuccess() {
        val rxImagePicker = RxImagePicker.Builder()
                .with(mockActivity)
                .build()
        verify(mockActivity, times(1)).fragmentManager

        assertTrue(rxImagePicker != null)
    }

    @Test
    @Throws(Exception::class)
    fun builderWithFragmentSuccess() {
        val rxImagePicker = RxImagePicker.Builder()
                .with(mockFragment)
                .build()
        verify(mockFragment, times(1)).fragmentManager
        verify(mockFragment, times(1)).activity

        assertTrue(rxImagePicker != null)
    }

    @Test
    fun builderNoWithFailed() {
        Single.just(RxImagePicker.Builder())
                .map { it.build() }
                .test()
                .assertNotComplete()
                .assertNoValues()
                .assertError(NullPointerException::class.java)
    }
}