import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.desktop.ui.tooling.preview.Preview
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
import components.ScrollableTextResultField
import components.TextLabel
import components.TextResultField
import java.net.http.HttpResponse

@Composable
fun RequestItem(request: RequestInfo, isSelected: Boolean, onSelect: (RequestInfo) -> Unit) {
    var error by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("") }

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
            .padding(horizontal = 4.dp, vertical = 3.dp)
            .border(1.dp, if (isSelected) colors.primary else Color.DarkGray, MaterialTheme.shapes.small)
            .clickable { onSelect(request) }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(request.index.toString(), modifier = Modifier.padding(4.dp), color = Color.DarkGray)
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

@Preview
@Composable
fun RequestView(response: HttpResponse<String>) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row {
            TextLabel("Request: ", 100.dp)
            TextResultField(response.request().method())
            TextResultField(response.request().uri().toString())
        }
        Spacer(Modifier.height(4.dp))
        Row {
            TextLabel("Response: ", 100.dp)
            TextResultField(response.statusCode().toString())
        }
        Spacer(Modifier.height(6.dp))
        ScrollableTextResultField(Utils.prettyJson(response.body()))
    }
}
