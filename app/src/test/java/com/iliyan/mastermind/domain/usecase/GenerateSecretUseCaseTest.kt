package com.iliyan.mastermind.domain.usecase

import com.iliyan.mastermind.domain.repository.GameRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GenerateSecretUseCaseTest {

    private lateinit var repository: GameRepository
    private lateinit var useCase: GenerateSecretUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = GenerateSecretUseCase(repository)
    }

    @Test
    fun `invoke calls repository generateSecret and returns result`() {
        val expectedSecret = "ABCD"
        whenever(repository.generateSecret()).thenReturn(expectedSecret)

        val result = useCase()

        assertEquals(expectedSecret, result)
        verify(repository).generateSecret()
    }
}