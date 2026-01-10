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


    @Test
    fun validAgencyRequestTest() {
        assertTrue(
            validator.validateAgencyRequest(
                "Best Houses",
                "agency@test.com",
                "Password1@",
                "Password1@"
            )
        )
    }


    @Test(expected = IllegalArgumentException::class)
    fun nullAgencyNameTest() {
        validator.validateAgencyRequest(
            null,
            "agency@test.com",
            "Password1@",
            "Password1@"
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun nullEmailTest() {
        validator.validateAgencyRequest(
            "Best Houses",
            null,
            "Password1@",
            "Password1@"
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun nullPasswordtest() {
        validator.validateAgencyRequest(
            "Best Houses",
            "agency@test.com",
            null,
            "Password1@"
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun nullConfirmPasswordTest() {
        validator.validateAgencyRequest(
            "Best Houses",
            "agency@test.com",
            "Password1@",
            null
        )
    }


    @Test
    fun emptyAgencyNameTest() {
        assertFalse(
            validator.validateAgencyRequest(
                "",
                "agency@test.com",
                "Password1@",
                "Password1@"
            )
        )
    }

    @Test
    fun blankAgencyNameTest() {
        assertFalse(
            validator.validateAgencyRequest(
                "   ",
                "agency@test.com",
                "Password1@",
                "Password1@"
            )
        )
    }

    @Test
    fun emptyEmailTest() {
        assertFalse(
            validator.validateAgencyRequest(
                "BEst Houses",
                "",
                "Password1@",
                "Password1@"
            )
        )
    }

    @Test
    fun emailWithoutAtSymbolTest() {
        assertFalse(
            validator.validateAgencyRequest(
                "Best Houses",
                "agencytest.com",
                "Password1@",
                "Password1@"
            )
        )
    }

    @Test
    fun emailWithoutDomainTest() {
        assertFalse(
            validator.validateAgencyRequest(
                "Best Houses",
                "agency@",
                "Password1@",
                "Password1@"
            )
        )
    }

    @Test
    fun emailWithInvalidCharactersTest() {
        assertFalse(
            validator.validateAgencyRequest(
                "Best Houses",
                "agency@@test.com",
                "Password1@",
                "Password1@"
            )
        )
    }

    @Test
    fun passwordTooShortTest() {
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
    fun passwordWithoutDigitTest() {
        assertFalse(
            validator.validateAgencyRequest(
                "Best Houses",
                "agency@test.com",
                "Password@",
                "Password@"
            )
        )
    }

    @Test
    fun passwordWithoutUppercaseTest() {
        assertFalse(
            validator.validateAgencyRequest(
                "Best Houses",
                "agency@test.com",
                "password1@",
                "password1@"
            )
        )
    }

    @Test
    fun passwordWithoutSpecialCharacterTest() {
        assertFalse(
            validator.validateAgencyRequest(
                "Best Houses",
                "agency@test.com",
                "Password12",
                "Password12"
            )
        )
    }


    @Test
    fun passwordsDoNotMatchTest() {
        assertFalse(
            validator.validateAgencyRequest(
                "Best Houses",
                "agency@test.com",
                "Password1@",
                "Password2@"
            )
        )
    }
}