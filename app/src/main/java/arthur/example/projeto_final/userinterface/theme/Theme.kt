package arthur.example.projeto_final.userinterface.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CineColorScheme = darkColorScheme(
    primary = Accent,
    onPrimary = Color.White,
    secondary = AccentSoft,
    background = Ink,
    onBackground = CopyColor,
    surface = SurfaceDark,
    onSurface = CopyColor,
    surfaceVariant = SurfaceLight,
    onSurfaceVariant = Muted,
    outline = OutlineColor,
    error = AccentSoft
)

/** O app usa sempre o tema escuro do wireframe (sem cores dinâmicas). */
@Composable
fun CineCatalogTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CineColorScheme,
        typography = Typography,
        content = content
    )
}
