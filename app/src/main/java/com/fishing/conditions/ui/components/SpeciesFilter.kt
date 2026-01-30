package com.fishing.conditions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fishing.conditions.data.models.Species

@Composable
fun SpeciesFilter(
    species: List<Species>,
    selectedSpecies: Set<String>,
    onSpeciesToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 96.dp) // 🔑 CRITICAL FIX
            .padding(16.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Filter by Species",
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(species) { fish ->
                    FilterChip(
                        selected = selectedSpecies.contains(fish.id),
                        onClick = { onSpeciesToggle(fish.id) },
                        content = { Text(fish.name) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.height(32.dp),
        shape = MaterialTheme.shapes.small,
        color = if (selected) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.surface
        },
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            content()
        }
    }
}
