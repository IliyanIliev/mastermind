package com.iliyan.mastermind.presentation.result

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class ResultViewModel(isSuccess: Boolean) : ViewModel() {

    private val _state = MutableStateFlow(ResultState(isSuccess))
    val state: StateFlow<ResultState> = _state.asStateFlow()

    private val _events = Channel<ResultEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: ResultAction) {
        when (action) {
            ResultAction.RetryClicked -> _events.trySend(ResultEvent.Retry)
        }
    }
}