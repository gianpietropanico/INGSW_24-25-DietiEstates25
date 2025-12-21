package com.example.ingsw_24_25_dietiestates25

import com.example.ingsw_24_25_dietiestates25.validation.SignUpValidation
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class SignUpValidationTest {

    private lateinit var validator: SignUpValidation

    @Before
    fun setup() {
        validator = SignUpValidation()
    }

    // Caso valido
    @Test
    fun testRegistrazioneValida() {
        assertTrue(validator.validateSignUp(
            "test@gmail.com",
            "Prova123@",
            "Prova123@"
        ))
    }

    // EMAIL
    @Test fun testEmailVuota() =
        assertFalse(validator.validateSignUp("", "Prova123@", "Prova123@"))


    @Test(expected = IllegalArgumentException::class)
    fun testEmailNull() {
        validator.validateSignUp(null, "Prova123@", "Prova123@")
    }

    @Test fun testEmailFormatoNonValido() =
        assertFalse(validator.validateSignUp("testgmail.com", "Prova123@", "Prova123@"))

    // PASSWORD
    @Test fun testPasswordVuota() =
        assertFalse(validator.validateSignUp("test@gmail.com", "", "Prova123@"))

    @Test(expected = IllegalArgumentException::class)
    fun testPasswordNull() {
        validator.validateSignUp("test@gmail.com", null, "Prova123@")
    }
    @Test fun testPasswordBreve() =
        assertFalse(validator.validateSignUp("test@gmail.com", "Pro1@", "Pro1@"))

    @Test fun testPasswordSenzaNumeri() =
        assertFalse(validator.validateSignUp("test@gmail.com", "ProvaTest@", "ProvaTest@"))

    @Test fun testPasswordSenzaMaiuscole() =
        assertFalse(validator.validateSignUp("test@gmail.com", "prova123@", "prova123@"))

    @Test fun testPasswordSenzaSpeciali() =
        assertFalse(validator.validateSignUp("test@gmail.com", "Prova1234", "Prova1234"))

    // CONFIRM PASSWORD
    @Test fun testConfirmPasswordNonCoincidente() =
        assertFalse(validator.validateSignUp("test@gmail.com", "Prova123@", "Prova1234@"))

    @Test fun testConfirmPasswordVuota() =
        assertFalse(validator.validateSignUp("test@gmail.com", "Prova123@", ""))

    @Test(expected = IllegalArgumentException::class)
    fun testConfirmPasswordNull() {
        validator.validateSignUp("test@gmail.com", "Prova123@", null)
    }
}
