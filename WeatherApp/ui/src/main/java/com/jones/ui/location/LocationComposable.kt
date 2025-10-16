package com.jones.ui.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.jones.core.location.Coordinates
import com.jones.core.location.LocationService
import com.jones.ui.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * State for managing location in Compose
 */
data class LocationState(
    val coordinates: Coordinates? = null,
    val isLoading: Boolean = false,
    val hasPermission: Boolean = false,
    val error: String? = null,
)

/**
 * Handles location permissions and fetching
 * @param onLocationReceived Callback when location is successfully retrieved
 * @return LocationState containing current location information
 */
@Composable
fun rememberLocationState(onLocationReceived: (Coordinates) -> Unit = {}): LocationState {
    val context = LocalContext.current
    val locationService = remember { LocationService(context) }
    val scope = rememberCoroutineScope()

    var locationState by remember {
        mutableStateOf(
            LocationState(
                hasPermission = hasLocationPermission(context),
            ),
        )
    }

    // Permission launcher
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            when {
                fineLocationGranted || coarseLocationGranted -> {
                    locationState = locationState.copy(hasPermission = true, error = null)
                    fetchLocation(locationService, scope, context) { newState ->
                        locationState = newState
                        newState.coordinates?.let { onLocationReceived(it) }
                    }
                }
                else -> {
                    locationState =
                        locationState.copy(
                            hasPermission = false,
                            error = context.getString(R.string.location_permission_is_required_to_show_weather_for_your_location),
                        )
                }
            }
        }

    // Auto-fetch location if permission is already granted
    LaunchedEffect(Unit) {
        if (locationState.hasPermission) {
            fetchLocation(locationService, scope, context) { newState ->
                locationState = newState
                newState.coordinates?.let { onLocationReceived(it) }
            }
        }
    }

    // Expose a way to request permissions
    DisposableEffect(Unit) {
        if (!locationState.hasPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        }
        onDispose { }
    }

    return locationState
}

/**
 * Function to request location permission
 */
@Composable
fun RequestLocationPermission(onPermissionResult: (Boolean) -> Unit) {
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            val granted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
            onPermissionResult(granted)
        }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
        )
    }
}

private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
}

private fun fetchLocation(
    locationService: LocationService,
    scope: CoroutineScope,
    context: Context,
    onStateUpdate: (LocationState) -> Unit,
) {
    onStateUpdate(LocationState(isLoading = true, hasPermission = true))

    if (!locationService.isLocationEnabled()) {
        onStateUpdate(
            LocationState(
                hasPermission = true,
                error = context.getString(R.string.please_enable_location_services),
            ),
        )
        return
    }

    scope.launch {
        try {
            val lastLocation = locationService.getLastLocation()
            when {
                lastLocation != null -> {
                    onStateUpdate(
                        LocationState(
                            coordinates = lastLocation,
                            hasPermission = true,
                            isLoading = false,
                        ),
                    )
                }
                else -> {
                    // If no last location, request current location
                    val currentLocation = locationService.getCurrentLocation()
                    when {
                        currentLocation != null -> {
                            onStateUpdate(
                                LocationState(
                                    coordinates = currentLocation,
                                    hasPermission = true,
                                    isLoading = false,
                                ),
                            )
                        }
                        else -> {
                            onStateUpdate(
                                LocationState(
                                    hasPermission = true,
                                    isLoading = false,
                                    error = context.getString(R.string.unable_to_get_location_please_try_again),
                                ),
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            onStateUpdate(
                LocationState(
                    hasPermission = true,
                    isLoading = false,
                    error = e.message ?: context.getString(R.string.failed_to_get_location),
                ),
            )
        }
    }
}
