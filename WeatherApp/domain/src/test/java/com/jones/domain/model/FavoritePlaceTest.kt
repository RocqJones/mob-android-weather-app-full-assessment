package com.jones.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for FavoritePlace domain model
 */
class FavoritePlaceTest {
    @Test
    fun `FavoritePlace creates instance with all fields`() {
        // Given & When
        val timestamp = System.currentTimeMillis()
        val place =
            FavoritePlace(
                id = 1,
                name = "Nairobi, Kenya",
                latitude = -1.286389,
                longitude = 36.817223,
                addedAt = timestamp,
            )

        // Then
        assertEquals(1, place.id)
        assertEquals("Nairobi, Kenya", place.name)
        assertEquals(-1.286389, place.latitude, 0.0001)
        assertEquals(36.817223, place.longitude, 0.0001)
        assertEquals(timestamp, place.addedAt)
    }

    @Test
    fun `FavoritePlace with default values`() {
        // Given & When
        val before = System.currentTimeMillis()
        val place =
            FavoritePlace(
                name = "London, UK",
                latitude = 51.5074,
                longitude = -0.1278,
            )
        val after = System.currentTimeMillis()

        // Then
        assertEquals(0, place.id)
        assertEquals("London, UK", place.name)
        assertTrue(place.addedAt >= before && place.addedAt <= after)
    }

    @Test
    fun `FavoritePlace equality works correctly`() {
        // Given
        val timestamp = 1697529600L
        val place1 =
            FavoritePlace(
                id = 1,
                name = "Tokyo, Japan",
                latitude = 35.6762,
                longitude = 139.6503,
                addedAt = timestamp,
            )

        val place2 =
            FavoritePlace(
                id = 1,
                name = "Tokyo, Japan",
                latitude = 35.6762,
                longitude = 139.6503,
                addedAt = timestamp,
            )

        // Then
        assertEquals(place1, place2)
        assertEquals(place1.hashCode(), place2.hashCode())
    }

    @Test
    fun `FavoritePlace with different values are not equal`() {
        // Given
        val place1 =
            FavoritePlace(
                id = 1,
                name = "Paris, France",
                latitude = 48.8566,
                longitude = 2.3522,
            )

        val place2 =
            FavoritePlace(
                id = 2,
                name = "Berlin, Germany",
                latitude = 52.5200,
                longitude = 13.4050,
            )

        // Then
        assertNotEquals(place1, place2)
    }

    @Test
    fun `FavoritePlace copy works correctly`() {
        // Given
        val original =
            FavoritePlace(
                id = 1,
                name = "New York, USA",
                latitude = 40.7128,
                longitude = -74.0060,
                addedAt = 1697529600L,
            )

        // When
        val copy = original.copy(name = "Brooklyn, USA")

        // Then
        assertEquals("Brooklyn, USA", copy.name)
        assertEquals(original.id, copy.id)
        assertEquals(original.latitude, copy.latitude, 0.0001)
        assertEquals(original.longitude, copy.longitude, 0.0001)
        assertEquals(original.addedAt, copy.addedAt)
    }

    @Test
    fun `FavoritePlace handles various coordinate formats`() {
        // Given & When
        val places =
            listOf(
                FavoritePlace(id = 1, name = "Sydney", latitude = -33.8688, longitude = 151.2093),
                FavoritePlace(id = 2, name = "Moscow", latitude = 55.7558, longitude = 37.6173),
                FavoritePlace(id = 3, name = "São Paulo", latitude = -23.5505, longitude = -46.6333),
                FavoritePlace(id = 4, name = "Cairo", latitude = 30.0444, longitude = 31.2357),
            )

        // Then
        assertEquals(4, places.size)
        assertTrue(places[0].latitude < 0) // Southern hemisphere
        assertTrue(places[1].latitude > 0) // Northern hemisphere
        assertTrue(places[2].latitude < 0 && places[2].longitude < 0) // SW quadrant
        assertTrue(places[3].latitude > 0 && places[3].longitude > 0) // NE quadrant
    }

    @Test
    fun `FavoritePlace handles places with same coordinates but different names`() {
        // Given
        val place1 =
            FavoritePlace(
                id = 1,
                name = "Location A",
                latitude = 10.0,
                longitude = 20.0,
            )

        val place2 =
            FavoritePlace(
                id = 2,
                name = "Location B",
                latitude = 10.0,
                longitude = 20.0,
            )

        // Then
        assertNotEquals(place1, place2) // Different IDs make them different
        assertEquals(place1.latitude, place2.latitude, 0.0001)
        assertEquals(place1.longitude, place2.longitude, 0.0001)
    }

    @Test
    fun `FavoritePlace timestamp increments for different places`() {
        // Given & When
        val place1 = FavoritePlace(name = "Place 1", latitude = 0.0, longitude = 0.0)
        Thread.sleep(10) // Small delay to ensure different timestamps
        val place2 = FavoritePlace(name = "Place 2", latitude = 1.0, longitude = 1.0)

        // Then
        assertTrue(place2.addedAt >= place1.addedAt)
    }
}
