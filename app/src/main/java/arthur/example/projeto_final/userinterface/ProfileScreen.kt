package arthur.example.projeto_final.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arthur.example.projeto_final.userinterface.theme.Accent
import arthur.example.projeto_final.userinterface.theme.AccentSoft
import arthur.example.projeto_final.userinterface.theme.Ink
import arthur.example.projeto_final.userinterface.theme.Muted
import arthur.example.projeto_final.userinterface.theme.Positive
import arthur.example.projeto_final.userinterface.theme.SurfaceDark
import arthur.example.projeto_final.userinterface.theme.SurfaceLight

@Composable
fun ProfileScreen(
    name: String,
    email: String,
    favoritesCount: Int,
    directorsCount: Int,
    onOpenFavorites: () -> Unit,
    onLogout: () -> Unit
) {
    var confirmLogout by rememberSaveable { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().background(Ink).verticalScroll(rememberScrollState())
            .padding(top = statusBarTop() + 24.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
    ) {
        PageHeader("SUA CONTA", "Perfil")
        Spacer(Modifier.height(22.dp))

        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                .background(Brush.linearGradient(listOf(SurfaceLight, Color(0xFF181E2B)))).padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InitialsAvatar(name, size = 62)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(name, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(5.dp))
                Text(email, fontSize = 12.sp, color = Muted)
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatTile(favoritesCount.toString(), "Filmes favoritos", Modifier.weight(1f))
            StatTile(directorsCount.toString(), "Diretores nos favoritos", Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(SurfaceDark)
                .clickable(onClick = onOpenFavorites).padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(Accent.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Outlined.FavoriteBorder, null, tint = AccentSoft) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Meus favoritos", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Acesse sua coleção salva", fontSize = 10.sp, color = Muted)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Muted)
        }

        Spacer(Modifier.height(26.dp))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFF151B27)).padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(9.dp).clip(CircleShape).background(Positive))
            Spacer(Modifier.width(13.dp))
            Column {
                Text("Sessão protegida", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Sua conta será restaurada ao reabrir o app.", fontSize = 11.sp, color = Muted)
            }
        }

        Spacer(Modifier.height(14.dp))
        PrimaryButton(
            text = "Sair da conta",
            onClick = { confirmLogout = true },
            secondary = true,
            destructive = true,
            icon = Icons.AutoMirrored.Outlined.Logout,
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (confirmLogout) {
        AlertDialog(
            onDismissRequest = { confirmLogout = false },
            containerColor = SurfaceDark,
            title = { Text("Sair da conta?", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Você precisará entrar novamente para acessar seu perfil.", color = Muted) },
            confirmButton = {
                TextButton(onClick = { confirmLogout = false; onLogout() }) {
                    Text("Sim, sair", color = AccentSoft, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmLogout = false }) { Text("Cancelar", color = Color.White) }
            }
        )
    }
}

@Composable
private fun StatTile(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(14.dp)).background(SurfaceDark).padding(18.dp)) {
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(Modifier.height(5.dp))
        Text(label, fontSize = 11.sp, color = Muted)
    }
}
