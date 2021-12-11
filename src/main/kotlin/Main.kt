import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(
    title = "SimplyHTTP",
    state = WindowState(width = 1280.dp, height = 768.dp),
    icon = BitmapPainter(useResource("icon.png", ::loadImageBitmap)),
) {
    App()
}