import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import com.genuino.sample.App

fun MainViewController(): UIViewController = ComposeUIViewController {
    App()
}
