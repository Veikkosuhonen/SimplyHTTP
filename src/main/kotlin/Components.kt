import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LargeTextField(
    value: String,
    onChange: (String) -> Unit,
    isError: Boolean,
    placeholder: String,
) {
    HorizontalScrollableField {
        FormTextField(value, onChange, isError, placeholder)
    }
}

@Composable
fun FormTextField(
    value: String,
    onChange: (String) -> Unit,
    isError: Boolean,
    placeholder: String,
    singleLine: Boolean = false
) = OutlinedTextField(
    value, onChange, isError = isError, placeholder = { PlaceHolder(placeholder) }, singleLine = singleLine,
    modifier = Modifier.fillMaxWidth()
)

@Composable
fun PlaceHolder(text: String) = Text(text, fontStyle = FontStyle.Italic, color = Color.DarkGray)

@Composable
fun FormLabel(text: String) {
    Text(text, fontWeight = FontWeight.Light)
}

@Composable
fun TextLabel(text: String, requiredWidth: Dp = Dp.Unspecified, color: Color = colors.onSurface, style: TextStyle = MaterialTheme.typography.body1) {
    Text(
        text = text,
        modifier = Modifier
            .padding(8.dp)
            .requiredWidthIn(requiredWidth),
        color = color,
        style = style,
    )
}

@Composable
fun ScrollableTextResultField(text: String) {
    TextSurface { HorizontalScrollableField { SelectableTextField(text) }  }
}

@Composable
fun TextResultField(text: String) {
    TextSurface {  SelectableTextField(text) }
}

@Composable
fun SelectableTextField(text: String) {
    SelectionContainer {
        Text(
            text = text,
            softWrap = false,
            modifier = Modifier
                .padding(8.dp),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun HorizontalScrollableField(composable: @Composable() BoxScope.() -> Unit) {
    Box {
        val horizontalScrollState = rememberScrollState()

        Box(Modifier.horizontalScroll(horizontalScrollState).padding(top = 8.dp)) {
            composable()
        }
        HorizontalScrollbar(rememberScrollbarAdapter(horizontalScrollState), Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun TextSurface(composable: @Composable () -> Unit) {
    Surface(
        elevation = (-1).dp,
        modifier = Modifier.border(
            1.dp,
            Color.DarkGray,
            shape = MaterialTheme.shapes.medium)
    ) {
        composable()
    }
}
