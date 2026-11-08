package arthur.example.projeto_final.data

/** Hash SHA-256 em hexadecimal (é o que vai para a coluna senhaHash). */
fun String.sha256(): String =
    java.security.MessageDigest.getInstance("SHA-256")
        .digest(toByteArray()).joinToString("") { "%02x".format(it) }
