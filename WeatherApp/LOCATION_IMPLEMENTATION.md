# Location-Based Weather Implementation

This document describes how the location-based weather feature has been implemented in the Weather App.

## Overview

The app now automatically fetches the user's current location and displays weather data for that location instead of using hardcoded coordinates.

## Components Created

### 1. Core Module - Location Services

#### `Coordinates.kt`
- Data class representing geographic coordinates (latitude, longitude)

#### `LocationService.kt`
- Service class that wraps Google Play Services Location API
- Provides methods to:
  - Check if location services are enabled
  - Get last known location (fast)
  - Get current location (accurate)
  - Get location updates as a Flow (for continuous tracking)

### 2. UI Module - Location Composables

#### `LocationComposable.kt`
- **`LocationState`**: Data class holding location state (coordinates, loading, permission status, errors)
- **`rememberLocationState()`**: Composable function that:
  - Requests location permissions
  - Fetches user's location
  - Returns LocationState
  - Accepts a callback when location is received
- **`RequestLocationPermission()`**: Standalone composable for requesting permissions

### 3. Updated Components

#### `WeatherViewModel.kt`
- Added `updateLocation(coordinates: Coordinates)` method
- Modified to accept dynamic coordinates instead of hardcoded values
- Falls back to default coordinates if location is unavailable
- Fetches weather data when location is updated

#### `NavigationGraph.kt`
- Integrated `rememberLocationState()` composable
- Automatically updates ViewModel when user's location is received
- Location is fetched once when the app starts

#### `AndroidManifest.xml`
- Added location permissions:
  - `ACCESS_FINE_LOCATION` - For precise location
  - `ACCESS_COARSE_LOCATION` - For approximate location

#### `core/build.gradle.kts`
- Added Google Play Services Location dependency

## How It Works

1. **App Starts**: `NavigationGraph` calls `rememberLocationState()`
2. **Permission Request**: User is prompted to grant location permission
3. **Location Fetch**: If granted, the app fetches the user's current location
4. **Weather Update**: Location coordinates are passed to `WeatherViewModel.updateLocation()`
5. **Data Display**: Weather data is fetched and displayed for the user's location

## Fallback Behavior

- If location permission is denied: Uses default coordinates (Nairobi, Kenya)
- If location services are disabled: Shows error message, uses default coordinates
- If location fetch fails: Uses default coordinates

## Benefits

✅ **Automatic Location Detection**: No need to manually enter location
✅ **Runtime Permissions**: Follows Android best practices for permissions
✅ **Composable Architecture**: Integrates seamlessly with Jetpack Compose
✅ **Error Handling**: Graceful fallback to default coordinates
✅ **Efficient**: Uses last known location first, then requests current if needed

## Testing

To test the location feature:
1. Grant location permission when prompted
2. Ensure location services are enabled on your device
3. The app will display weather for your current location
4. City name in the weather card will show your actual location

