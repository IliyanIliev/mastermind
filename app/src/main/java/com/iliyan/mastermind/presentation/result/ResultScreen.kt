package com.iliyan.mastermind.presentation.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iliyan.mastermind.R
import com.iliyan.mastermind.presentation.theme.Green200
import com.iliyan.mastermind.presentation.theme.Red200

@Composable
fun ResultScreen(
    state: ResultState,
    onAction: (ResultAction) -> Unit
) {
    val bg = if (state.isSuccess) Green200 else Red200
    val title = stringResource(
        if (state.isSuccess) R.string.title_success
        else R.string.title_game_over
    )
    val message = stringResource(
        if (state.isSuccess) R.string.message_success
        else R.string.message_game_over
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Spacer(Modifier.height(12.dp))
            Text(message, style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Spacer(Modifier.height(24.dp))
            Button(onClick = { onAction(ResultAction.RetryClicked) }) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}