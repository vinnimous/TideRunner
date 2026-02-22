package com.fishing.conditions.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.fishing.conditions.data.models.Species

@Composable
fun SpeciesFilter(
    species: List<Species>,
    selectedSpecies: Set<String>,
    onSpeciesToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {

        // Dropdown trigger card
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
                Text(
                    text = "Filter by Species",
                    style = MaterialTheme.typography.body1,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand",
                    tint = MaterialTheme.colors.primary
                )
            }
        }

        // Scrollable popup dropdown
        if (expanded) {
            Popup(
                alignment = Alignment.TopCenter,
                onDismissRequest = { expanded = false } // closes if click outside
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .heightIn(max = 400.dp),
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(species) { fish ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSpeciesToggle(fish.id)
                                        expanded = false // ✅ closes dropdown
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(fish.name)
                                if (selectedSpecies.contains(fish.id)) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }
                            }
                            Divider()
                        }
                    }
                }
            }
        }
    }
}
