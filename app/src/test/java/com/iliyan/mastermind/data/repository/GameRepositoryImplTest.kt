package com.iliyan.mastermind.data.repository

import com.iliyan.mastermind.domain.model.GameRules
import com.iliyan.mastermind.domain.model.GuessSlot
import com.iliyan.mastermind.domain.model.LetterResult
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameRepositoryImplTest {

    private lateinit var repository: GameRepositoryImpl

    @Before
    fun setup() {
        repository = GameRepositoryImpl()
    }

    private fun guess(vararg chars: Char?): List<GuessSlot> {
        return chars.map {
            if (it == null) GuessSlot.Empty
            else GuessSlot.Filled(it)
        }
    }

    @Test
    fun `generateSecret returns the correct number of uppercase letters`() {
        val secret = repository.generateSecret()

        assertEquals(GameRules.GAME_LENGTH, secret.length)
        assertTrue(secret.all { it in 'A'..'Z' })
    }

    @Test(expected = IllegalArgumentException::class)
    fun `checkGuess throws if secret length is not accurate`() {
        repository.checkGuess("ABC", guess('A', 'B', 'C', 'D'))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `checkGuess throws if guess size is not accurate`() {
        repository.checkGuess("ABCD", listOf(GuessSlot.Empty))
    }

    @Test
    fun `all letters GREEN when guess matches secret`() {
        val result = repository.checkGuess(
            secret = "ABCD",
            guess = guess('A', 'B', 'C', 'D')
        )

        val evaluations = result.positions

        assertEquals(LetterResult.GREEN, evaluations[0].result)
        assertEquals(LetterResult.GREEN, evaluations[1].result)
        assertEquals(LetterResult.GREEN, evaluations[2].result)
        assertEquals(LetterResult.GREEN, evaluations[3].result)
    }

    @Test
    fun `all letters RED when none exist in secret`() {
        val result = repository.checkGuess(
            secret = "ABCD",
            guess = guess('X', 'Y', 'Z', 'W')
        )

        result.positions.forEach {
            assertEquals(LetterResult.RED, it.result)
        }
    }

    @Test
    fun `all letters ORANGE when correct letters wrong positions`() {
        val result = repository.checkGuess(
            secret = "ABCD",
            guess = guess('B', 'A', 'D', 'C')
        )

        result.positions.forEach {
            assertEquals(LetterResult.ORANGE, it.result)
        }
    }

    @Test
    fun `mix of GREEN ORANGE RED`() {
        val result = repository.checkGuess(
            secret = "ABCD",
            guess = guess('A', 'C', 'X', 'D')
        )

        val evaluations = result.positions

        assertEquals(LetterResult.GREEN, evaluations[0].result)   // A correct
        assertEquals(LetterResult.ORANGE, evaluations[1].result)  // C wrong pos
        assertEquals(LetterResult.RED, evaluations[2].result)     // X not exist
        assertEquals(LetterResult.GREEN, evaluations[3].result)   // D correct
    }

    @Test
    fun `empty slots return null result`() {
        val result = repository.checkGuess(
            secret = "ABCD",
            guess = guess(null, null, null, null)
        )

        result.positions.forEach {
            assertNull(it.result)
        }
    }

    @Test
    fun `duplicate letters are evaluated based on current simple contains logic`() {
        val result = repository.checkGuess(
            secret = "AABC",
            guess = guess('A', 'A', 'A', 'A')
        )

        val evaluations = result.positions

        assertEquals(LetterResult.GREEN, evaluations[0].result)
        assertEquals(LetterResult.GREEN, evaluations[1].result)
        assertEquals(LetterResult.ORANGE, evaluations[2].result)
        assertEquals(LetterResult.ORANGE, evaluations[3].result)
    }
}