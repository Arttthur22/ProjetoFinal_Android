package arthur.example.projeto_final

import arthur.example.projeto_final.data.Validation
import arthur.example.projeto_final.data.sha256
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationTest {

    @Test fun emailValido() {
        assertTrue(Validation.isEmailValid("marina.costa@email.com"))
        assertTrue(Validation.isEmailValid("  voce@email.com.br "))
    }

    @Test fun emailInvalido() {
        assertFalse(Validation.isEmailValid("sem-arroba"))
        assertFalse(Validation.isEmailValid("a@b"))
        assertFalse(Validation.isEmailValid(""))
    }

    @Test fun senhaComSeisOuMaisCaracteres() {
        assertTrue(Validation.isPasswordValid("123456"))
        assertFalse(Validation.isPasswordValid("12345"))
    }

    @Test fun hashNaoEhTextoPuro() {
        val hash = "minhasenha".sha256()
        assertEquals(64, hash.length)
        assertNotEquals("minhasenha", hash)
        assertEquals(hash, "minhasenha".sha256())
        assertEquals("2bb80d537b1da3e38bd30361aa855686bde0eacd7162fef6a25fe97bf527a25b", "secret".sha256())
    }
}
