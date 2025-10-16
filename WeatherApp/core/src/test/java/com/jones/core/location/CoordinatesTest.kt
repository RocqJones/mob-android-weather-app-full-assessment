package com.jones.core.location

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Unit tests for Coordinates data class
 */
class CoordinatesTest {
    @Test
    fun `Coordinates creates instance with correct values`() {
        // Given
        val latitude = -1.2921
        val longitude = 36.8219

        // When
        val coordinates = Coordinates(latitude, longitude)

        // Then
        assertEquals(latitude, coordinates.latitude, 0.0001)
        assertEquals(longitude, coordinates.longitude, 0.0001)
    }

    @Test
    fun `Coordinates with same values are equal`() {
        // Given
        val coords1 = Coordinates(-1.2921, 36.8219)
        val coords2 = Coordinates(-1.2921, 36.8219)

        // Then
        assertEquals(coords1, coords2)
    }

    @Test
    fun `Coordinates with different values are not equal`() {
        // Given
        val coords1 = Coordinates(-1.2921, 36.8219)
        val coords2 = Coordinates(-1.2922, 36.8219)

        // Then
        assertNotEquals(coords1, coords2)
    }

    @Test
    fun `Coordinates supports positive latitude and longitude`() {
        // Given
        val latitude = 40.7128
        val longitude = -74.0060

        // When
        val coordinates = Coordinates(latitude, longitude)

        // Then
        assertEquals(latitude, coordinates.latitude, 0.0001)
        assertEquals(longitude, coordinates.longitude, 0.0001)
    }

    @Test
    fun `Coordinates supports negative latitude and longitude`() {
        // Given
        val latitude = -33.8688
        val longitude = 151.2093

        // When
        val coordinates = Coordinates(latitude, longitude)

        // Then
        assertEquals(latitude, coordinates.latitude, 0.0001)
        assertEquals(longitude, coordinates.longitude, 0.0001)
    }

    @Test
    fun `Coordinates handles edge case values`() {
        // Given - Maximum valid latitude/longitude
        val coords1 = Coordinates(90.0, 180.0)
        val coords2 = Coordinates(-90.0, -180.0)
        val coords3 = Coordinates(0.0, 0.0)

        // Then
        assertEquals(90.0, coords1.latitude, 0.0001)
        assertEquals(180.0, coords1.longitude, 0.0001)
        assertEquals(-90.0, coords2.latitude, 0.0001)
        assertEquals(-180.0, coords2.longitude, 0.0001)
        assertEquals(0.0, coords3.latitude, 0.0001)
        assertEquals(0.0, coords3.longitude, 0.0001)
    }

    @Test
    fun `Coordinates copy works correctly`() {
        // Given
        val original = Coordinates(-1.2921, 36.8219)

        // When
        val copy = original.copy()
        val modifiedCopy = original.copy(latitude = -1.3000)

        // Then
        assertEquals(original, copy)
        assertEquals(-1.3000, modifiedCopy.latitude, 0.0001)
        assertEquals(36.8219, modifiedCopy.longitude, 0.0001)
    }

    @Test
    fun `Coordinates toString provides readable output`() {
        // Given
        val coordinates = Coordinates(-1.2921, 36.8219)

        // When
        val string = coordinates.toString()

        // Then
        assert(string.contains("-1.2921"))
        assert(string.contains("36.8219"))
    }

    @Test
    fun `Coordinates hashCode is consistent`() {
        // Given
        val coords1 = Coordinates(-1.2921, 36.8219)
        val coords2 = Coordinates(-1.2921, 36.8219)
        val coords3 = Coordinates(-1.2922, 36.8219)

        // Then
        assertEquals(coords1.hashCode(), coords2.hashCode())
        assertNotEquals(coords1.hashCode(), coords3.hashCode())
    }
}
