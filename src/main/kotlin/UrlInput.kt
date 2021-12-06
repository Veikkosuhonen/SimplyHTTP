import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import java.net.URI

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UrlInputField(onSubmit: (String) -> Unit) {
    var input by remember { mutableStateOf("") }
    var valid by remember { mutableStateOf(false) }

    fun completeUrl(url: String): String {
        var result: String = url
        if (!url.matches(Regex("^https?://.*"))) {
            result = "https://$result"
        }
        return result
    }

    val finalUrlColor: Color by animateColorAsState(if (valid) colors.secondary else colors.error)
    val urlResult = completeUrl(input)
    valid = Request.validateUrl(urlResult)

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        TextField(input, { input = it }, singleLine = true, keyboardActions = KeyboardActions { if (this.equals(Key.Enter)) onSubmit(urlResult) })
        Spacer(modifier = Modifier.height(4.dp))
        Text(urlResult,
            color = finalUrlColor,
            modifier = Modifier.border(1.dp, shape = MaterialTheme.shapes.small, color = finalUrlColor).padding(4.dp),
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = { onSubmit(urlResult) }, enabled = valid) {
            Text("Send request")
        }
    }
}