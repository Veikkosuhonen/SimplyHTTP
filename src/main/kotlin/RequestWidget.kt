import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import java.net.http.HttpResponse

@Composable
fun RequestItem(index: Int, request: RequestInfo, onSelect: (RequestInfo) -> Unit) {
    var error by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("") }
    var isSelected by remember { mutableStateOf(false) }

    request.response
        .thenAccept {
            response = it.statusCode().toString()
        }.exceptionally {
            error = it.message.toString()
            response = "Error"
            null
        }

    val responseColor = when(response) {
        "200" -> Color.Green
        "Error" -> colors.error
        else -> Color.Blue
    }
    val statusColor: Color by animateColorAsState(responseColor)

    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = if (isSelected) 6.dp else 3.dp,
        modifier = Modifier
            //.fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 3.dp)
            .border(1.dp, if (isSelected) colors.primary else Color.DarkGray, MaterialTheme.shapes.small)
            .clickable { onSelect(request); isSelected = true }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(index.toString(), modifier = Modifier.padding(4.dp), color = Color.DarkGray)
            Text(request.request.method(), color = colors.onSurface, modifier = Modifier.padding(4.dp))
            Text(request.request.uri().toString(), color = colors.onSurface, modifier = Modifier.padding(4.dp))
            Spacer(Modifier.width(8.dp))
            Text(text = response,
                color = Color.DarkGray,
                modifier = Modifier.padding(5.dp).background(statusColor),
                fontStyle = FontStyle.Italic
            )
            Spacer(Modifier.width(8.dp))
            Text(error, color = colors.onSurface, modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
fun RequestView(response: HttpResponse<String>) {
    Column(modifier = Modifier.padding(8.dp)) {

        Text(response.request().method() + " " + response.request().uri().toString(), Modifier.padding(4.dp))
        Surface(
            elevation = (-2).dp,
            modifier = Modifier.border(
                    1.dp,
                    Color.DarkGray,
                    shape = MaterialTheme.shapes.medium)
        ) {
            Box {
                val verticalScrollState = rememberScrollState()
                val horizontalScrollState = rememberScrollState()

                Box(Modifier.horizontalScroll(horizontalScrollState).verticalScroll(verticalScrollState)) {
                    Text(
                        text = response.body(),
                        softWrap = false,
                        modifier = Modifier
                            .focusable()
                            .padding(8.dp)
                    )
                }
                HorizontalScrollbar(rememberScrollbarAdapter(horizontalScrollState), Modifier.align(Alignment.BottomCenter))
                VerticalScrollbar(rememberScrollbarAdapter(verticalScrollState), Modifier.align(Alignment.CenterEnd))
            }
        }
    }
}
