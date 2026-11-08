package arthur.example.projeto_final.data

import arthur.example.projeto_final.network.FilmDto

fun FilmDto.toMovie() = Movie(
    id = id,
    title = title,
    originalTitle = originalTitle.ifBlank { originalTitleRomanised },
    image = image,
    banner = movieBanner,
    description = description.trim(),
    director = director,
    producer = producer,
    year = releaseDate,
    runningTime = runningTime.toIntOrNull() ?: 0,
    rtScore = rtScore.toIntOrNull() ?: 0
)

fun Favorito.toMovie() = Movie(
    id = movieId,
    title = title,
    originalTitle = originalTitle,
    image = image,
    banner = banner,
    description = description,
    director = director,
    producer = producer,
    year = year,
    runningTime = runningTime,
    rtScore = rtScore
)

fun Movie.toFavorito(usuarioId: Long) = Favorito(
    usuarioId = usuarioId,
    movieId = id,
    title = title,
    originalTitle = originalTitle,
    image = image,
    banner = banner,
    description = description,
    director = director,
    producer = producer,
    year = year,
    runningTime = runningTime,
    rtScore = rtScore,
    savedAt = System.currentTimeMillis()
)
