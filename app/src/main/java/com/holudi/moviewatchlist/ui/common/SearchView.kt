package com.holudi.moviewatchlist.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import  com.holudi.moviewatchlist.R
import com.holudi.moviewatchlist.ui.theme.Shapes

@Composable
fun SearchView(text: String, onTextChange: (String) -> Unit, onClearClicked: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(56.dp),
        shape = Shapes.medium,
        elevation = 2.dp,
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            value = text,
            singleLine = true,
            textStyle = MaterialTheme.typography.subtitle1,
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(onClick = { onClearClicked() }) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                    }
                }
            },
            onValueChange = { onTextChange(it) },
            placeholder = {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                    Text(
                        text = stringResource(id = R.string.search),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Transparent,
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent,
                disabledIndicatorColor = Transparent
            )
        )
    }
}

@Composable
@Preview
fun SearchViewPreview(@PreviewParameter(SearchViewPreviewProvider::class) text: String) {
    SearchView(text = text, onTextChange = {}, onClearClicked = {})
}

class SearchViewPreviewProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf("", "abc")
}