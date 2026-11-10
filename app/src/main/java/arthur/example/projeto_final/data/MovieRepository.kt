package arthur.example.projeto_final.data

import arthur.example.projeto_final.network.GhibliApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

class MovieRepository(
    private val api: GhibliApi,
    private val favoritoDao: FavoritoDao,
    private val session: SessionManager
) {
    private var cache: List<Movie> = emptyList()

    suspend fun getMovies(forceRefresh: Boolean = false): Result<List<Movie>> {
        if (!forceRefresh && cache.isNotEmpty()) return Result.success(cache)
        return safeCall { api.getFilms().map { it.toMovie() } }.onSuccess { cache = it }
    }

    suspend fun getMovie(id: String): Result<Movie> {
        cache.firstOrNull { it.id == id }?.let { return Result.success(it) }
        val uid = session.userId.value
        if (uid != null) {
            val salvo = withContext(Dispatchers.IO) { favoritoDao.buscar(uid, id) }
            if (salvo != null) return Result.success(salvo.toMovie())
        }
        return safeCall { api.getFilm(id).toMovie() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeFavorites(): Flow<List<Movie>> =
        session.userId.flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList())
            else favoritoDao.observar(uid).map { lista -> lista.map { it.toMovie() } }
        }

    suspend fun toggleFavorite(movie: Movie) {
        val uid = session.userId.value ?: return
        withContext(Dispatchers.IO) {
            if (favoritoDao.buscar(uid, movie.id) == null) favoritoDao.inserir(movie.toFavorito(uid))
            else favoritoDao.remover(uid, movie.id)
        }
    }

    private suspend fun <T> safeCall(block: suspend () -> T): Result<T> = withContext(Dispatchers.IO) {
        try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Result.failure(Exception("Sem conexão com a internet. Verifique sua rede e tente novamente."))
        } catch (e: Exception) {
            Result.failure(Exception("Não foi possível carregar os filmes agora. Tente novamente."))
        }
    }
}
