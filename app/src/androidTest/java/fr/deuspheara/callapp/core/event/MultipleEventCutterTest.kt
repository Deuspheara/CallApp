package fr.deuspheara.callapp.core.event

import junit.framework.TestCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.core.event.MultipleEventCutterTest
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Test for multiple event cutter
 *
 */
class MultipleEventsCutterTest {


    private lateinit var cutter: MultipleEventsCutterImpl

    @Before
    fun setUp() {
        cutter = MultipleEventsCutterImpl
    }

    @Test
    fun testMultipleEventsCutter() = runBlocking {

        var clicked = false
        cutter.processEvent { clicked = true }
        TestCase.assertTrue(clicked)

        clicked = false
        cutter.processEvent { clicked = true }
        TestCase.assertFalse(clicked)

        delay(1000)
        cutter.processEvent { clicked = true }
        TestCase.assertTrue(clicked)
    }

}