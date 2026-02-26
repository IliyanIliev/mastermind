package com.iliyan.mastermind.presentation.game

import com.iliyan.mastermind.MainDispatcherRule
import com.iliyan.mastermind.domain.model.*
import com.iliyan.mastermind.domain.usecase.CheckGuessUseCase
import com.iliyan.mastermind.domain.usecase.CountdownTimerUseCase
import com.iliyan.mastermind.domain.usecase.GenerateSecretUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

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
        countdownTimer = mock()

        whenever(generateSecret()).thenReturn("ABCD")

        whenever(countdownTimer.invoke(any())).thenReturn(
            flow {
                emit(60)
                emit(59)
                emit(0)
            }
        )

        viewModel = GameViewModel(
            generateSecret,
            checkGuess,
            countdownTimer
        )
    }


    @Test
    fun `init generates secret and starts timer`() = runTest {
        verify(generateSecret).invoke()
        verify(countdownTimer).invoke(60)
    }

    @Test
    fun `timer updates secondsRemaining`() = runTest {
        advanceUntilIdle()

        assertEquals(0, viewModel.gameState.value.secondsRemaining)
    }

    @Test
    fun `timer completion sends game over event`() = runTest {
        advanceUntilIdle()

        val event = viewModel.events.first()
        assertEquals(GameEvent.NavigateToResult(false), event)
    }

    @Test
    fun `InputChanged updates slot with uppercase letter`() = runTest {
        viewModel.onAction(GameAction.InputChanged(index = 0, value = "a"))

        val slot = viewModel.gameState.value.slots[0]

        assertEquals(GuessSlot.Filled('A'), slot)
    }

    @Test
    fun `InputChanged sets empty when input is blank`() = runTest {
        viewModel.onAction(GameAction.InputChanged(index = 0, value = ""))

        val slot = viewModel.gameState.value.slots[0]

        assertEquals(GuessSlot.Empty, slot)
    }

    @Test
    fun `InputChanged resets evaluations`() = runTest {
        whenever(checkGuess(any(), any())).thenReturn(
            GuessResult(
                positions = List(4) {
                    PositionEvaluation(GuessSlot.Empty, LetterResult.RED)
                }
            )
        )

        viewModel.onAction(GameAction.CheckClicked)

        // Now change input
        viewModel.onAction(GameAction.InputChanged(0, "A"))

        assertEquals(
            listOf(null, null, null, null),
            viewModel.gameState.value.evaluations
        )
    }

    @Test
    fun `CheckClicked updates evaluations`() = runTest {
        val guessSlots = viewModel.gameState.value.slots

        val result = GuessResult(
            positions = listOf(
                PositionEvaluation(guessSlots[0], LetterResult.GREEN),
                PositionEvaluation(guessSlots[1], LetterResult.RED),
                PositionEvaluation(guessSlots[2], LetterResult.ORANGE),
                PositionEvaluation(guessSlots[3], LetterResult.RED),
            )
        )

        whenever(checkGuess(any(), any())).thenReturn(result)

        viewModel.onAction(GameAction.CheckClicked)

        val evaluations = viewModel.gameState.value.evaluations

        assertEquals(
            listOf(
                LetterResult.GREEN,
                LetterResult.RED,
                LetterResult.ORANGE,
                LetterResult.RED
            ),
            evaluations
        )
    }
}