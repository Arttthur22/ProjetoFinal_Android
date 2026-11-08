package arthur.example.projeto_final.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arthur.example.projeto_final.data.AuthRepository
import arthur.example.projeto_final.data.AuthResult
import arthur.example.projeto_final.data.Usuario
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    /** true logo depois de um cadastro bem-sucedido; a tela navega para o login e consome o evento. */
    val registered: Boolean = false
)

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    /** Id do usuário logado (null = deslogado). Lido do SharedPreferences, então sobrevive ao fechar o app. */
    val userId: StateFlow<Long?> = repository.sessionUserId

    @OptIn(ExperimentalCoroutinesApi::class)
    val usuario: StateFlow<Usuario?> = userId
        .flatMapLatest { id -> flow { emit(id?.let { repository.getUsuario(it) }) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(email: String, senha: String) {
        if (_state.value.loading) return
        viewModelScope.launch {
            _state.update { AuthUiState(loading = true) }
            when (val result = repository.login(email, senha)) {
                // Em caso de sucesso o userId muda e a navegação reage a isso.
                is AuthResult.Success -> _state.update { AuthUiState() }
                is AuthResult.Failure -> _state.update { AuthUiState(error = result.message) }
            }
        }
    }

    fun register(nome: String, email: String, senha: String, confirmacao: String) {
        if (_state.value.loading) return
        viewModelScope.launch {
            _state.update { AuthUiState(loading = true) }
            when (val result = repository.register(nome, email, senha, confirmacao)) {
                is AuthResult.Success -> _state.update { AuthUiState(registered = true) }
                is AuthResult.Failure -> _state.update { AuthUiState(error = result.message) }
            }
        }
    }

    fun logout() = repository.logout()

    /** Limpa erros e eventos pendentes (ao trocar entre login e cadastro). */
    fun clearState() = _state.update { AuthUiState() }
}
