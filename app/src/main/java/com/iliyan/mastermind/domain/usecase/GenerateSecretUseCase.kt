package com.iliyan.mastermind.domain.usecase

import com.iliyan.mastermind.domain.repository.GameRepository

class GenerateSecretUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(): String = repository.generateSecret()
}