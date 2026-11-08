package arthur.example.projeto_final.data

/** Regras mínimas do cadastro: e-mail válido e senha com 6 ou mais caracteres. */
object Validation {

    private val EMAIL = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun isEmailValid(email: String): Boolean = EMAIL.matches(email.trim())

    fun isPasswordValid(password: String): Boolean = password.length >= 6
}
