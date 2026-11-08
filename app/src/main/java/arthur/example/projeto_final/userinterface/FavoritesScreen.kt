package arthur.example.projeto_final.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arthur.example.projeto_final.data.Movie
import arthur.example.projeto_final.userinterface.theme.Ink
import arthur.example.projeto_final.userinterface.theme.Muted
import arthur.example.projeto_final.userinterface.theme.Positive

@Composable
fun FavoritesScreen(
    favorites: List<Movie>,
    onExplore: () -> Unit,
    onMovieClick: (Movie) -> Unit,
    onToggleFavorite: (Movie) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().background(Ink),
        contentPadding = gridPadding(),
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            PageHeader(
                eyebrow = "SUA COLEÇÃO",
                title = "Meus favoritos",
                subtitle = "${favorites.size} ${if (favorites.size == 1) "filme salvo" else "filmes salvos"} neste dispositivo"
            )
        }
        if (favorites.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                StateMessage(
                    icon = Icons.Outlined.FavoriteBorder,
                    title = "Sua lista está vazia",
                    text = "Salve os filmes que você quer assistir e encontre todos por aqui.",
                    actionLabel = "Explorar filmes",
                    onAction = onExplore
                )
            }
        } else {
            items(favorites, key = { it.id }) { movie ->
                MovieCard(
                    movie = movie,
                    isFavorite = true,
                    onFavoriteClick = { onToggleFavorite(movie) },
                    onClick = { onMovieClick(movie) }
                )
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFF151B27)).padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Wifi, contentDescription = null, tint = Positive)
                Spacer(Modifier.width(13.dp))
                Column {
                    Text("Disponível offline", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Seus favoritos ficam salvos neste dispositivo.", fontSize = 11.sp, color = Muted)
                }
            }
        }
    }
}
