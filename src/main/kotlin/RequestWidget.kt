import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RequestWidget(request: RequestInfo) {
    var error by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    val responseColor = when(response) {
        "200" -> MaterialTheme.colors.secondary
        "Error" -> MaterialTheme.colors.error
        else -> MaterialTheme.colors.secondaryVariant
    }
    val surfaceColor: Color by animateColorAsState(responseColor)

    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 2.dp,
        color = surfaceColor,
        modifier = Modifier
            .animateContentSize()
            .padding(2.dp)
            .clickable { isExpanded = !isExpanded }
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row {
                request.response
                    .thenAccept {
                        response = it.statusCode().toString()
                        body = it.body()
                    }.exceptionally {
                        error = it.message.toString()
                        response = "Error"
                        null
                    }

                Text(request.request.method(), color = MaterialTheme.colors.contentColorFor(responseColor), modifier = Modifier.padding(4.dp))
                Spacer(Modifier.width(8.dp))

                Text(text = response, color = MaterialTheme.colors.contentColorFor(responseColor), modifier = Modifier.padding(4.dp))
                Spacer(Modifier.width(8.dp))
                Text(error, color = MaterialTheme.colors.contentColorFor(responseColor), modifier = Modifier.padding(4.dp))
            }
            Spacer(Modifier.height(4.dp))
            if (isExpanded)
            Row {
                Text(text = body, style = MaterialTheme.typography.subtitle2)
            }
        }
    }
}