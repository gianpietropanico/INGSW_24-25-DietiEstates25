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
    fun validSignUpTest() {
        assertTrue(
            validator.validateSignUp(
                "test@gmail.com",
                "Prova123@",
                "Prova123@"
            )
        )
    }


    @Test
    fun blankEmailTest() =
        assertFalse(validator.validateSignUp(
            "",
            "Prova123@",
            "Prova123@"))


    @Test(expected = IllegalArgumentException::class)
    fun nullEmailTest() {
        validator.validateSignUp(
            null,
            "Prova123@",
            "Prova123@"
        )
    }

    @Test
    fun invalidEmailFormatTest() =
        assertFalse(
            validator.validateSignUp(
                "testgmail.com",
                "Prova123@",
                "Prova123@"
            )
        )

    @Test
    fun emptyPasswordTest() =
        assertFalse(
            validator.validateSignUp(
                "test@gmail.com",
                "",
                "Prova123@"
            )
        )

    @Test(expected = IllegalArgumentException::class)
    fun nullPasswordTest() {
        validator.validateSignUp(
            "test@gmail.com",
            null,
            "Prova123@"
        )
    }

    @Test
    fun passwordTooShortTest() =
        assertFalse(
            validator.validateSignUp(
                "test@gmail.com",
                "Pro1@",
                "Pro1@"
            )
        )

    @Test
    fun passwordWithoutDigitTest() =
        assertFalse(
            validator.validateSignUp(
                "test@gmail.com",
                "ProvaTest@",
                "ProvaTest@"
            )
        )

    @Test
    fun passwordWithoutUppercaseTest() =
        assertFalse(
            validator.validateSignUp(
                "test@gmail.com",
                "prova123@",
                "prova123@"
            )
        )

    @Test
    fun passwordWithoutSpecialCharacterTest() =
        assertFalse(
            validator.validateSignUp(
                "test@gmail.com",
                "Prova1234",
                "Prova1234"
            )
        )


    @Test
    fun confirmPasswordDoesNotMatchTest() =
        assertFalse(
            validator.validateSignUp(
                "test@gmail.com",
                "Prova123@",
                "Prova1234@"
            )
        )

    @Test
    fun emptyConfirmPasswordTest() =
        assertFalse(
            validator.validateSignUp(
                "test@gmail.com",
                "Prova123@",
                ""
            )
        )

    @Test(expected = IllegalArgumentException::class)
    fun nullConfirmPasswordTest() {
        validator.validateSignUp(
            "test@gmail.com",
            "Prova123@",
            null
        )
    }

}
