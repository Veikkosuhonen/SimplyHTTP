// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.CoroutineName
import java.lang.Exception

public val colors = darkColors()

@Composable
@Preview()
fun App() {
    DesktopMaterialTheme(
        colors = colors,
    ) {
        Surface(Modifier.fillMaxSize(), elevation = 1.dp)  {
            Row(Modifier.padding(4.dp)) {
                val requests by remember { mutableStateOf(mutableStateListOf<RequestData>()) }
                var selected by remember { mutableStateOf<ResponseData?>(null) }
                val requestListScrollState = rememberScrollState()

                var index = 0

                Surface(
                    elevation = 2.dp,
                    modifier = Modifier.padding(4.dp)
                ) {
                    RequestForm(onSubmit = { url, headers, body ->
                        requests.add(RequestData(index++, "GET", url, headers, body))
                    })
                }
                Column {

                    Surface(
                        elevation = 2.dp,
                        modifier = Modifier.padding(4.dp).fillMaxWidth()
                    ) {

                        Row(
                            modifier = Modifier.height(300.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(4.dp).verticalScroll(requestListScrollState)
                            ) {
                                requests.forEach {
                                    RequestItem(it, false) { response ->
                                        selected = response
                                    }
                                }
                            }
                            VerticalScrollbar(rememberScrollbarAdapter(requestListScrollState))
                        }
                    }
                    selected?.let {
                        Surface(
                            elevation = 2.dp,
                            modifier = Modifier.padding(4.dp).fillMaxSize()
                        ) {
                            RequestView(it)
                        }
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
