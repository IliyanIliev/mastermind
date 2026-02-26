package com.iliyan.mastermind.domain.usecase

import com.iliyan.mastermind.domain.model.GuessResult
import com.iliyan.mastermind.domain.model.GuessSlot
import com.iliyan.mastermind.domain.model.LetterResult
import com.iliyan.mastermind.domain.model.PositionEvaluation
import com.iliyan.mastermind.domain.repository.GameRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class CheckGuessUseCaseTest {

    private lateinit var repository: GameRepository
    private lateinit var useCase: CheckGuessUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = CheckGuessUseCase(repository)
    }

    @Test
    fun `invoke calls repository checkGuess with correct parameters and returns result`() {
        val secret = "ABCD"
        val guess = listOf(
            GuessSlot.Filled('A'),
            GuessSlot.Filled('B'),
            GuessSlot.Filled('C'),
            GuessSlot.Filled('D')
        )

        val expectedResult = GuessResult(
            positions = listOf(
                PositionEvaluation(guess[0], LetterResult.GREEN),
                PositionEvaluation(guess[1], LetterResult.GREEN),
                PositionEvaluation(guess[2], LetterResult.GREEN),
                PositionEvaluation(guess[3], LetterResult.GREEN),
            )
        )

        whenever(repository.checkGuess(secret, guess))
            .thenReturn(expectedResult)

        val result = useCase(secret, guess)

        assertEquals(expectedResult, result)
        verify(repository, times(1)).checkGuess(secret, guess)
        verifyNoMoreInteractions(repository)
    }
}