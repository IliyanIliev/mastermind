package com.iliyan.mastermind.presentation.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iliyan.mastermind.R
import com.iliyan.mastermind.domain.model.GuessSlot
import com.iliyan.mastermind.domain.model.LetterResult
import com.iliyan.mastermind.presentation.theme.Green100
import com.iliyan.mastermind.presentation.theme.Orange100
import com.iliyan.mastermind.presentation.theme.Red100

@Composable
fun GameScreen(
    gameState: GameState,
    onAction: (GameAction) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        TimerCircle(
            seconds = gameState.secondsRemaining,
            progress = gameState.progress,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 10.dp)
        )
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))

            GuessRow(
                slots = gameState.slots,
                evaluations = gameState.evaluations,
                onInputChanged = { index, value ->
                    onAction(GameAction.InputChanged(index, value))
                }
            )

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = { onAction(GameAction.CheckClicked) },
                modifier = Modifier.width(280.dp)
            ) {
                Text(stringResource(R.string.check))
            }
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun TimerCircle(
    seconds: Int,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(64.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            strokeWidth = 6.dp,
            modifier = Modifier.fillMaxSize()
        )
        Text(text = seconds.toString())
    }
}

@Composable
private fun GuessRow(
    slots: List<GuessSlot>,
    evaluations: List<LetterResult?>,
    onInputChanged: (index: Int, value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        slots.forEachIndexed { index, slot ->

            val text = when (slot) {
                GuessSlot.Empty -> ""
                is GuessSlot.Filled -> slot.letter.toString()
            }

            val background = resultColor(
                evaluations.getOrNull(index)
            )

            GuessField(
                value = text,
                background = background,
                onValueChange = { newValue ->
                    onInputChanged(index, newValue)
                }
            )
        }
    }
}

@Composable
private fun GuessField(
    value: String,
    background: Color,
    onValueChange: (String) -> Unit
) {
    val shape = RoundedCornerShape(14.dp)

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .size(72.dp)
            .padding(6.dp)
            .border(3.dp, Color.Gray, shape),
        singleLine = true,
        textStyle = TextStyle(fontSize = 22.sp, textAlign = TextAlign.Center),
        shape = shape,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = background,
            unfocusedContainerColor = background,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

private fun resultColor(result: LetterResult?): Color =
    when (result) {
        LetterResult.GREEN -> Green100
        LetterResult.ORANGE -> Orange100
        LetterResult.RED -> Red100
        null -> Color.Transparent
    }
