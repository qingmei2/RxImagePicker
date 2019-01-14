package com.qingmei2.rximagepicker.core

class RxImagePickerTest {

//    private val mockFragment: Fragment = mock()
//    private val mockActivity: FragmentActivity = mock()
//    private val mockFragmentManager: FragmentManager = mock()
//
//    private val mockPicker: BasicImagePicker = mock()
//
//    @Before
//    fun setUp() {
//        doReturn(mockActivity).whenever(mockFragment).activity
//        doReturn(mockFragmentManager).whenever(mockFragment).fragmentManager
//        doReturn(mockFragmentManager).whenever(mockActivity).fragmentManager
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun buildTestWithActivitySuccess() {
//        val rxImagePicker = instanceWithActivityBuilder()
//
//        verify(mockActivity, times(1)).fragmentManager
//
//        assertEquals(rxImagePicker.builder.fragmentActivity, mockActivity)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun buildTestWithFragmentSuccess() {
//        val rxImagePicker = instanceWithFragmentBuilder()
//
//        verify(mockFragment, times(1)).fragmentManager
//        verify(mockFragment, times(1)).activity
//
//        assertEquals(rxImagePicker.builder.fragmentActivity, mockActivity)
//
//    }
//
//    @Test(expected = java.lang.NullPointerException::class)
//    fun buildTestWithNoContextFailed() {
//        instanceWithNoContextBuilder().build()
//    }
//
//    @Test
//    fun createTestWithNoParamSuccess() {
//        val spy = instanceWithActivityBuilder()
//                .let { spy(it) }
//        doReturn(mockPicker).whenever(spy).create(BasicImagePicker::class.java)
//
//        val defaultImagePicker = spy.create()
//
//        argumentCaptor<Class<Any>>().apply {
//            verify(spy, Times(1)).create(capture())
//            assertEquals(firstValue, BasicImagePicker::class.java)
//        }
//        assertEquals(defaultImagePicker, mockPicker)
//    }
//
//    private fun instanceWithActivityBuilder(): RxImagePicker {
//        return RxImagePicker.Builder()
//                .with(mockActivity)
//                .build()
//    }
//
//    private fun instanceWithFragmentBuilder(): RxImagePicker {
//        return RxImagePicker.Builder()
//                .with(mockFragment)
//                .build()
//    }
//
//    private fun instanceWithNoContextBuilder(): RxImagePicker.Builder {
//        return RxImagePicker.Builder()
//    }
}