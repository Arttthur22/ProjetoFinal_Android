package arthur.example.projeto_final.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Resposta crua da Studio Ghibli API (https://ghibliapi.vercel.app/films). */
@Serializable
data class FilmDto(
    val id: String,
    val title: String,
    @SerialName("original_title") val originalTitle: String = "",
    @SerialName("original_title_romanised") val originalTitleRomanised: String = "",
    val image: String = "",
    @SerialName("movie_banner") val movieBanner: String = "",
    val description: String = "",
    val director: String = "",
    val producer: String = "",
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("running_time") val runningTime: String = "",
    @SerialName("rt_score") val rtScore: String = ""
)
