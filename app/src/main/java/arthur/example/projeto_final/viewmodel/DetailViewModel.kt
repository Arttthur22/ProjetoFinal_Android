package arthur.example.projeto_final.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arthur.example.projeto_final.data.Movie
import arthur.example.projeto_final.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val movie: Movie) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

class DetailViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _state = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    private var loadedId: String? = null

    /** Recebe o id que chegou pela rota type-safe e carrega o filme certo. */
    fun load(movieId: String, force: Boolean = false) {
        if (!force && loadedId == movieId && _state.value is DetailUiState.Success) return
        loadedId = movieId
        viewModelScope.launch {
            _state.value = DetailUiState.Loading
            repository.getMovie(movieId).fold(
                onSuccess = { _state.value = DetailUiState.Success(it) },
                onFailure = { _state.value = DetailUiState.Error(it.message ?: "Erro desconhecido.") }
            )
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch { repository.toggleFavorite(movie) }
    }
}
