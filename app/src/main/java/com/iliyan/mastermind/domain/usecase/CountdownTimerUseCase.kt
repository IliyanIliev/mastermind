package com.iliyan.mastermind.domain.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class CountdownTimerUseCase {
    operator fun invoke(totalSeconds: Int): Flow<Int> = flow {
        for (secondsLeft in totalSeconds downTo 0) {
            emit(secondsLeft)
            if (secondsLeft > 0) {
                delay(1.seconds)
            }
        }
    }
}