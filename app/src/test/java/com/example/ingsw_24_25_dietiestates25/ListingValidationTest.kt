package com.example.ingsw_24_25_dietiestates25

import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.EnergyClass
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Type
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.User
import com.example.ingsw_24_25_dietiestates25.ui.listingUI.listingState.ListingFormState
import com.example.ingsw_24_25_dietiestates25.validation.ListingValidator
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Property
import com.example.ingsw_24_25_dietiestates25.data.model.dataclass.Role

class ListingValidatorTest {

    private lateinit var validator: ListingValidator

    private val dummyProperty = Property(
        city = "Milan",
        cap = "20100",
        country = "Italy",
        province = "MI",
        street = "Via Roma",
        civicNumber = "10",
        latitude = 45.0,
        longitude = 9.0,
        numberOfRooms = 2,
        numberOfBathrooms = 1,
        size = 80f,
        energyClass = EnergyClass.A,
        parking = false,
        garden = false,
        elevator = true,
        gatehouse = false,
        balcony = false,
        roof = false,
        airConditioning = true,
        heatingSystem = false,
        pois = emptyList(),
        images = emptyList(),
        description = "Nice apartment"
    )

    private val dummyAgent = User(
        id = "1",
        email = "agent@test.com",
        role = Role.AGENT_USER,
        username = TODO(),
        name = TODO(),
        surname = TODO(),
        profilePicture = TODO()
    )

    @Before
    fun setup() {
        validator = ListingValidator()
    }


    @Test
    fun validListingTest() {
        val result = validator.validateListing(
            title = "Nice apartment",
            type = Type.SELL,
            price = 150000f,
            property = dummyProperty,
            agent = dummyAgent
        )

        assertTrue(result.isValid)
    }


    @Test
    fun nullTitleTest() {
        val result = validator.validateListing(
            title = null,
            type = Type.SELL,
            price = 150000f,
            property = dummyProperty,
            agent = dummyAgent
        )

        assertFalse(result.isValid)
    }

    @Test
    fun blankTitleBlankTest() {
        val result = validator.validateListing(
            title = "",
            type = Type.SELL,
            price = 150000f,
            property = dummyProperty,
            agent = dummyAgent
        )

        assertFalse(result.isValid)
    }


    @Test
    fun nullTypeTest() {
        val result = validator.validateListing(
            title = "Apartment",
            type = null,
            price = 150000f,
            property = dummyProperty,
            agent = dummyAgent
        )

        assertFalse(result.isValid)
    }


    @Test
    fun nullPricetest() {
        val result = validator.validateListing(
            title = "Apartment",
            type = Type.SELL,
            price = null,
            property = dummyProperty,
            agent = dummyAgent
        )

        assertFalse(result.isValid)
    }

    @Test
    fun priceNotPositiveTest() {
        val result = validator.validateListing(
            title = "Apartment",
            type = Type.SELL,
            price = -10f,
            property = dummyProperty,
            agent = dummyAgent
        )

        assertFalse(result.isValid)
    }

    @Test
    fun nanPriceTest() {
        val result = validator.validateListing(
            title = "Apartment",
            type = Type.SELL,
            price = Float.NaN,
            property = dummyProperty,
            agent = dummyAgent
        )

        assertFalse(result.isValid)
    }


    @Test
    fun propertyNullTest() {
        val result = validator.validateListing(
            title = "Apartment",
            type = Type.SELL,
            price = 150000f,
            property = null,
            agent = dummyAgent
        )

        assertFalse(result.isValid)
    }


    @Test
    fun agentNullTest() {
        val result = validator.validateListing(
            title = "Apartment",
            type = Type.SELL,
            price = 150000f,
            property = dummyProperty,
            agent = null
        )

        assertFalse(result.isValid)
    }
}











