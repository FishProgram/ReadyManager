package com.fish.readymanager

import org.junit.Test
import kotlin.concurrent.thread
import org.junit.Assert.*

class ReadyUnitTest {

    private val readyManager = ReadyManager()
    @Test
    fun commonUseTest(){
        readyManager.addTagJob("tag1")
        readyManager.addTagJob("tag2")
        readyManager.addTagJob("tag3")
        var endResult = "incorrect"
        readyManager.addListener(OnceObserver("obs",true) {endResult="correct";println("listener")})
        thread {
            println("tag1")
            readyManager.apply("tag1")
            Thread.sleep(1000)
            println("tag2")
            readyManager.apply("tag2")
            Thread.sleep(1000)
            println("tag3")
            readyManager.apply("tag3")
            Thread.sleep(1000)

        }.join()
        assertEquals(endResult,"correct")

    }
}