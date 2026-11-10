package arthur.example.projeto_final.data

import java.util.Locale

data class Movie(
    val id: String,
    val title: String,
    val originalTitle: String,
    val image: String,
    val banner: String,
    val description: String,
    val director: String,
    val producer: String,
    val year: String,
    val runningTime: Int,
    val rtScore: Int
) {
    val rating: String
        get() = String.format(Locale.forLanguageTag("pt-BR"), "%.1f", rtScore / 10.0)

    val duration: String
        get() = if (runningTime <= 0) "" else "${runningTime / 60}h ${(runningTime % 60).toString().padStart(2, '0')}min"
}
