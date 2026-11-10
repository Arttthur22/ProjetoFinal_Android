package arthur.example.projeto_final.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arthur.example.projeto_final.data.Movie
import arthur.example.projeto_final.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface MoviesUiState {
    data object Loading : MoviesUiState
    data class Success(val movies: List<Movie>) : MoviesUiState
    data class Error(val message: String) : MoviesUiState
}

const val ALL_DIRECTORS = "Todos"

class MoviesViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _state = MutableStateFlow<MoviesUiState>(MoviesUiState.Loading)
    val state: StateFlow<MoviesUiState> = _state.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _director = MutableStateFlow(ALL_DIRECTORS)
    val director: StateFlow<String> = _director.asStateFlow()

    val favorites: StateFlow<List<Movie>> = repository.observeFavorites()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val favoriteIds: StateFlow<Set<String>> = favorites
        .map { lista -> lista.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    val directors: StateFlow<List<String>> = _state
        .map { s ->
            if (s is MoviesUiState.Success) listOf(ALL_DIRECTORS) + s.movies.map { it.director }.distinct()
            else listOf(ALL_DIRECTORS)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf(ALL_DIRECTORS))

    val searchResults: StateFlow<List<Movie>> = combine(_state, _query, _director) { s, q, d ->
        val movies = (s as? MoviesUiState.Success)?.movies.orEmpty()
        movies.filter { m ->
            (q.isBlank() || m.title.contains(q.trim(), ignoreCase = true) ||
                m.originalTitle.contains(q.trim(), ignoreCase = true)) &&
                (d == ALL_DIRECTORS || m.director == d)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = MoviesUiState.Loading
            repository.getMovies(forceRefresh = true).fold(
                onSuccess = { _state.value = MoviesUiState.Success(it) },
                onFailure = { _state.value = MoviesUiState.Error(it.message ?: "Erro desconhecido.") }
            )
        }
    }

    fun onQueryChange(value: String) { _query.value = value }

    fun onDirectorChange(value: String) { _director.value = value }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch { repository.toggleFavorite(movie) }
    }
}
