package leegroup.module.alarm.ui.screens.setup.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import leegroup.module.alarm.R

@Composable
fun AlarmSetupItemLabel(
    modifier: Modifier = Modifier,
    value: String,
    onTextChange: (String) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.label),
            style = MaterialTheme.typography.bodyMedium,
        )

        BasicTextField(
            value = value,
            onValueChange = onTextChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                textAlign = TextAlign.End
            ),
            cursorBrush = SolidColor(Color.Gray),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd // aligns both text and placeholder to end
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(R.string.optional),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray,
                                textAlign = TextAlign.End
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}