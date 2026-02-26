package com.iliyan.mastermind.di

import com.iliyan.mastermind.data.repository.GameRepositoryImpl
import com.iliyan.mastermind.domain.repository.GameRepository
import com.iliyan.mastermind.domain.usecase.CheckGuessUseCase
import com.iliyan.mastermind.domain.usecase.CountdownTimerUseCase
import com.iliyan.mastermind.domain.usecase.GenerateSecretUseCase
import com.iliyan.mastermind.presentation.game.GameViewModel
import com.iliyan.mastermind.presentation.result.ResultViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<GameRepository> { GameRepositoryImpl() }

    factory { GenerateSecretUseCase(get()) }
    factory { CheckGuessUseCase(get()) }
    factory { CountdownTimerUseCase() }

    viewModel {
        GameViewModel(
            generateSecret = get(),
            checkGuess = get(),
            countdownTimer = get()
        )
    }

    viewModel { (isSuccess: Boolean) ->
        ResultViewModel(isSuccess)
    }
}