import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private fun completeUrl(url: String): String {
    var result: String = url
    if (!url.matches(Regex("^https?://.*"))) {
        result = "https://$result"
    }
    return result
}

@Composable
fun RequestForm(onSubmit: (String, String, String, String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        var urlInput by remember { mutableStateOf("") }
        var urlResult by remember { mutableStateOf(completeUrl(urlInput)) }
        var validUrl by remember { mutableStateOf(false) }
        var validHeaders by remember { mutableStateOf(true) }
        var headers by remember { mutableStateOf("") }
        var body by remember { mutableStateOf("") }
        var method by remember { mutableStateOf("GET") }
        var expanded by remember { mutableStateOf(false) }

        Row(verticalAlignment = Alignment.CenterVertically) {
            MethodSelector(method, { method = it }, expanded, { expanded = it })
            Spacer(Modifier.width(12.dp))
            Button(onClick = { onSubmit(method, urlResult, headers, body) }, enabled = validUrl && validHeaders) {
                Text("Send request")
            }
        }
        UrlInput(urlInput, urlResult, validUrl) {
            urlInput = it
            urlResult = completeUrl(urlInput)
            validUrl = Request.validateUrl(urlResult)
        }
        HeadersInput(headers, validHeaders) {
            headers = it
            validHeaders = Request.validateHeaders(headers)
        }
        BodyInput(body, validUrl) {
            body = it
        }
    }
}

@Composable
private fun MethodSelector(method: String, onChange: (String) -> Unit, expanded: Boolean, onOpen: (Boolean) -> Unit) {
    FormLabel("Method")
    Spacer(Modifier.width(4.dp))
    Box {
        TextButton(onClick = { onOpen(true) }, border = BorderStroke(1.dp, Color.DarkGray)) {
            Text(method)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { onOpen(false) }) {
            Request.METHODS.forEach {
                DropdownMenuItem(onClick = { onChange(it); onOpen(false) }) {
                    if (method == it)
                        TextLabel(it, color = colors.primary, style = MaterialTheme.typography.button)
                    else
                        TextLabel(it, style = MaterialTheme.typography.button)
                }
            }
        }
    }
}

@Composable
private fun UrlInput(input: String, result: String, valid: Boolean, onChange: (String) -> Unit) {
    val fieldColor: Color by animateColorAsState(if (valid) colors.secondary else colors.error)

    Column {
        FormTextField(input, { onChange(it) }, isError = !valid, placeholder = "url", singleLine = true)
        Spacer(modifier = Modifier.height(4.dp))
        Text(result,
            color = fieldColor,
            modifier = Modifier.border(1.dp, shape = MaterialTheme.shapes.small, color = fieldColor).padding(4.dp),
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
private fun HeadersInput(input: String, valid: Boolean, onChange: (String) -> Unit) {
    Column {
        FormLabel("Headers")
        FormTextField(input, { onChange(it) }, isError = !valid, placeholder = "no headers")
    }
}

@Composable
private fun BodyInput(input: String, valid: Boolean, onChange: (String) -> Unit) {
    Column {
        FormLabel("Body")
        FormTextField(input, { onChange(it) }, isError = false, placeholder = "empty body")
    }
}


