package arthur.example.projeto_final.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Usuário cadastrado neste aparelho. A senha NUNCA é guardada em texto puro: só o hash SHA-256. */
@Entity(tableName = "usuarios", indices = [Index(value = ["email"], unique = true)])
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val email: String,
    val senhaHash: String
)
