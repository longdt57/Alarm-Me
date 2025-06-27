package leegroup.module.photosample.ui.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun QueryBox(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = { onQueryChanged(it) },
        placeholder = { Text(text = "Search...") },
        leadingIcon = {
            IconButton(onClick = { /* Handle search icon click */ }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            }
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        },
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent, // Remove the focused underline
            unfocusedIndicatorColor = Color.Transparent, // Remove the unfocused underline
        )
    )
}

@Preview(showBackground = true)
@Composable
fun QueryBoxEmptyPreview() {
    var query by remember { mutableStateOf("") }
    leegroup.module.designsystem.theme.ComposeTheme {
        QueryBox(
            query = query,
            onQueryChanged = { query = it },
            onClear = { query = "" },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QueryBoxPreview() {
    var query by remember { mutableStateOf("Nature") }
    leegroup.module.designsystem.theme.ComposeTheme {
        QueryBox(
            query = query,
            onQueryChanged = { query = it },
            onClear = { query = "" },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
