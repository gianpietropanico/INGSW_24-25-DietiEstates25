package com.example.ingsw_24_25_dietiestates25

import com.example.ingsw_24_25_dietiestates25.validation.ResetPasswordValidation
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ResetPasswordValidationTest {

    private lateinit var validator: ResetPasswordValidation

    @Before
    fun setup() {
        validator = ResetPasswordValidation()
    }

    // Caso valido
    @Test
    fun testCambioPasswordValido() {
        assertTrue(
            validator.validateResetPassword(
                oldPassword = "OldPass1@",
                newPassword = "NewPass1@"
            )
        )
    }

    // OLD PASSWORD
    @Test
    fun testOldPasswordVuota() {
        assertFalse(
            validator.validateResetPassword(
                oldPassword = "",
                newPassword = "NewPass1@"
            )
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testOldPasswordNull() {
        validator.validateResetPassword(
            oldPassword = null,
            newPassword = "NewPass1@"
        )
    }

    // NEW PASSWORD
    @Test
    fun testNewPasswordVuota() {
        assertFalse(
            validator.validateResetPassword(
                oldPassword = "OldPass1@",
                newPassword = ""
            )
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNewPasswordNull() {
        validator.validateResetPassword(
            oldPassword = "OldPass1@",
            newPassword = null
        )
    }

    @Test
    fun testNewPasswordTroppoCorta() {
        assertFalse(
            validator.validateResetPassword(
                oldPassword = "OldPass1@",
                newPassword = "New1@"
            )
        )
    }

    @Test
    fun testNewPasswordSenzaNumeri() {
        assertFalse(
            validator.validateResetPassword(
                oldPassword = "OldPass1@",
                newPassword = "NewPass@@"
            )
        )
    }

    @Test
    fun testNewPasswordSenzaMaiuscole() {
        assertFalse(
            validator.validateResetPassword(
                oldPassword = "OldPass1@",
                newPassword = "newpass1@"
            )
        )
    }

    @Test
    fun testNewPasswordSenzaSpeciali() {
        assertFalse(
            validator.validateResetPassword(
                oldPassword = "OldPass1@",
                newPassword = "NewPass12"
            )
        )
    }

    @Test
    fun testNewPasswordUgualeOldPassword() {
        assertFalse(
            validator.validateResetPassword(
                oldPassword = "SamePass1@",
                newPassword = "SamePass1@"
            )
        )
    }
}