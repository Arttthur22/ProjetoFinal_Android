package arthur.example.projeto_final.network

import retrofit2.http.GET
import retrofit2.http.Path

interface GhibliApi {

    @GET("films")
    suspend fun getFilms(): List<FilmDto>

    @GET("films/{id}")
    suspend fun getFilm(@Path("id") id: String): FilmDto
}
