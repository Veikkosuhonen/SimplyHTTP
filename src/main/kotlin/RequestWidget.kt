import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
        "200" -> colors.secondary
        "Error" -> colors.error
        else -> colors.secondaryVariant
    }
    val statusColor: Color by animateColorAsState(responseColor)

    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 2.dp,
        modifier = Modifier
            .animateContentSize()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, colors.secondary, MaterialTheme.shapes.small)
            ) {
                request.response
                    .thenAccept {
                        response = it.statusCode().toString()
                        body = it.body()
                    }.exceptionally {
                        error = it.message.toString()
                        response = "Error"
                        null
                    }

                Text(request.request.method(), color = colors.onSurface, modifier = Modifier.padding(4.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = response,
                    modifier = Modifier.padding(5.dp).background(statusColor))
                Spacer(Modifier.width(8.dp))
                Text(error, color = colors.onSurface, modifier = Modifier.padding(4.dp))
            }
            Spacer(Modifier.height(4.dp))
            if (isExpanded)
            Surface(
                elevation = 2.dp,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(2.dp)
                    .background(colors.surface)
            ) {
                OutlinedTextField(
                    value = body,
                    onValueChange = { },
                    modifier = Modifier.padding(2.dp),
                    readOnly = true,
                )
            }
        }
    }
}