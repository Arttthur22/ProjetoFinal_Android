package arthur.example.projeto_final.data

import java.util.Locale

/** Modelo usado pelo app inteiro (a UI nunca enxerga o DTO do Retrofit nem a Entity do Room). */
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
    /** Nota de 0 a 10 no formato brasileiro, ex.: "9,5". */
    val rating: String
        get() = String.format(Locale.forLanguageTag("pt-BR"), "%.1f", rtScore / 10.0)

    /** Duração no formato "2h 05min". */
    val duration: String
        get() = if (runningTime <= 0) "" else "${runningTime / 60}h ${(runningTime % 60).toString().padStart(2, '0')}min"
}
