package arthur.example.projeto_final.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import arthur.example.projeto_final.userinterface.theme.Accent
import arthur.example.projeto_final.userinterface.theme.Ink
import arthur.example.projeto_final.userinterface.theme.Muted
import arthur.example.projeto_final.userinterface.theme.StarColor
import arthur.example.projeto_final.userinterface.theme.SurfaceDark
import arthur.example.projeto_final.viewmodel.MoviesUiState
import java.util.Calendar

@Composable
fun HomeScreen(
    userName: String,
    state: MoviesUiState,
    favoriteIds: Set<String>,
    onRetry: () -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
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
            Column {
                HomeHeader(userName, onProfileClick)
                Spacer(Modifier.height(22.dp))
                SearchBox(onSearchClick)
            }
        }

        when (state) {
            is MoviesUiState.Loading -> item(span = { GridItemSpan(maxLineSpan) }) { LoadingState() }
            is MoviesUiState.Error -> item(span = { GridItemSpan(maxLineSpan) }) { ErrorState(state.message, onRetry) }
            is MoviesUiState.Success -> {
                val movies = state.movies
                val featured = movies.maxByOrNull { it.rtScore }
                if (featured != null) {
                    item(span = { GridItemSpan(maxLineSpan) }) { FeaturedCard(featured) { onMovieClick(featured) } }
                }
                val topRated = movies.filter { it.id != featured?.id }.sortedByDescending { it.rtScore }.take(6)
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        Text("Mais bem avaliados", style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Spacer(Modifier.height(14.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(13.dp)) {
                            items(topRated, key = { it.id }) { movie ->
                                MovieCard(
                                    movie = movie,
                                    isFavorite = movie.id in favoriteIds,
                                    onFavoriteClick = { onToggleFavorite(movie) },
                                    onClick = { onMovieClick(movie) },
                                    modifier = Modifier.width(142.dp),
                                    posterHeight = 210.dp
                                )
                            }
                        }
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text("Todos os filmes", style = MaterialTheme.typography.titleLarge, color = Color.White)
                }
                items(movies, key = { it.id }) { movie ->
                    MovieCard(
                        movie = movie,
                        isFavorite = movie.id in favoriteIds,
                        onFavoriteClick = { onToggleFavorite(movie) },
                        onClick = { onMovieClick(movie) }
                    )
                }
            }
        }
    }
}

private fun greeting(): String = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
    in 5..11 -> "Bom dia,"
    in 12..17 -> "Boa tarde,"
    else -> "Boa noite,"
}

@Composable
private fun HomeHeader(userName: String, onProfileClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(greeting(), fontSize = 14.sp, color = Muted)
            Text(
                "Olá, ${userName.substringBefore(' ').ifBlank { "cinéfilo" }}!",
                style = MaterialTheme.typography.headlineMedium, color = Color.White
            )
        }
        InitialsAvatar(userName, size = 44, onClick = onProfileClick)
    }
}

@Composable
fun InitialsAvatar(name: String, size: Int, onClick: (() -> Unit)? = null) {
    val initials = name.trim().split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }.ifBlank { "?" }
    Box(
        Modifier.size(size.dp).clip(CircleShape)
            .background(Brush.linearGradient(listOf(Color(0xFFD51E2A), Color(0xFF771018))))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = (size * 0.38f).sp)
    }
}

@Composable
private fun SearchBox(onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(50.dp).clip(RoundedCornerShape(14.dp)).background(SurfaceDark)
            .clickable(onClick = onClick).padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Search, contentDescription = null, tint = Muted)
        Spacer(Modifier.width(11.dp))
        Text("Buscar filmes...", color = Muted, fontSize = 14.sp)
    }
}

@Composable
private fun FeaturedCard(movie: Movie, onClick: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(310.dp).clip(RoundedCornerShape(22.dp)).background(SurfaceDark)) {
        AsyncImage(
            model = movie.banner.ifBlank { movie.image },
            contentDescription = "Imagem de ${movie.title}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Ink), startY = 120f)))
        Column(Modifier.align(Alignment.BottomStart).padding(18.dp)) {
            Text(
                "DESTAQUE",
                Modifier.clip(RoundedCornerShape(7.dp)).background(Accent).padding(horizontal = 9.dp, vertical = 6.dp),
                color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.8.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(movie.title, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Spacer(Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, null, tint = StarColor, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(5.dp))
                Text("${movie.rating} · ${movie.year} · ${movie.director}", fontSize = 13.sp, color = Color(0xFFD6D9E1))
            }
            Spacer(Modifier.height(14.dp))
            PrimaryButton("Ver detalhes", onClick = onClick)
        }
    }
}
