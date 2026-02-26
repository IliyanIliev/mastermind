package com.iliyan.mastermind.presentation.game

import com.iliyan.mastermind.MainDispatcherRule
import com.iliyan.mastermind.domain.model.*
import com.iliyan.mastermind.domain.usecase.CheckGuessUseCase
import com.iliyan.mastermind.domain.usecase.CountdownTimerUseCase
import com.iliyan.mastermind.domain.usecase.GenerateSecretUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.time.Duration.Companion.seconds

/**
 * Integration tests using real CountdownTimerUseCase.
 * These tests verify coroutine timing and cancellation behavior.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelIntegrationTest {

    private lateinit var generateSecret: GenerateSecretUseCase
    private lateinit var checkGuess: CheckGuessUseCase
    private lateinit var countdownTimer: CountdownTimerUseCase

    private lateinit var viewModel: GameViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        generateSecret = mock()
        checkGuess = mock()
        countdownTimer = CountdownTimerUseCase() // REAL implementation

        whenever(generateSecret()).thenReturn("ABCD")
    }

    @Test
    fun `real timer reaching zero triggers game over`() = runTest {
        viewModel = GameViewModel(
            generateSecret,
            checkGuess,
            countdownTimer
        )

        // Advance full 60 seconds
        advanceTimeBy(60.seconds)
        runCurrent()

        val event = viewModel.events.first()

        assertEquals(GameEvent.NavigateToResult(false), event)
    }

    @Test
    fun `winning cancels timer and prevents game over`() = runTest {
        whenever(checkGuess(any(), any())).thenReturn(
            GuessResult(
                positions = List(4) {
                    PositionEvaluation(
                        GuessSlot.Filled('A'),
                        LetterResult.GREEN
                    )
                }
            )
        )

        viewModel = GameViewModel(
            generateSecret,
            checkGuess,
            countdownTimer
        )

        // Trigger win
        viewModel.onAction(GameAction.CheckClicked)

        // Advance full time if timer wasn't cancelled it would emit GameOver
        advanceTimeBy(60.seconds)
        runCurrent()

        val event = viewModel.events.first()

        assertEquals(GameEvent.NavigateToResult(true), event)
    }
}