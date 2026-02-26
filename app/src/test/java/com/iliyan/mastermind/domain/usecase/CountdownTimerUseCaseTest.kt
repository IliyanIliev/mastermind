package com.iliyan.mastermind.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class CountdownTimerUseCaseTest {

    private lateinit var useCase: CountdownTimerUseCase

    @Before
    fun setup() {
        useCase = CountdownTimerUseCase()
    }

    @Test
    fun `countdown emits immediately and then every second until zero`() = runTest {
        val emitted = mutableListOf<Int>()

        val job = launch {
            useCase(totalSeconds = 3).toList(emitted)
        }

        // First value is emitted immediately
        runCurrent()
        assertEquals(listOf(3), emitted)

        // Advance 1 second -> expect 2
        advanceTimeBy(1.seconds)
        runCurrent()
        assertEquals(listOf(3, 2), emitted)

        // Advance another second -> expect 1
        advanceTimeBy(1.seconds)
        runCurrent()
        assertEquals(listOf(3, 2, 1), emitted)

        // Advance another second -> expect 0
        advanceTimeBy(1.seconds)
        runCurrent()
        assertEquals(listOf(3, 2, 1, 0), emitted)

        job.cancel()
    }

    @Test
    fun `countdown with zero emits only zero`() = runTest {
        val emitted = useCase(totalSeconds = 0).toList()

        assertEquals(listOf(0), emitted)
    }

    @Test
    fun `countdown completes after reaching zero`() = runTest {
        val emitted = mutableListOf<Int>()

        useCase(totalSeconds = 1).toList(emitted)

        assertEquals(listOf(1, 0), emitted)
    }
}