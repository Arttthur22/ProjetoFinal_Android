package arthur.example.projeto_final.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arthur.example.projeto_final.data.Movie
import arthur.example.projeto_final.userinterface.theme.Accent
import arthur.example.projeto_final.userinterface.theme.Ink
import arthur.example.projeto_final.userinterface.theme.Muted
import arthur.example.projeto_final.userinterface.theme.OutlineColor
import arthur.example.projeto_final.userinterface.theme.SurfaceDark
import arthur.example.projeto_final.viewmodel.ALL_DIRECTORS
import arthur.example.projeto_final.viewmodel.MoviesUiState

@Composable
fun SearchScreen(
    state: MoviesUiState,
    query: String,
    director: String,
    directors: List<String>,
    results: List<Movie>,
    favoriteIds: Set<String>,
    onQueryChange: (String) -> Unit,
    onDirectorChange: (String) -> Unit,
    onRetry: () -> Unit,
    onMovieClick: (Movie) -> Unit,
    onToggleFavorite: (Movie) -> Unit
) {
    val hasFilter = query.isNotBlank() || director != ALL_DIRECTORS

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().background(Ink),
        contentPadding = gridPadding(),
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            androidx.compose.foundation.layout.Column {
                PageHeader("DESCUBRA", "Buscar filmes")
                Spacer(Modifier.height(22.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar filmes...", color = Muted) },
                    leadingIcon = { Icon(Icons.Outlined.Search, null, tint = Muted) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(Icons.Outlined.Close, contentDescription = "Limpar busca", tint = Muted)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Search),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = OutlineColor,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Accent
                    )
                )
                Spacer(Modifier.height(20.dp))
                Text("Filtrar por diretor", fontSize = 12.sp, color = Muted)
                Spacer(Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(directors, key = { it }) { d ->
                        FilterChip(d, selected = d == director, onClick = { onDirectorChange(d) })
                    }
                }
            }
        }

        when {
            state is MoviesUiState.Loading ->
                item(span = { GridItemSpan(maxLineSpan) }) { LoadingState() }

            state is MoviesUiState.Error ->
                item(span = { GridItemSpan(maxLineSpan) }) { ErrorState(state.message, onRetry) }

            !hasFilter ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    StateMessage(
                        icon = Icons.Outlined.Search,
                        title = "Sua próxima história começa aqui",
                        text = "Pesquise por título ou explore os diretores acima."
                    )
                }

            results.isEmpty() ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    StateMessage(
                        icon = Icons.Outlined.Movie,
                        title = "Nenhum filme encontrado",
                        text = "Tente buscar por outro título ou diretor."
                    )
                }

            else -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        "${results.size} ${if (results.size == 1) "resultado" else "resultados"}",
                        fontSize = 12.sp, color = Muted
                    )
                }
                items(results, key = { it.id }) { movie ->
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
