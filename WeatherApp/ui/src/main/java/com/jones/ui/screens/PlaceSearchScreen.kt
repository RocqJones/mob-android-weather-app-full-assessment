package com.jones.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jones.ui.viewmodel.FavoritesViewModel
import org.koin.androidx.compose.koinViewModel

data class PlacePrediction(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String,
    val latitude: Double,
    val longitude: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceSearchScreen(
    navController: NavController,
    onPlaceSelected: (String, Double, Double) -> Unit,
    favoritesViewModel: FavoritesViewModel = koinViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<PlacePrediction>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Location") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
                .padding(16.dp)
        ) {
            // Search TextField
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    // TODO: Implement Google Places Autocomplete API call here
                    // For now, using mock data
                    if (query.length >= 3) {
                        isSearching = true
                        // Mock predictions - replace with actual Google Places API call
                        predictions = listOf(
                            PlacePrediction(
                                placeId = "1",
                                primaryText = "Nairobi",
                                secondaryText = "Kenya",
                                latitude = -1.286389,
                                longitude = 36.817223
                            ),
                            PlacePrediction(
                                placeId = "2",
                                primaryText = "New York",
                                secondaryText = "USA",
                                latitude = 40.7128,
                                longitude = -74.0060
                            ),
                            PlacePrediction(
                                placeId = "3",
                                primaryText = "London",
                                secondaryText = "UK",
                                latitude = 51.5074,
                                longitude = -0.1278
                            ),
                            PlacePrediction(
                                placeId = "4",
                                primaryText = "Tokyo",
                                secondaryText = "Japan",
                                latitude = 35.6762,
                                longitude = 139.6503
                            ),
                            PlacePrediction(
                                placeId = "5",
                                primaryText = "Paris",
                                secondaryText = "France",
                                latitude = 48.8566,
                                longitude = 2.3522
                            )
                        ).filter {
                            it.primaryText.contains(query, ignoreCase = true) ||
                            it.secondaryText.contains(query, ignoreCase = true)
                        }
                        isSearching = false
                    } else {
                        predictions = emptyList()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search for a city...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isSearching) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Search Results
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(predictions) { prediction ->
                    PlacePredictionItem(
                        prediction = prediction,
                        onClick = {
                            val placeName = "${prediction.primaryText}, ${prediction.secondaryText}"
                            favoritesViewModel.addFavoritePlace(
                                name = placeName,
                                latitude = prediction.latitude,
                                longitude = prediction.longitude
                            )
                            onPlaceSelected(placeName, prediction.latitude, prediction.longitude)
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PlacePredictionItem(
    prediction: PlacePrediction,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = prediction.primaryText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = prediction.secondaryText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
