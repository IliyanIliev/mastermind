package com.iliyan.mastermind.presentation.result

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResultViewModelTest {

    @Test
    fun `initial state contains correct success value true`() {
        val viewModel = ResultViewModel(isSuccess = true)

        assertEquals(true, viewModel.state.value.isSuccess)
    }

    @Test
    fun `initial state contains correct success value false`() {
        val viewModel = ResultViewModel(isSuccess = false)

        assertEquals(false, viewModel.state.value.isSuccess)
    }

    @Test
    fun `RetryClicked emits Retry event`() = runTest {
        val viewModel = ResultViewModel(isSuccess = true)

        viewModel.onAction(ResultAction.RetryClicked)

        val event = viewModel.events.first()

        assertEquals(ResultEvent.Retry, event)
    }

    @Test
    fun `RetryClicked does not change state`() = runTest {
        val viewModel = ResultViewModel(isSuccess = true)

        viewModel.onAction(ResultAction.RetryClicked)

        assertEquals(true, viewModel.state.value.isSuccess)
    }
}