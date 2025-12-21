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

    // Caso valido (SELL)
    @Test
    fun testSearchValidaBuy() {
        assertTrue(validator.validateSearch("SELL", "Rome"))
    }

    // TYPE
    @Test
    fun testTypeVuoto() {
        assertFalse(validator.validateSearch("", "Rome"))
    }

    @Test
    fun testTypeNonAmmesso() {
        assertFalse(validator.validateSearch("BUY", "Rome"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testTypeNull() {
        validator.validateSearch(null, "Rome")
    }

    // LOCATION
    @Test
    fun testLocationVuota() {
        assertFalse(validator.validateSearch("SELL", ""))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testLocationNull() {
        validator.validateSearch("SELL", null)
    }
}