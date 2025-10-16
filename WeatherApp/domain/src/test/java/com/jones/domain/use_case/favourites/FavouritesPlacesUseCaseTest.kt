package com.jones.domain.use_case.favourites

import com.jones.domain.model.FavoritePlace
import com.jones.domain.repository.FavoritePlaceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for FavouritesPlacesUseCase
 */
class FavouritesPlacesUseCaseTest {

    private lateinit var repository: FavoritePlaceRepository
    private lateinit var useCase: FavouritesPlacesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = FavouritesPlacesUseCase(repository)
    }

    @Test
    fun `getAllFavoritePlaces returns list of favorite places`() = runTest {
        // Given
        val favoritePlaces = listOf(
            FavoritePlace(
                id = 1,
                name = "Nairobi, Kenya",
                latitude = -1.286389,
                longitude = 36.817223,
                addedAt = System.currentTimeMillis()
            ),
            FavoritePlace(
                id = 2,
                name = "New York, USA",
                latitude = 40.7128,
                longitude = -74.0060,
                addedAt = System.currentTimeMillis()
            )
        )

        coEvery { repository.getAllFavoritePlaces() } returns flowOf(favoritePlaces)

        // When
        val result = useCase.getAllFavoritePlaces().first()

        // Then
        Assert.assertEquals(2, result.size)
        Assert.assertEquals("Nairobi, Kenya", result[0].name)
        Assert.assertEquals("New York, USA", result[1].name)
        coVerify { repository.getAllFavoritePlaces() }
    }

    @Test
    fun `getAllFavoritePlaces returns empty list when no favorites`() = runTest {
        // Given
        coEvery { repository.getAllFavoritePlaces() } returns flowOf(emptyList())

        // When
        val result = useCase.getAllFavoritePlaces().first()

        // Then
        Assert.assertTrue(result.isEmpty())
        coVerify { repository.getAllFavoritePlaces() }
    }

    @Test
    fun `addFavoritePlace successfully adds new place`() = runTest {
        // Given
        val newPlace = FavoritePlace(
            id = 0,
            name = "Tokyo, Japan",
            latitude = 35.6762,
            longitude = 139.6503,
            addedAt = System.currentTimeMillis()
        )

        coEvery { repository.insertFavoritePlace(newPlace) } returns Unit

        // When
        useCase.addFavoritePlace(newPlace)

        // Then
        coVerify(exactly = 1) { repository.insertFavoritePlace(newPlace) }
    }

    @Test
    fun `addFavoritePlace handles place with auto-generated id`() = runTest {
        // Given
        val newPlace = FavoritePlace(
            name = "Paris, France",
            latitude = 48.8566,
            longitude = 2.3522
        )

        coEvery { repository.insertFavoritePlace(newPlace) } returns Unit

        // When
        useCase.addFavoritePlace(newPlace)

        // Then
        coVerify(exactly = 1) { repository.insertFavoritePlace(newPlace) }
    }

    @Test
    fun `deleteFavoritePlace removes place by id`() = runTest {
        // Given
        val placeId = 5

        coEvery { repository.deleteFavoritePlaceById(placeId) } returns Unit

        // When
        useCase.deleteFavoritePlace(placeId)

        // Then
        coVerify(exactly = 1) { repository.deleteFavoritePlaceById(placeId) }
    }

    @Test
    fun `getFavoritePlaceById returns correct place`() = runTest {
        // Given
        val placeId = 3
        val expectedPlace = FavoritePlace(
            id = placeId,
            name = "London, UK",
            latitude = 51.5074,
            longitude = -0.1278,
            addedAt = System.currentTimeMillis()
        )

        coEvery { repository.getFavoritePlaceById(placeId) } returns expectedPlace

        // When
        val result = useCase.getFavoritePlaceById(placeId)

        // Then
        Assert.assertNotNull(result)
        Assert.assertEquals(placeId, result?.id)
        Assert.assertEquals("London, UK", result?.name)
        coVerify { repository.getFavoritePlaceById(placeId) }
    }

    @Test
    fun `getFavoritePlaceById returns null when place not found`() = runTest {
        // Given
        val placeId = 999

        coEvery { repository.getFavoritePlaceById(placeId) } returns null

        // When
        val result = useCase.getFavoritePlaceById(placeId)

        // Then
        Assert.assertNull(result)
        coVerify { repository.getFavoritePlaceById(placeId) }
    }

    @Test
    fun `getAllFavoritePlaces handles places with different coordinates`() = runTest {
        // Given
        val favoritePlaces = listOf(
            FavoritePlace(
                id = 1,
                name = "Sydney, Australia",
                latitude = -33.8688,
                longitude = 151.2093,
                addedAt = System.currentTimeMillis()
            ),
            FavoritePlace(
                id = 2,
                name = "Rio de Janeiro, Brazil",
                latitude = -22.9068,
                longitude = -43.1729,
                addedAt = System.currentTimeMillis()
            ),
            FavoritePlace(
                id = 3,
                name = "Moscow, Russia",
                latitude = 55.7558,
                longitude = 37.6173,
                addedAt = System.currentTimeMillis()
            )
        )

        coEvery { repository.getAllFavoritePlaces() } returns flowOf(favoritePlaces)

        // When
        val result = useCase.getAllFavoritePlaces().first()

        // Then
        assertEquals(3, result.size)
        assertTrue(result[0].latitude < 0)
        assertTrue(result[1].latitude < 0)
        assertTrue(result[2].latitude > 0)
    }

    @Test
    fun `addFavoritePlace preserves timestamp`() = runTest {
        // Given
        val timestamp = System.currentTimeMillis()
        val newPlace = FavoritePlace(
            id = 0,
            name = "Dubai, UAE",
            latitude = 25.2048,
            longitude = 55.2708,
            addedAt = timestamp
        )

        coEvery { repository.insertFavoritePlace(any()) } returns Unit

        // When
        useCase.addFavoritePlace(newPlace)

        // Then
        coVerify { repository.insertFavoritePlace(match { it.addedAt == timestamp }) }
    }

    @Test
    fun `getAllFavoritePlaces maintains order from repository`() = runTest {
        // Given
        val favoritePlaces = listOf(
            FavoritePlace(
                id = 3,
                name = "Place 3",
                latitude = 0.0,
                longitude = 0.0,
                addedAt = 3000L
            ),
            FavoritePlace(
                id = 1,
                name = "Place 1",
                latitude = 0.0,
                longitude = 0.0,
                addedAt = 1000L
            ),
            FavoritePlace(
                id = 2,
                name = "Place 2",
                latitude = 0.0,
                longitude = 0.0,
                addedAt = 2000L
            )
        )

        coEvery { repository.getAllFavoritePlaces() } returns flowOf(favoritePlaces)

        // When
        val result = useCase.getAllFavoritePlaces().first()

        // Then
        Assert.assertEquals(3, result.size)
        Assert.assertEquals(3, result[0].id)
        Assert.assertEquals(1, result[1].id)
        Assert.assertEquals(2, result[2].id)
    }

    @Test
    fun `deleteFavoritePlace handles multiple deletes`() = runTest {
        // Given
        val placeIds = listOf(1, 2, 3)

        placeIds.forEach { id ->
            coEvery { repository.deleteFavoritePlaceById(id) } returns Unit
        }

        // When
        placeIds.forEach { id ->
            useCase.deleteFavoritePlace(id)
        }

        // Then
        placeIds.forEach { id ->
            coVerify(exactly = 1) { repository.deleteFavoritePlaceById(id) }
        }
    }

    @Test
    fun `addFavoritePlace handles places with same coordinates but different names`() = runTest {
        // Given
        val place1 = FavoritePlace(
            id = 1,
            name = "Location A",
            latitude = 10.0,
            longitude = 20.0,
            addedAt = System.currentTimeMillis()
        )
        val place2 = FavoritePlace(
            id = 2,
            name = "Location B",
            latitude = 10.0,
            longitude = 20.0,
            addedAt = System.currentTimeMillis()
        )

        coEvery { repository.insertFavoritePlace(any()) } returns Unit

        // When
        useCase.addFavoritePlace(place1)
        useCase.addFavoritePlace(place2)

        // Then
        coVerify(exactly = 1) { repository.insertFavoritePlace(place1) }
        coVerify(exactly = 1) { repository.insertFavoritePlace(place2) }
    }
}