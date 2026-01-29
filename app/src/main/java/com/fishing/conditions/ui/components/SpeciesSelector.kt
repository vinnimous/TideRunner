package com.fishing.conditions.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
        // Selected species display / dropdown trigger
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
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
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
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

        // Dropdown menu with species list
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 500.dp)
        ) {
            // Group by category
            val groupedSpecies = allSpecies.groupBy { it.category }

            androidx.compose.foundation.lazy.LazyColumn {
                groupedSpecies.forEach { (category, speciesList) ->
                    // Category header
                    item {
                        Text(
                            text = getCategoryDisplayName(category),
                            style = MaterialTheme.typography.subtitle2,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    // Species in this category
                    items(speciesList.size) { index ->
                        val species = speciesList[index]
                        DropdownMenuItem(
                            onClick = {
                                onSpeciesSelected(species)
                                expanded = false
                            }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
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
                    }

                    if (category != groupedSpecies.keys.last()) {
                        item {
                            Divider()
                        }
                    }
                }
            }
        }

        // Species info card (when species is selected)
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

                    InfoRow("Water Temp", "${species.preferredWaterTemp.min}°C - ${species.preferredWaterTemp.max}°C")
                    InfoRow("Optimal", "${species.preferredWaterTemp.optimal}°C")
                    InfoRow("Depth", "${species.preferredDepth.min}m - ${species.preferredDepth.max}m")
                    InfoRow("Wind Speed", "${species.preferredWindSpeed.min} - ${species.preferredWindSpeed.max} m/s")
                    InfoRow("Wave Height", "${species.preferredWaveHeight.min} - ${species.preferredWaveHeight.max}m")

                    if (species.preferredMoonPhase.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Best Moon Phases:",
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = species.preferredMoonPhase.joinToString(", ") {
                                it.name.replace("_", " ").lowercase().replaceFirstChar { char -> char.uppercase() }
                            },
                            style = MaterialTheme.typography.caption
                        )
                    }

                    if (species.preferredTidePhase.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Best Tide Phases:",
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = species.preferredTidePhase.joinToString(", ") {
                                it.name.replace("_", " ").lowercase().replaceFirstChar { char -> char.uppercase() }
                            },
                            style = MaterialTheme.typography.caption
                        )
                    }

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

private fun getCategoryDisplayName(category: Species.FishCategory): String {
    return when (category) {
        Species.FishCategory.SALTWATER_INSHORE -> "Saltwater - Inshore"
        Species.FishCategory.SALTWATER_OFFSHORE -> "Saltwater - Offshore"
        Species.FishCategory.FRESHWATER -> "Freshwater"
        Species.FishCategory.MIXED -> "Mixed Water"
    }
}
