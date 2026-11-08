package arthur.example.projeto_final.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {

    @Query("SELECT * FROM favoritos WHERE usuarioId = :usuarioId ORDER BY savedAt DESC")
    fun observar(usuarioId: Long): Flow<List<Favorito>>

    @Query("SELECT * FROM favoritos WHERE usuarioId = :usuarioId AND movieId = :movieId LIMIT 1")
    suspend fun buscar(usuarioId: Long, movieId: String): Favorito?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(favorito: Favorito)

    @Query("DELETE FROM favoritos WHERE usuarioId = :usuarioId AND movieId = :movieId")
    suspend fun remover(usuarioId: Long, movieId: String)
}
