package arthur.example.projeto_final.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import arthur.example.projeto_final.data.Movie
import arthur.example.projeto_final.userinterface.theme.AccentSoft
import arthur.example.projeto_final.userinterface.theme.Ink
import arthur.example.projeto_final.userinterface.theme.Muted
import arthur.example.projeto_final.userinterface.theme.Positive
import arthur.example.projeto_final.userinterface.theme.StarColor
import arthur.example.projeto_final.userinterface.theme.SurfaceDark
import arthur.example.projeto_final.viewmodel.DetailUiState

@Composable
fun DetailScreen(
    state: DetailUiState,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onToggleFavorite: (Movie) -> Unit
) {
    Box(Modifier.fillMaxSize().background(Ink)) {
        when (state) {
            is DetailUiState.Loading -> LoadingState(Modifier.align(Alignment.Center), "Carregando detalhes...")
            is DetailUiState.Error -> ErrorState(state.message, onRetry, Modifier.align(Alignment.Center))
            is DetailUiState.Success -> DetailContent(state.movie, isFavorite, onToggleFavorite)
        }
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(top = statusBarTop() + 12.dp, start = 18.dp).size(42.dp)
                .clip(CircleShape).background(Color(0xBB0C1019))
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
        }
    }
}

@Composable
private fun DetailContent(movie: Movie, isFavorite: Boolean, onToggleFavorite: (Movie) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(bottom = 36.dp)) {
        Box(Modifier.fillMaxWidth().height(410.dp)) {
            AsyncImage(
                model = movie.banner.ifBlank { movie.image },
                contentDescription = "Imagem de ${movie.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0x3310141F), Ink))))
            Row(
                Modifier.align(Alignment.TopEnd).padding(top = statusBarTop() + 18.dp, end = 18.dp)
                    .clip(CircleShape).background(Color(0xBB0C1019)).padding(horizontal = 9.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(7.dp).clip(CircleShape).background(Positive))
                Spacer(Modifier.width(6.dp))
                Text("Dados Ghibli API", fontSize = 10.sp, color = Color(0xFFD2D6DF))
            }
        }

        Column(Modifier.offset(y = (-45).dp).padding(horizontal = 20.dp)) {
            Text(movie.director.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.7.sp, color = AccentSoft)
            Spacer(Modifier.height(7.dp))
            Text(movie.title, fontSize = 31.sp, lineHeight = 33.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            if (movie.originalTitle.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(movie.originalTitle, fontSize = 13.sp, color = Muted)
            }
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, null, tint = StarColor, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(movie.rating, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StarColor)
                Text("  ·  ${movie.year}", fontSize = 12.sp, color = Color(0xFFC0C6D1))
                if (movie.duration.isNotEmpty()) Text("  ·  ${movie.duration}", fontSize = 12.sp, color = Color(0xFFC0C6D1))
            }
            Spacer(Modifier.height(15.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoChip(movie.director)
                if (movie.producer.isNotBlank() && movie.producer != movie.director) InfoChip("Prod.: ${movie.producer}")
            }
            Spacer(Modifier.height(18.dp))
            PrimaryButton(
                text = if (isFavorite) "Remover dos favoritos" else "Adicionar aos favoritos",
                onClick = { onToggleFavorite(movie) },
                secondary = isFavorite,
                icon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(25.dp))
            Text("Sinopse", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(8.dp))
            Text(movie.description.ifBlank { "Sinopse indisponível." }, fontSize = 13.sp, lineHeight = 21.sp, color = Color(0xFFBAC1CE))
            Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetaTile("Diretor", movie.director, Modifier.weight(1f))
                MetaTile("Lançamento", movie.year, Modifier.weight(1f))
            }
            Spacer(Modifier.height(18.dp))
            Text(
                "Dados da Studio Ghibli API · ghibliapi.vercel.app",
                fontSize = 10.sp, color = Color(0xFF6F788A), modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun MetaTile(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(12.dp)).background(SurfaceDark).padding(14.dp)) {
        Text(label, fontSize = 10.sp, color = Muted)
        Spacer(Modifier.height(5.dp))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}
