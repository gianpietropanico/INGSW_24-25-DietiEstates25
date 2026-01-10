package com.example.ingsw_24_25_dietiestates25

import com.example.ingsw_24_25_dietiestates25.validation.SearchValidation
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SearchValidationTest {

    private lateinit var validator: SearchValidation

    @Before
    fun setup() {
        validator = SearchValidation()
    }

    @Test
    fun searchValidTest() {
        assertTrue(validator.validateSearch("SELL", "Rome"))
    }

    @Test
    fun blankTypeTest() {
        assertFalse(validator.validateSearch("", "Rome"))
    }

    @Test
    fun typeNotValidTest() {
        assertFalse(validator.validateSearch("BUY", "Rome"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun nullTypeTest() {
        validator.validateSearch(null, "Rome")
    }

    @Test
    fun emptyLocationTest() {
        assertFalse(validator.validateSearch("SELL", ""))
    }

    @Test(expected = IllegalArgumentException::class)
    fun nullLocationTest() {
        validator.validateSearch("SELL", null)
    }
}












