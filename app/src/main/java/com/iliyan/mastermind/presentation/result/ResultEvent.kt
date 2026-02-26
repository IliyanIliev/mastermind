package com.iliyan.mastermind.presentation.result

sealed interface ResultEvent {
    data object Retry : ResultEvent
}