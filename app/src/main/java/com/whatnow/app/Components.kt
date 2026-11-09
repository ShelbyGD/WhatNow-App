package com.whatnow.app

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IdeaCard(
    idea: Idea,
    isFav: Boolean,
    onToggleFav: () -> Unit,
    onOpenMaps: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = idea.emoji, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = idea.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${idea.minutes} Min • ${if (idea.cost == 0) "Kostenlos" else "Max. ${idea.cost} €"} • ${if (idea.isHome) "Zuhause" else "In der Nähe"}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onToggleFav) {
                    Icon(
                        imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorit",
                        tint = if (isFav) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = idea.desc, style = MaterialTheme.typography.bodyMedium)

            if (idea.mapsQuery != null) {
                Spacer(modifier = Modifier.height(10.dp))
                FilledTonalButton(
                    onClick = { onOpenMaps(idea.mapsQuery) },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("In der Nähe auf Maps suchen")
                }
            }
        }
    }
}

@Composable
fun RandomIdeaDialog(
    idea: Idea,
    onDismiss: () -> Unit,
    onReroll: () -> Unit,
    onOpenMaps: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Text(idea.emoji, fontSize = 44.sp) },
        title = { Text(idea.title, fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(idea.desc, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "⏱️ ${idea.minutes} Min  |  💶 ${if (idea.cost == 0) "0 €" else "max. ${idea.cost} €"}  |  📍 ${if (idea.isHome) "Zuhause" else "Unterwegs"}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                if (idea.mapsQuery != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(onClick = { onOpenMaps(idea.mapsQuery) }) {
                        Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Auf Google Maps zeigen")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onReroll) {
                Icon(Icons.Default.Casino, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Neu würfeln")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Super, mach ich!") }
        }
    )
}

@Composable
fun AddIdeaDialog(onDismiss: () -> Unit, onAdd: (Idea) -> Unit) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var isHome by remember { mutableStateOf(true) }
    var forFriends by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eigene Idee hinzufügen") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titel der Aktivität") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Kurze Beschreibung") }
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = isHome,
                        onClick = { isHome = !isHome },
                        label = { Text(if (isHome) "🏠 Zuhause" else "🌳 Unterwegs") }
                    )
                    FilterChip(
                        selected = forFriends,
                        onClick = { forFriends = !forFriends },
                        label = { Text(if (forFriends) "👥 Mit Freunden" else "👤 Alleine") }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(
                            Idea(
                                id = (1000..9999).random(),
                                title = title.trim(),
                                desc = if (desc.isBlank()) "Eigene Freizeit-Idee" else desc.trim(),
                                minutes = 30,
                                cost = 0,
                                isHome = isHome,
                                weather = "Egal",
                                forFriends = forFriends,
                                emoji = "✨",
                                mapsQuery = if (!isHome) title.trim() else null
                            )
                        )
                    }
                }
            ) { Text("Speichern") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Abbrechen") }
        }
    )
}
