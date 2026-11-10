package arthur.example.projeto_final.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import arthur.example.projeto_final.data.AppContainer

object AppViewModelProvider {

    val Factory = viewModelFactory {
        initializer { AuthViewModel(container().authRepository) }
        initializer { MoviesViewModel(container().movieRepository) }
        initializer { DetailViewModel(container().movieRepository) }
    }

    private fun CreationExtras.container(): AppContainer =
        AppContainer.get(this[APPLICATION_KEY] as Application)
}
