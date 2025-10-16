package com.jones.ui.viewmodel

import com.jones.domain.model.FavoritePlace
import com.jones.domain.use_case.favourites.FavouritesPlacesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for FavoritesViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {
    private lateinit var favouritesPlacesUseCase: FavouritesPlacesUseCase
    private lateinit var viewModel: FavoritesViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        favouritesPlacesUseCase = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel loads favorite places on initialization`() =
        runTest {
            // Given
            val favoritePlaces =
                listOf(
                    FavoritePlace(
                        id = 1,
                        name = "Nairobi, Kenya",
                        latitude = -1.286389,
                        longitude = 36.817223,
                        addedAt = System.currentTimeMillis(),
                    ),
                    FavoritePlace(
                        id = 2,
                        name = "New York, USA",
                        latitude = 40.7128,
                        longitude = -74.0060,
                        addedAt = System.currentTimeMillis(),
                    ),
                )

            coEvery { favouritesPlacesUseCase.getAllFavoritePlaces() } returns flowOf(favoritePlaces)

            // When
            viewModel = FavoritesViewModel(favouritesPlacesUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertEquals(2, viewModel.favoritePlaces.value.size)
            assertEquals("Nairobi, Kenya", viewModel.favoritePlaces.value[0].name)
            assertEquals("New York, USA", viewModel.favoritePlaces.value[1].name)
        }

    @Test
    fun `addFavoritePlace calls use case with correct parameters`() =
        runTest {
            // Given
            coEvery { favouritesPlacesUseCase.getAllFavoritePlaces() } returns flowOf(emptyList())
            coEvery { favouritesPlacesUseCase.addFavoritePlace(any()) } returns Unit

            viewModel = FavoritesViewModel(favouritesPlacesUseCase)

            // When
            viewModel.addFavoritePlace("Tokyo, Japan", 35.6762, 139.6503)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify {
                favouritesPlacesUseCase.addFavoritePlace(
                    match {
                        it.name == "Tokyo, Japan" &&
                            it.latitude == 35.6762 &&
                            it.longitude == 139.6503
                    },
                )
            }
        }

    @Test
    fun `deleteFavoritePlace calls use case with correct id`() =
        runTest {
            // Given
            coEvery { favouritesPlacesUseCase.getAllFavoritePlaces() } returns flowOf(emptyList())
            coEvery { favouritesPlacesUseCase.deleteFavoritePlace(any()) } returns Unit

            viewModel = FavoritesViewModel(favouritesPlacesUseCase)

            // When
            viewModel.deleteFavoritePlace(5)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify(exactly = 1) { favouritesPlacesUseCase.deleteFavoritePlace(5) }
        }

    @Test
    fun `favoritePlaces starts empty when no data available`() =
        runTest {
            // Given
            coEvery { favouritesPlacesUseCase.getAllFavoritePlaces() } returns flowOf(emptyList())

            // When
            viewModel = FavoritesViewModel(favouritesPlacesUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertTrue(viewModel.favoritePlaces.value.isEmpty())
        }

    @Test
    fun `favoritePlaces updates when use case emits new data`() =
        runTest {
            // Given
            val initialPlaces =
                listOf(
                    FavoritePlace(
                        id = 1,
                        name = "Paris, France",
                        latitude = 48.8566,
                        longitude = 2.3522,
                        addedAt = System.currentTimeMillis(),
                    ),
                )

            coEvery { favouritesPlacesUseCase.getAllFavoritePlaces() } returns flowOf(initialPlaces)

            // When
            viewModel = FavoritesViewModel(favouritesPlacesUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            assertEquals(1, viewModel.favoritePlaces.value.size)
            assertEquals("Paris, France", viewModel.favoritePlaces.value[0].name)
        }

    @Test
    fun `addFavoritePlace handles places with special characters`() =
        runTest {
            // Given
            coEvery { favouritesPlacesUseCase.getAllFavoritePlaces() } returns flowOf(emptyList())
            coEvery { favouritesPlacesUseCase.addFavoritePlace(any()) } returns Unit

            viewModel = FavoritesViewModel(favouritesPlacesUseCase)

            // When
            viewModel.addFavoritePlace("São Paulo, Brazil", -23.5505, -46.6333)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify {
                favouritesPlacesUseCase.addFavoritePlace(
                    match { it.name == "São Paulo, Brazil" },
                )
            }
        }

    @Test
    fun `multiple deleteFavoritePlace calls are handled correctly`() =
        runTest {
            // Given
            coEvery { favouritesPlacesUseCase.getAllFavoritePlaces() } returns flowOf(emptyList())
            coEvery { favouritesPlacesUseCase.deleteFavoritePlace(any()) } returns Unit

            viewModel = FavoritesViewModel(favouritesPlacesUseCase)

            // When
            viewModel.deleteFavoritePlace(1)
            viewModel.deleteFavoritePlace(2)
            viewModel.deleteFavoritePlace(3)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            coVerify(exactly = 1) { favouritesPlacesUseCase.deleteFavoritePlace(1) }
            coVerify(exactly = 1) { favouritesPlacesUseCase.deleteFavoritePlace(2) }
            coVerify(exactly = 1) { favouritesPlacesUseCase.deleteFavoritePlace(3) }
        }
}
