package com.jones.ui.screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.jones.core.constants.Constants
import com.jones.ui.R
import com.jones.ui.viewmodel.FavoritesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceSearchScreen(
    navController: NavController,
    onPlaceSelected: (String, Double, Double) -> Unit,
    favoritesViewModel: FavoritesViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val tag = "PlaceSearchScreen"

    // Initialize Places API if not already initialized
    LaunchedEffect(Unit) {
        if (!Places.isInitialized()) {
            try {
                Places.initialize(
                    context.applicationContext,
                    Constants.PLACES_API_KEY
                )
            } catch (e: Exception) {
                Log.e(tag, context.getString(R.string.places_api_initialization_failed), e)
            }
        }
    }

    val intentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.let { data ->
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(tag, "Place: ${place.name}, ${place.latLng}, ${place.id}")

                        place.latLng?.let { latLng ->
                            val placeName = place.name ?: context.getString(R.string.unknown_place)

                            // Add to favorites
                            favoritesViewModel.addFavoritePlace(
                                name = placeName,
                                latitude = latLng.latitude,
                                longitude = latLng.longitude
                            )

                            // Update weather for selected location
                            onPlaceSelected(placeName, latLng.latitude, latLng.longitude)

                            Toast.makeText(
                                context,
                                context.getString(R.string.added_successfully, placeName),
                                Toast.LENGTH_SHORT
                            ).show()

                            navController.navigateUp()
                        }
                    }
                }

                AutocompleteActivity.RESULT_ERROR -> {
                    result.data?.let { data ->
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.e(tag, "Place Error status: ${status.statusMessage}")
                        Toast.makeText(
                            context,
                            "Error: ${status.statusMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                Activity.RESULT_CANCELED -> {
                    Log.i(tag, context.getString(R.string.user_cancelled_place_selection))
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "intentLauncher Error", e)
            Toast.makeText(context, "Error selecting place", Toast.LENGTH_SHORT).show()
        }
    }

    val launchMapInputOverlay = {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG
        )

        // Show Google Places Autocomplete Dialog
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY,
            fields
        ).build(context)
        intentLauncher.launch(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.search_location)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.find_a_location),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.search_for_cities_worldwide_to_add_to_your_favorites),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { launchMapInputOverlay() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.search_for_a_place))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Powered by Google Places",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
