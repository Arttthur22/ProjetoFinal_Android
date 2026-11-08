package arthur.example.projeto_final.userinterface.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    headlineMedium = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, lineHeight = 30.sp, letterSpacing = (-0.7).sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 29.sp, lineHeight = 33.sp, letterSpacing = (-0.8).sp),
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 19.sp, letterSpacing = (-0.3).sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
    bodyMedium = TextStyle(fontSize = 13.sp, lineHeight = 21.sp),
    bodySmall = TextStyle(fontSize = 11.sp, lineHeight = 15.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, letterSpacing = 1.7.sp)
)
