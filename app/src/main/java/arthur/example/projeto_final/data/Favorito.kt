package arthur.example.projeto_final.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "favoritos",
    primaryKeys = ["usuarioId", "movieId"],
    indices = [Index("usuarioId")],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Favorito(
    val usuarioId: Long,
    val movieId: String,
    val title: String,
    val originalTitle: String,
    val image: String,
    val banner: String,
    val description: String,
    val director: String,
    val producer: String,
    val year: String,
    val runningTime: Int,
    val rtScore: Int,
    val savedAt: Long
)
