package com.fishing.conditions.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.fishing.conditions.ui.components.ConditionsPanel
import com.fishing.conditions.ui.components.SpeciesSelector
import com.fishing.conditions.ui.viewmodel.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val selectedSpecies by viewModel.selectedSpecies.collectAsState()
    val marineConditions by viewModel.marineConditions.collectAsState()
    val fishingSuitability by viewModel.fishingSuitability.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Date selection state
    var selectedDate by remember { mutableStateOf(java.util.Date()) }

    // Location permissions
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )

    // Initialize OSMDroid configuration
    remember {
        Configuration.getInstance().apply {
            userAgentValue = context.packageName
        }
        true
    }

    var myLocationOverlay by remember { mutableStateOf<MyLocationNewOverlay?>(null) }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK) // OpenStreetMap tiles - free!
            setMultiTouchControls(true)
            controller.setZoom(10.0)
            // Default to a fishing-friendly location (coastal area)
            controller.setCenter(GeoPoint(37.7749, -122.4194)) // San Francisco Bay

            // Set up map click listener to update location
            overlays.add(object : org.osmdroid.views.overlay.Overlay() {
                override fun onSingleTapConfirmed(e: android.view.MotionEvent?, mapView: MapView?): Boolean {
                    mapView?.let { map ->
                        val projection = map.projection
                        val geoPoint = projection.fromPixels(e?.x?.toInt() ?: 0, e?.y?.toInt() ?: 0) as? GeoPoint
                        geoPoint?.let {
                            viewModel.updateLocation(it.latitude, it.longitude)
                        }
                    }
                    return true
                }
            })

            // Add my location overlay
            val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), this)
            locationOverlay.enableMyLocation()
            locationOverlay.enableFollowLocation()
            overlays.add(locationOverlay)
            myLocationOverlay = locationOverlay
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    mapView.onResume()
                    myLocationOverlay?.enableMyLocation()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    mapView.onPause()
                    myLocationOverlay?.disableMyLocation()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            myLocationOverlay?.disableMyLocation()
            mapView.onDetach()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Map background
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // Species selector at top
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            SpeciesSelector(
                selectedSpecies = selectedSpecies,
                onSpeciesSelected = { species ->
                    viewModel.selectSpecies(species)
                }
            )
        }

        // My Location button
        FloatingActionButton(
            onClick = {
                if (locationPermissionsState.allPermissionsGranted) {
                    // Get current location and center map
                    getCurrentLocation(context) { lat, lon ->
                        mapView.controller.animateTo(GeoPoint(lat, lon))
                        mapView.controller.setZoom(13.0)
                        viewModel.updateLocation(lat, lon)
                    }
                } else {
                    locationPermissionsState.launchMultiplePermissionRequest()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 180.dp),
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "My Location",
                tint = MaterialTheme.colors.onPrimary
            )
        }

        // Conditions panel at bottom
        marineConditions?.let { conditions ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                ConditionsPanel(
                    conditions = conditions,
                    suitability = fishingSuitability,
                    selectedSpecies = selectedSpecies,
                    selectedDate = selectedDate,
                    onDateSelected = { newDate ->
                        selectedDate = newDate
                        // TODO: Fetch forecast for selected date
                    }
                )
            }
        }

        // Loading indicator
        if (uiState is MapViewModel.MapUiState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Error message
        if (uiState is MapViewModel.MapUiState.Error) {
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.error
            ) {
                Text(
                    text = (uiState as MapViewModel.MapUiState.Error).message,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colors.onError
                )
            }
        }

        // Instructional text when no location selected
        if (marineConditions == null && selectedSpecies != null) {
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tap the map or use 📍 button",
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "to get fishing conditions for ${selectedSpecies?.name ?: "your selected species"}",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Select species prompt
        if (selectedSpecies == null) {
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome to TideRunner!",
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Select a fish species above to get started",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }

        // Location permission dialog
        if (locationPermissionsState.shouldShowRationale) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Location Permission Required") },
                text = {
                    Text("TideRunner needs location access to show fishing conditions at your current location. This helps you find the best spots nearby!")
                },
                confirmButton = {
                    TextButton(onClick = {
                        locationPermissionsState.launchMultiplePermissionRequest()
                    }) {
                        Text("Grant Permission")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { /* dismiss */ }) {
                        Text("Not Now")
                    }
                }
            )
        }
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    context: android.content.Context,
    onLocationReceived: (Double, Double) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location ->
            location?.let {
                onLocationReceived(it.latitude, it.longitude)
            }
        }
}
