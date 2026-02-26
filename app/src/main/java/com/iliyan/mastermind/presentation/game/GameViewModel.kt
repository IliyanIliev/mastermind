package com.iliyan.mastermind.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iliyan.mastermind.domain.model.GameRules.GAME_LENGTH
import com.iliyan.mastermind.domain.model.GuessSlot
import com.iliyan.mastermind.domain.usecase.CheckGuessUseCase
import com.iliyan.mastermind.domain.usecase.CountdownTimerUseCase
import com.iliyan.mastermind.domain.usecase.GenerateSecretUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val generateSecret: GenerateSecretUseCase,
    private val checkGuess: CheckGuessUseCase,
    private val countdownTimer: CountdownTimerUseCase
) : ViewModel() {

    private var secret: String = ""
    private var timerJob: Job? = null

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        startNewGame()
    }

    private fun startNewGame() {
        secret = generateSecret()
        println("Generated secret: $secret")
        _gameState.update { GameState() }
        startTimer()
    }


    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            countdownTimer(60)
                .onEach { sec ->
                    _gameState.update { it.copy(secondsRemaining = sec) }
                }
                .onCompletion { cause ->
                    if (cause == null) {
                        _events.send(GameEvent.NavigateToResult(isSuccess = false))
                    }
                }
                .collect()
        }
    }

    fun onAction(action: GameAction) {
        when (action) {
            is GameAction.InputChanged -> updateSlot(index = action.index, value = action.value)
            is GameAction.CheckClicked -> evaluateGuess()
        }
    }

    fun updateSlot(index: Int, value: String) {
        val letter = value.firstOrNull()?.uppercaseChar()
        require(index in _gameState.value.slots.indices) {
            "Invalid slot index: $index"
        }
        _gameState.update { state ->
            val updatedSlots = state.slots.toMutableList()

            updatedSlots[index] =
                if (letter == null) GuessSlot.Empty
                else GuessSlot.Filled(letter)

            state.copy(
                slots = updatedSlots,
                evaluations = List(GAME_LENGTH) { null }
            )
        }
    }

    private fun evaluateGuess() {
        val state = _gameState.value

        val result = checkGuess(secret, state.slots)

        _gameState.update {
            it.copy(
                evaluations = result.positions.map { evaluation -> evaluation.result }
            )
        }

        if (result.isWin) {
            timerJob?.cancel()
            viewModelScope.launch {
                _events.send(
                    GameEvent.NavigateToResult(isSuccess = true)
                )
            }
        }
    }
}