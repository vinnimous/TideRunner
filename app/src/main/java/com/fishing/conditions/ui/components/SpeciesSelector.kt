package com.fishing.conditions.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.fishing.conditions.data.models.FishSpeciesDatabase
import com.fishing.conditions.data.models.Species

@Composable
fun SpeciesSelector(
    selectedSpecies: Species?,
    onSpeciesSelected: (Species) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val allSpecies = remember { FishSpeciesDatabase.getAllSpecies() }

    Column(modifier = modifier) {

        /* ---------- Selector Card ---------- */

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Target Species",
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = selectedSpecies?.name ?: "Select a fish species",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold
                    )
                    selectedSpecies?.let {
                        Text(
                            text = it.scientificName,
                            style = MaterialTheme.typography.caption,
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand",
                    tint = MaterialTheme.colors.primary
                )
            }
        }

        /* ---------- Scrollable Popup Dropdown ---------- */

        if (expanded) {
            Popup(
                alignment = Alignment.TopCenter,
                onDismissRequest = { expanded = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(500.dp), // 🔑 HARD CONSTRAINT (required)
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp)
                ) {

                    val listState = rememberLazyListState()

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(), // 🔑 must fill
                        state = listState,
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        val groupedSpecies =
                            allSpecies.groupBy { it.category }

                        groupedSpecies.forEach { (category, speciesList) ->

                            item {
                                Text(
                                    text = getCategoryDisplayName(category),
                                    style = MaterialTheme.typography.subtitle2,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    )
                                )
                            }

                            items(speciesList) { species ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onSpeciesSelected(species)
                                            expanded = false
                                        }
                                        .padding(
                                            horizontal = 16.dp,
                                            vertical = 12.dp
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = species.name,
                                            style = MaterialTheme.typography.body1
                                        )
                                        Text(
                                            text = species.scientificName,
                                            style = MaterialTheme.typography.caption,
                                            fontStyle = FontStyle.Italic,
                                            color = Color.Gray
                                        )
                                    }

                                    if (selectedSpecies?.id == species.id) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = MaterialTheme.colors.primary
                                        )
                                    }
                                }
                            }

                            if (category != groupedSpecies.keys.last()) {
                                item { Divider() }
                            }
                        }
                    }
                }
            }
        }

        /* ---------- Species Info Card ---------- */

        selectedSpecies?.let { species ->
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp,
                backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.7f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Optimal Conditions",
                        style = MaterialTheme.typography.subtitle2,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    InfoRow(
                        "Water Temp",
                        "${species.preferredWaterTemp.min}°C - ${species.preferredWaterTemp.max}°C"
                    )
                    InfoRow("Optimal", "${species.preferredWaterTemp.optimal}°C")
                    InfoRow(
                        "Depth",
                        "${species.preferredDepth.min}m - ${species.preferredDepth.max}m"
                    )
                    InfoRow(
                        "Wind Speed",
                        "${species.preferredWindSpeed.min} - ${species.preferredWindSpeed.max} m/s"
                    )
                    InfoRow(
                        "Wave Height",
                        "${species.preferredWaveHeight.min} - ${species.preferredWaveHeight.max}m"
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = species.description,
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(90.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.caption
        )
    }
}

private fun getCategoryDisplayName(category: Species.FishCategory): String =
    when (category) {
        Species.FishCategory.SALTWATER_INSHORE -> "Saltwater - Inshore"
        Species.FishCategory.SALTWATER_OFFSHORE -> "Saltwater - Offshore"
        Species.FishCategory.FRESHWATER -> "Freshwater"
        Species.FishCategory.MIXED -> "Mixed Water"
    }
