package com.example.ingsw_24_25_dietiestates25

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState.ListingFormState
import com.example.ingsw_24_25_dietiestates25.validation.ListingValidation
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*


class ListingValidationTest {

    private lateinit var validator: ListingValidation

    @Before
    fun setup() {
        validator = ListingValidation()
    }

    private fun validForm() = ListingFormState(
        title = "Appartamento",
        type = Type.SELL,
        price = "150000",
        city = "Milano"
    )

    // CASO VALIDO
    @Test
    fun testListingValido() {
        assertTrue(validator.validateListing(validForm()))
    }

    // CLASSI DI EQUIVALENZA NON VALIDE

    @Test
    fun testTitoloVuoto() {
        val form = validForm().copy(title = "")
        assertFalse(validator.validateListing(form))
    }

    @Test
    fun testPrezzoNonNumerico() {
        val form = validForm().copy(price = "abc")
        assertFalse(validator.validateListing(form))
    }

    @Test
    fun testPrezzoNegativo() {
        val form = validForm().copy(price = "-10")
        assertFalse(validator.validateListing(form))
    }

    @Test
    fun testCittaVuota() {
        val form = validForm().copy(city = "")
        assertFalse(validator.validateListing(form))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testFormNull() {
        validator.validateListing(null)
    }
}