package com.fishing.conditions.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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

    var selectedDate by remember { mutableStateOf(java.util.Date()) }

    // 👇 NEW: Welcome prompt dismissal state
    var showWelcomePrompt by remember { mutableStateOf(true) }

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )

    remember {
        Configuration.getInstance().apply {
            userAgentValue = context.packageName
        }
        true
    }

    var myLocationOverlay by remember { mutableStateOf<MyLocationNewOverlay?>(null) }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(10.0)
            controller.setCenter(GeoPoint(37.7749, -122.4194))

            overlays.add(object : org.osmdroid.views.overlay.Overlay() {
                override fun onSingleTapConfirmed(
                    e: android.view.MotionEvent?,
                    mapView: MapView?
                ): Boolean {
                    mapView?.let { map ->
                        val projection = map.projection
                        val geoPoint = projection.fromPixels(
                            e?.x?.toInt() ?: 0,
                            e?.y?.toInt() ?: 0
                        ) as? GeoPoint
                        geoPoint?.let {
                            viewModel.updateLocation(it.latitude, it.longitude)
                        }
                    }
                    return true
                }
            })

            val locationOverlay =
                MyLocationNewOverlay(GpsMyLocationProvider(context), this)
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

        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            SpeciesSelector(
                selectedSpecies = selectedSpecies,
                onSpeciesSelected = {
                    viewModel.selectSpecies(it)
                    showWelcomePrompt = false
                }
            )
        }

        FloatingActionButton(
            onClick = {
                if (locationPermissionsState.allPermissionsGranted) {
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
                .padding(end = 16.dp, bottom = 180.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "My Location"
            )
        }

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
                    onDateSelected = { selectedDate = it }
                )
            }
        }

        if (uiState is MapViewModel.MapUiState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 👇 FULL-SCREEN TAP DISMISS OVERLAY
        if (selectedSpecies == null && showWelcomePrompt) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        showWelcomePrompt = false
                    }
            )

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
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    context: android.content.Context,
    onLocationReceived: (Double, Double) -> Unit
) {
    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        null
    ).addOnSuccessListener { location ->
        location?.let {
            onLocationReceived(it.latitude, it.longitude)
        }
    }
}
