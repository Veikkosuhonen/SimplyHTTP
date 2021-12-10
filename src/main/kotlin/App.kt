// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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

                Surface(
                    elevation = 2.dp,
                    modifier = Modifier.padding(4.dp).fillMaxWidth(0.4f)
                ) {
                    RequestForm(onSubmit = { method, url, headers, body ->
                        requests.add(RequestData(method, url, headers, body))
                    })
                }
                Column {
                    Surface(
                        elevation = 2.dp,
                        modifier = Modifier.padding(4.dp).fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.height(300.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(4.dp).verticalScroll(requestListScrollState)
                            ) {
                                if (requests.isEmpty()) TextLabel("Requests", color = Color.DarkGray)
                                val selectedId = selected?.requestData?.index
                                requests.forEach {
                                    RequestItem(it, selectedId == it.index) { response ->
                                        selected = response
                                    }
                                }
                            }
                            VerticalScrollbar(
                                rememberScrollbarAdapter(requestListScrollState),
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                    Surface(
                        elevation = 2.dp,
                        modifier = Modifier.padding(4.dp).fillMaxSize()
                    ) {
                        if (selected == null) TextLabel("Response details", color = Color.DarkGray)
                        else RequestView(selected!!)
                    }
                }
            }
        }
    }
}
