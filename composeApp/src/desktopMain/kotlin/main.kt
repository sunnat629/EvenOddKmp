import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "EvenOddKmp",
    ) {
        val isEven  = isEven(3)

        App()
    }
}