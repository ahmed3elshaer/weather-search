package com.ahmed3elshaer.weather.core

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

class FakeDispatcher : DispatcherProvider {
    private val testDispatcher = StandardTestDispatcher()
    override fun io() = testDispatcher

    override fun main() = testDispatcher

    override fun default() = testDispatcher

    override fun unconfined() = testDispatcher
}