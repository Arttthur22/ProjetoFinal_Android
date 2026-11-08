package arthur.example.projeto_final.data

import android.content.Context
import arthur.example.projeto_final.network.RetrofitInstance

/** Cria (uma única vez) o banco, a sessão e os repositories. */
class AppContainer private constructor(context: Context) {

    private val database = AppDatabase.getInstance(context)
    private val session = SessionManager(context)

    val authRepository = AuthRepository(database.usuarioDao(), session)
    val movieRepository = MovieRepository(RetrofitInstance.api, database.favoritoDao(), session)

    companion object {
        @Volatile
        private var instance: AppContainer? = null

        fun get(context: Context): AppContainer =
            instance ?: synchronized(this) {
                instance ?: AppContainer(context.applicationContext).also { instance = it }
            }
    }
}
