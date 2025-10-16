package com.jones.data.repository

import com.jones.data.local.dao.FavoritePlaceDao
import com.jones.data.local.entity.FavoritePlaceEntity
import com.jones.domain.model.FavoritePlace
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for FavoritePlaceRepositoryImpl
 */
class FavoritePlaceRepositoryImplTest {

    private lateinit var dao: FavoritePlaceDao
    private lateinit var repository: FavoritePlaceRepositoryImpl

    @Before
    fun setup() {
        dao = mockk()
        repository = FavoritePlaceRepositoryImpl(dao)
    }

    @Test
    fun `getAllFavoritePlaces returns mapped domain models`() = runTest {
        // Given
        val entities = listOf(
            FavoritePlaceEntity(
                id = 1,
                name = "Nairobi, Kenya",
                latitude = -1.286389,
                longitude = 36.817223,
                addedAt = 1697529600L
            ),
            FavoritePlaceEntity(
                id = 2,
                name = "New York, USA",
                latitude = 40.7128,
                longitude = -74.0060,
                addedAt = 1697529700L
            )
        )

        every { dao.getAllFavoritePlaces() } returns flowOf(entities)

        // When
        val result = repository.getAllFavoritePlaces().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Nairobi, Kenya", result[0].name)
        assertEquals("New York, USA", result[1].name)
        assertEquals(-1.286389, result[0].latitude, 0.0001)
        assertEquals(40.7128, result[1].latitude, 0.0001)
    }

    @Test
    fun `getAllFavoritePlaces returns empty list when no data`() = runTest {
        // Given
        every { dao.getAllFavoritePlaces() } returns flowOf(emptyList())

        // When
        val result = repository.getAllFavoritePlaces().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getFavoritePlaceById returns mapped domain model`() = runTest {
        // Given
        val entity = FavoritePlaceEntity(
            id = 3,
            name = "London, UK",
            latitude = 51.5074,
            longitude = -0.1278,
            addedAt = 1697529600L
        )

        coEvery { dao.getFavoritePlaceById(3) } returns entity

        // When
        val result = repository.getFavoritePlaceById(3)

        // Then
        assertNotNull(result)
        assertEquals(3, result?.id)
        assertEquals("London, UK", result?.name)
        assertEquals(51.5074, result!!.latitude, 0.0001)
        coVerify { dao.getFavoritePlaceById(3) }
    }

    @Test
    fun `getFavoritePlaceById returns null when place not found`() = runTest {
        // Given
        coEvery { dao.getFavoritePlaceById(999) } returns null

        // When
        val result = repository.getFavoritePlaceById(999)

        // Then
        assertNull(result)
        coVerify { dao.getFavoritePlaceById(999) }
    }

    @Test
    fun `insertFavoritePlace converts and inserts entity`() = runTest {
        // Given
        val domainPlace = FavoritePlace(
            id = 0,
            name = "Tokyo, Japan",
            latitude = 35.6762,
            longitude = 139.6503,
            addedAt = 1697529600L
        )

        coEvery { dao.insertFavoritePlace(any()) } returns Unit

        // When
        repository.insertFavoritePlace(domainPlace)

        // Then
        coVerify {
            dao.insertFavoritePlace(
                match {
                    it.name == "Tokyo, Japan" &&
                    it.latitude == 35.6762 &&
                    it.longitude == 139.6503
                }
            )
        }
    }

    @Test
    fun `deleteFavoritePlace converts and deletes entity`() = runTest {
        // Given
        val domainPlace = FavoritePlace(
            id = 5,
            name = "Paris, France",
            latitude = 48.8566,
            longitude = 2.3522,
            addedAt = 1697529600L
        )

        coEvery { dao.deleteFavoritePlace(any()) } returns Unit

        // When
        repository.deleteFavoritePlace(domainPlace)

        // Then
        coVerify {
            dao.deleteFavoritePlace(
                match {
                    it.id == 5 &&
                    it.name == "Paris, France"
                }
            )
        }
    }

    @Test
    fun `deleteFavoritePlaceById calls dao with correct id`() = runTest {
        // Given
        coEvery { dao.deleteFavoritePlaceById(7) } returns Unit

        // When
        repository.deleteFavoritePlaceById(7)

        // Then
        coVerify(exactly = 1) { dao.deleteFavoritePlaceById(7) }
    }

    @Test
    fun `getAllFavoritePlaces maintains order from dao`() = runTest {
        // Given
        val entities = listOf(
            FavoritePlaceEntity(id = 3, name = "Place 3", latitude = 0.0, longitude = 0.0, addedAt = 3000L),
            FavoritePlaceEntity(id = 1, name = "Place 1", latitude = 0.0, longitude = 0.0, addedAt = 1000L),
            FavoritePlaceEntity(id = 2, name = "Place 2", latitude = 0.0, longitude = 0.0, addedAt = 2000L)
        )

        every { dao.getAllFavoritePlaces() } returns flowOf(entities)

        // When
        val result = repository.getAllFavoritePlaces().first()

        // Then
        assertEquals(3, result.size)
        assertEquals(3, result[0].id)
        assertEquals(1, result[1].id)
        assertEquals(2, result[2].id)
    }

    @Test
    fun `insertFavoritePlace preserves all fields during mapping`() = runTest {
        // Given
        val timestamp = System.currentTimeMillis()
        val domainPlace = FavoritePlace(
            id = 10,
            name = "Dubai, UAE",
            latitude = 25.2048,
            longitude = 55.2708,
            addedAt = timestamp
        )

        coEvery { dao.insertFavoritePlace(any()) } returns Unit

        // When
        repository.insertFavoritePlace(domainPlace)

        // Then
        coVerify {
            dao.insertFavoritePlace(
                match {
                    it.id == 10 &&
                    it.name == "Dubai, UAE" &&
                    it.latitude == 25.2048 &&
                    it.longitude == 55.2708 &&
                    it.addedAt == timestamp
                }
            )
        }
    }

    @Test
    fun `getAllFavoritePlaces handles multiple places with different coordinates`() = runTest {
        // Given
        val entities = listOf(
            FavoritePlaceEntity(id = 1, name = "Sydney", latitude = -33.8688, longitude = 151.2093, addedAt = 1000L),
            FavoritePlaceEntity(id = 2, name = "Moscow", latitude = 55.7558, longitude = 37.6173, addedAt = 2000L),
            FavoritePlaceEntity(id = 3, name = "Rio", latitude = -22.9068, longitude = -43.1729, addedAt = 3000L)
        )

        every { dao.getAllFavoritePlaces() } returns flowOf(entities)

        // When
        val result = repository.getAllFavoritePlaces().first()

        // Then
        assertEquals(3, result.size)
        assertTrue(result[0].latitude < 0)
        assertTrue(result[1].latitude > 0)
        assertTrue(result[2].latitude < 0 && result[2].longitude < 0)
    }

    @Test
    fun `repository handles flow updates correctly`() = runTest {
        // Given
        val initialEntities = listOf(
            FavoritePlaceEntity(id = 1, name = "Place 1", latitude = 0.0, longitude = 0.0, addedAt = 1000L)
        )

        val updatedEntities = listOf(
            FavoritePlaceEntity(id = 1, name = "Place 1", latitude = 0.0, longitude = 0.0, addedAt = 1000L),
            FavoritePlaceEntity(id = 2, name = "Place 2", latitude = 1.0, longitude = 1.0, addedAt = 2000L)
        )

        every { dao.getAllFavoritePlaces() } returns flowOf(initialEntities, updatedEntities)

        // When
        val results = mutableListOf<List<FavoritePlace>>()
        repository.getAllFavoritePlaces().collect { results.add(it) }

        // Then
        assertEquals(2, results.size)
        assertEquals(1, results[0].size)
        assertEquals(2, results[1].size)
    }
}
