import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(
    title = "Postiman API Tester",
    state = WindowState(width = 1280.dp, height = 768.dp),
    icon = BitmapPainter(useResource("Postiman.png", ::loadImageBitmap)),
) {
    App()
}