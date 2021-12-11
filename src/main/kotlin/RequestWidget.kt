@file:OptIn(ExperimentalTime::class)

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.net.http.HttpResponse
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@Composable
fun RequestItem(requestData: RequestData, isSelected: Boolean, onSelect: (ResponseData?) -> Unit) {

    var response by remember { mutableStateOf<HttpResponse<String>?>(null) }
    var error by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf(Duration.ZERO) }

    LaunchedEffect(requestData) {
        Logger.log("Launching " + requestData.url)
        Request.send(requestData)
            .thenAccept {
                response = it
            }.exceptionally {
                error = it.message.toString()
                null
            }
    }

    if (response == null && error.isEmpty())
    LaunchedEffect(requestData) {
        val step = Duration.milliseconds(10)
        val start = System.currentTimeMillis()
        while (true) {
            delay(step)
            duration = Duration.Companion.milliseconds(System.currentTimeMillis() - start)
        }
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = if (isSelected) 6.dp else 3.dp,
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 3.dp)
            .border(1.dp, if (isSelected) colors.primary else Color.DarkGray, MaterialTheme.shapes.small)
            .clickable { onSelect(
                    if (isSelected) null
                    else ResponseData(requestData, response, duration))
            }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextLabel(requestData.index.toString(), color = Color.DarkGray, requiredWidth = 50.dp)
            TextLabel(requestData.method, style = MaterialTheme.typography.button)
            TextLabel(requestData.url)
            Spacer(Modifier.width(8.dp))
            response?.let {
                Text(text = it.statusCode().toString(),
                    color = Color.DarkGray,
                    modifier = Modifier.padding(4.dp).background(Color.Green),
                    fontStyle = FontStyle.Italic
                )
            }
            if (error.isNotEmpty()) {
                TextLabel(error, color = colors.error)
            }
            Spacer(Modifier.width(8.dp))
            TextLabel(duration.toString(), color = Color.Gray)
        }
    }
}

@Composable
fun RequestView(response: ResponseData) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TextLabel("Request: ", 100.dp, color = Color.Gray)
            TextResultField(response.requestData.method)
            TextResultField(response.requestData.url)
        }
        Divider(Modifier.padding(vertical = 10.dp))
        response.response?.let {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextLabel("Response: ", 100.dp, color = Color.Gray)
                TextResultField(it.statusCode().toString())
                TextLabel("Latency: ", color = Color.Gray)
                TextResultField(response.duration.toString())
            }
            Divider(Modifier.padding(vertical = 10.dp))
            TextLabel("Headers", color = Color.Gray)
            ScrollableTextResultField(Utils.headersToString(it.headers()))
            Spacer(Modifier.height(4.dp))
            TextLabel("Body", color = Color.Gray)
            ScrollableTextResultField(Utils.prettyJson(it.body()))
        }
    }
}
