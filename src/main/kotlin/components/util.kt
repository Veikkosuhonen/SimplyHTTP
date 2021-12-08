package components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TextLabel(text: String, requiredWidth: Dp = Dp.Unspecified) {
    Text(
        text = text,
        modifier = Modifier
            .padding(8.dp)
            .requiredWidthIn(requiredWidth)
    )
}

@Composable
fun ScrollableTextResultField(text: String) {
    ResultSurface { ScrollableField { SelectableTextField(text) }  }
}

@Composable
fun TextResultField(text: String) {
    ResultSurface {  SelectableTextField(text) }
}

@Composable
fun SelectableTextField(text: String) {
    SelectionContainer {
        Text(
            text = text,
            softWrap = false,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Composable
fun ScrollableField(composable: @Composable() BoxScope.() -> Unit) {
    Box {
        val verticalScrollState = rememberScrollState()
        val horizontalScrollState = rememberScrollState()

        Box(Modifier.horizontalScroll(horizontalScrollState).verticalScroll(verticalScrollState)) {
            composable()
        }
        HorizontalScrollbar(rememberScrollbarAdapter(horizontalScrollState), Modifier.align(Alignment.BottomCenter))
        VerticalScrollbar(rememberScrollbarAdapter(verticalScrollState), Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun ResultSurface(composable: @Composable () -> Unit) {
    Surface(
        elevation = (-2).dp,
        modifier = Modifier.border(
            1.dp,
            Color.DarkGray,
            shape = MaterialTheme.shapes.medium)
    ) {
        composable()
    }
}