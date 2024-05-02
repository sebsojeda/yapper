package com.github.sebsojeda.yapper.features.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sebsojeda.yapper.R
import com.github.sebsojeda.yapper.core.extensions.topBorder
import com.github.sebsojeda.yapper.ui.theme.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInput(
    focus: Boolean,
    onCreateMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    var content by remember { mutableStateOf("") }

    LaunchedEffect(focus) {
        if (focus) {
            focusRequester.requestFocus()
        }
    }

    Box(
        modifier = modifier
            .topBorder(1.dp, Colors.Neutral200)
            .padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .background(Colors.Neutral200)
        ) {
            BasicTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { hasFocus = it.isFocused },
                decorationBox = @Composable { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = content,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = false,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        placeholder = {
                            Text(
                                text = "Start a message",
                                color = Colors.Neutral400
                            )
                        },
                        contentPadding = PaddingValues(),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Colors.Transparent,
                            unfocusedIndicatorColor = Colors.Transparent,
                            focusedContainerColor = Colors.Transparent,
                            focusedIndicatorColor = Colors.Transparent,
                            cursorColor = Colors.Neutral950
                        )
                    )
                }
            )
            IconButton(
                onClick = {
                    onCreateMessage(content)
                    content = ""
                },
                enabled = content.isNotBlank(),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Colors.Indigo500,
                    contentColor = Colors.White,
                    disabledContainerColor = Colors.Indigo300,
                    disabledContentColor = Colors.Indigo500
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.paper_airplane_mini),
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatInputPreview() {
    ChatInput(
        focus = false,
        onCreateMessage = { }
    )
}