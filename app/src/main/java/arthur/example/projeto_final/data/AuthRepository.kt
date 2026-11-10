package arthur.example.projeto_final.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

sealed interface AuthResult {
    data class Success(val usuario: Usuario) : AuthResult
    data class Failure(val message: String) : AuthResult
}

class AuthRepository(
    private val usuarioDao: UsuarioDao,
    private val session: SessionManager
) {
    val sessionUserId: StateFlow<Long?> = session.userId

    suspend fun register(nome: String, email: String, senha: String, confirmacao: String): AuthResult {
        val nomeLimpo = nome.trim()
        val emailLimpo = email.trim().lowercase()

        if (nomeLimpo.isEmpty()) return AuthResult.Failure("Informe seu nome completo.")
        if (!Validation.isEmailValid(emailLimpo)) return AuthResult.Failure("Informe um e-mail válido para continuar.")
        if (!Validation.isPasswordValid(senha)) return AuthResult.Failure("A senha precisa ter pelo menos 6 caracteres.")
        if (senha != confirmacao) return AuthResult.Failure("As senhas não conferem.")

        return withContext(Dispatchers.IO) {
            if (usuarioDao.buscarPorEmail(emailLimpo) != null) {
                AuthResult.Failure("Este e-mail já está cadastrado.")
            } else {
                val novo = Usuario(nome = nomeLimpo, email = emailLimpo, senhaHash = senha.sha256())
                val id = usuarioDao.inserir(novo)
                AuthResult.Success(novo.copy(id = id))
            }
        }
    }

    suspend fun login(email: String, senha: String): AuthResult {
        val emailLimpo = email.trim().lowercase()
        if (emailLimpo.isEmpty() || senha.isEmpty()) return AuthResult.Failure("Informe seu e-mail e sua senha.")

        return withContext(Dispatchers.IO) {
            val usuario = usuarioDao.buscarPorEmail(emailLimpo)
            if (usuario == null || usuario.senhaHash != senha.sha256()) {
                AuthResult.Failure("E-mail ou senha incorretos.")
            } else {
                session.save(usuario.id)
                AuthResult.Success(usuario)
            }
        }
    }

    fun logout() = session.clear()

    suspend fun getUsuario(id: Long): Usuario? = withContext(Dispatchers.IO) { usuarioDao.buscarPorId(id) }
}
