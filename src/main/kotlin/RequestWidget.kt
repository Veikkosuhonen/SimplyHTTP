@file:OptIn(ExperimentalTime::class)

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import components.ScrollableTextResultField
import components.TextLabel
import components.TextResultField
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
                .fillMaxWidth()
        ) {
            TextLabel(requestData.index.toString(), color = Color.DarkGray)
            TextLabel(requestData.method)
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
            TextLabel(duration.toString())
        }
    }
}

@Composable
fun RequestView(response: ResponseData) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TextLabel("Request: ", 100.dp)
            TextResultField(response.requestData.method)
            TextResultField(response.requestData.url)
        }
        Spacer(Modifier.height(4.dp))
        response.response?.let {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextLabel("Response: ", 100.dp)
                TextResultField(it.statusCode().toString())
                TextLabel("Latency: ", color = Color.DarkGray)
                TextLabel(response.duration.toString())
            }
            Spacer(Modifier.height(6.dp))
            ScrollableTextResultField(Utils.prettyJson(it.body()))
        }
    }
}
