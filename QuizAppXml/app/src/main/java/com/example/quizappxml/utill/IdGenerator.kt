package com.example.quizappxml.utill

import java.util.concurrent.atomic.AtomicLong

object IdGenerator {
    private val counter = AtomicLong(System.currentTimeMillis())

    fun generateUniqueId(): Long {
        return counter.incrementAndGet()
    }
}