package arthur.example.projeto_final.data

fun String.sha256(): String =
    java.security.MessageDigest.getInstance("SHA-256")
        .digest(toByteArray()).joinToString("") { "%02x".format(it) }
