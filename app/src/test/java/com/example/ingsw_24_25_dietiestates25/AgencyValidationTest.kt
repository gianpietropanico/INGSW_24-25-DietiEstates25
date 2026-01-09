package com.example.ingsw_24_25_dietiestates25

import com.example.ingsw_24_25_dietiestates25.validation.AgencyRequestValidation
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AgencyRequestValidationTest {

    private lateinit var validator: AgencyRequestValidation

    @Before
    fun setup() {
        validator = AgencyRequestValidation()
    }

    // === CASO VALIDO ===
    @Test
    fun testRichiestaAgenziaValida() {
        assertTrue(
            validator.validateAgencyRequest(
                "Best Houses",
                "agency@test.com",
                "Password1@",
                "Password1@"
            )
        )
    }

    // === AGENCY NAME ===
    @Test
    fun testAgencyNameVuoto() {
        assertFalse(
            validator.validateAgencyRequest(
                "",
                "agency@test.com",
                "Password1@",
                "Password1@"
            )
        )
    }

    // === EMAIL ===
    @Test
    fun testEmailFormatoNonValido() {
        assertFalse(
            validator.validateAgencyRequest(
                "Best Houses",
                "agencytest.com",
                "Password1@",
                "Password1@"
            )
        )
    }

    // === PASSWORD ===
    @Test
    fun testPasswordBreve() {
        assertFalse(
            validator.validateAgencyRequest(
                "Best Houses",
                "agency@test.com",
                "Pass1@",
                "Pass1@"
            )
        )
    }

    @Test
    fun testPasswordNonCoincidenti() {
        assertFalse(
            validator.validateAgencyRequest(
                "Best Houses",
                "agency@test.com",
                "Password1@",
                "Password2@"
            )
        )
    }

    // === NULL ===
    @Test(expected = IllegalArgumentException::class)
    fun testParametriNull() {
        validator.validateAgencyRequest(
            null,
            "agency@test.com",
            "Password1@",
            "Password1@"
        )
    }
}