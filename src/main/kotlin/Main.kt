// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


@Composable
@Preview
fun App() {
    DesktopMaterialTheme {
        Row {
            val requests by remember { mutableStateOf(mutableStateListOf<RequestInfo>()) }
            val stateVertical = rememberScrollState(0)

            Surface(
                elevation = 2.dp,
                modifier = Modifier.padding(4.dp)
            ) {
                Column {
                    UrlInputField(onSubmit = {
                        requests.add(Request.get(it))
                    })
                }
            }
            Surface(
                elevation = 2.dp,
                modifier = Modifier.padding(4.dp).fillMaxSize()
            ) {
                LazyColumn(modifier = Modifier.padding(4.dp)) {
                    item {}
                    items(requests) {
                        RequestWidget(it)
                        Spacer(Modifier.height(4.dp))
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
