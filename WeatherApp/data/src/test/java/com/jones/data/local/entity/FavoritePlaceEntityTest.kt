package com.jones.data.local.entity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for FavoritePlaceEntity
 */
class FavoritePlaceEntityTest {
    @Test
    fun `FavoritePlaceEntity creates instance with all fields`() {
        // Given & When
        val timestamp = System.currentTimeMillis()
        val entity =
            FavoritePlaceEntity(
                id = 1,
                name = "Nairobi, Kenya",
                latitude = -1.286389,
                longitude = 36.817223,
                addedAt = timestamp,
            )

        // Then
        assertEquals(1, entity.id)
        assertEquals("Nairobi, Kenya", entity.name)
        assertEquals(-1.286389, entity.latitude, 0.0001)
        assertEquals(36.817223, entity.longitude, 0.0001)
        assertEquals(timestamp, entity.addedAt)
    }

    @Test
    fun `FavoritePlaceEntity with auto-generated id`() {
        // Given & When
        val before = System.currentTimeMillis()
        val entity =
            FavoritePlaceEntity(
                name = "London, UK",
                latitude = 51.5074,
                longitude = -0.1278,
            )
        val after = System.currentTimeMillis()

        // Then
        assertEquals(0, entity.id)
        assertEquals("London, UK", entity.name)
        assertTrue(entity.addedAt >= before && entity.addedAt <= after)
    }

    @Test
    fun `FavoritePlaceEntity equality works correctly`() {
        // Given
        val timestamp = 1697529600L
        val entity1 =
            FavoritePlaceEntity(
                id = 1,
                name = "Tokyo, Japan",
                latitude = 35.6762,
                longitude = 139.6503,
                addedAt = timestamp,
            )

        val entity2 =
            FavoritePlaceEntity(
                id = 1,
                name = "Tokyo, Japan",
                latitude = 35.6762,
                longitude = 139.6503,
                addedAt = timestamp,
            )

        // Then
        assertEquals(entity1, entity2)
        assertEquals(entity1.hashCode(), entity2.hashCode())
    }

    @Test
    fun `FavoritePlaceEntity with different values are not equal`() {
        // Given
        val entity1 =
            FavoritePlaceEntity(
                id = 1,
                name = "Paris, France",
                latitude = 48.8566,
                longitude = 2.3522,
                addedAt = 1000L,
            )

        val entity2 =
            FavoritePlaceEntity(
                id = 2,
                name = "Berlin, Germany",
                latitude = 52.5200,
                longitude = 13.4050,
                addedAt = 2000L,
            )

        // Then
        assertNotEquals(entity1, entity2)
    }

    @Test
    fun `FavoritePlaceEntity copy works correctly`() {
        // Given
        val original =
            FavoritePlaceEntity(
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
    fun `FavoritePlaceEntity handles various coordinate formats`() {
        // Given & When
        val entities =
            listOf(
                FavoritePlaceEntity(id = 1, name = "Sydney", latitude = -33.8688, longitude = 151.2093),
                FavoritePlaceEntity(id = 2, name = "Moscow", latitude = 55.7558, longitude = 37.6173),
                FavoritePlaceEntity(id = 3, name = "São Paulo", latitude = -23.5505, longitude = -46.6333),
                FavoritePlaceEntity(id = 4, name = "Cairo", latitude = 30.0444, longitude = 31.2357),
            )

        // Then
        assertEquals(4, entities.size)
        assertTrue(entities[0].latitude < 0) // Southern hemisphere
        assertTrue(entities[1].latitude > 0) // Northern hemisphere
        assertTrue(entities[2].latitude < 0 && entities[2].longitude < 0) // SW quadrant
        assertTrue(entities[3].latitude > 0 && entities[3].longitude > 0) // NE quadrant
    }

    @Test
    fun `FavoritePlaceEntity Room compatibility check`() {
        // Given
        val entity =
            FavoritePlaceEntity(
                id = 100,
                name = "Test City",
                latitude = 12.34,
                longitude = 56.78,
                addedAt = 1697529600L,
            )

        // When - simulating Room would use primary key
        val primaryKey = entity.id

        // Then
        assertEquals(100, primaryKey)
    }

    @Test
    fun `FavoritePlaceEntity handles special characters in name`() {
        // Given & When
        val entities =
            listOf(
                FavoritePlaceEntity(id = 1, name = "São Paulo, Brazil", latitude = 0.0, longitude = 0.0),
                FavoritePlaceEntity(id = 2, name = "Zürich, Switzerland", latitude = 0.0, longitude = 0.0),
                FavoritePlaceEntity(id = 3, name = "Москва, Russia", latitude = 0.0, longitude = 0.0),
                FavoritePlaceEntity(id = 4, name = "北京, China", latitude = 0.0, longitude = 0.0),
            )

        // Then
        assertEquals("São Paulo, Brazil", entities[0].name)
        assertEquals("Zürich, Switzerland", entities[1].name)
        assertEquals("Москва, Russia", entities[2].name)
        assertEquals("北京, China", entities[3].name)
    }

    @Test
    fun `FavoritePlaceEntity timestamp ordering`() {
        // Given
        val entity1 =
            FavoritePlaceEntity(
                id = 1,
                name = "Place 1",
                latitude = 0.0,
                longitude = 0.0,
                addedAt = 1000L,
            )

        val entity2 =
            FavoritePlaceEntity(
                id = 2,
                name = "Place 2",
                latitude = 0.0,
                longitude = 0.0,
                addedAt = 2000L,
            )

        val entity3 =
            FavoritePlaceEntity(
                id = 3,
                name = "Place 3",
                latitude = 0.0,
                longitude = 0.0,
                addedAt = 1500L,
            )

        // When
        val sorted = listOf(entity1, entity2, entity3).sortedBy { it.addedAt }

        // Then
        assertEquals(entity1.id, sorted[0].id)
        assertEquals(entity3.id, sorted[1].id)
        assertEquals(entity2.id, sorted[2].id)
    }

    @Test
    fun `FavoritePlaceEntity handles extreme coordinates`() {
        // Given & When
        val entities =
            listOf(
                FavoritePlaceEntity(id = 1, name = "North Pole", latitude = 90.0, longitude = 0.0),
                FavoritePlaceEntity(id = 2, name = "South Pole", latitude = -90.0, longitude = 0.0),
                FavoritePlaceEntity(id = 3, name = "Date Line West", latitude = 0.0, longitude = -180.0),
                FavoritePlaceEntity(id = 4, name = "Date Line East", latitude = 0.0, longitude = 180.0),
            )

        // Then
        assertEquals(90.0, entities[0].latitude, 0.0001)
        assertEquals(-90.0, entities[1].latitude, 0.0001)
        assertEquals(-180.0, entities[2].longitude, 0.0001)
        assertEquals(180.0, entities[3].longitude, 0.0001)
    }
}
