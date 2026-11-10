package com.whatnow.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AktivitaetKarte(
    item: FreizeitAktivitaet,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onMapClick: () -> Unit,
    onShareClick: () -> Unit,
    isCompleted: Boolean = false,
    onCompletedClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) {
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.emoji,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = item.titel,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${item.kategorie} • ${item.interesse}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onCompletedClick) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Als erledigt markieren",
                            tint = if (isCompleted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorit",
                            tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (item.beschreibung.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.beschreibung,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text("⏱️ ${item.dauer}") }
                )
                AssistChip(
                    onClick = {},
                    label = { Text("💶 ${item.budget}") }
                )
                AssistChip(
                    onClick = {},
                    label = { Text("👥 ${item.zielgruppe}") }
                )
                AssistChip(
                    onClick = {},
                    label = { Text("⚡ ${item.energieLevel}") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (item.ort != "Zuhause") {
                    Button(
                        onClick = onMapClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("In der Nähe")
                    }
                }
                OutlinedButton(
                    onClick = onShareClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Einladen")
                }
            }
        }
    }
}

@Composable
fun MysteryCard(
    onReveal: () -> Unit = {},
    onOpenMaps: (String) -> Unit = {}
) {
    val challenges = remember { ActivityRepository.getMysteryChallenges() }
    var currentIndex by remember { mutableStateOf(0) }
    var revealed by remember { mutableStateOf(false) }
    val current = challenges[currentIndex % challenges.size]

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎁 Blind-Date Freizeit-Challenge",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                if (revealed) {
                    IconButton(
                        onClick = {
                            currentIndex = (currentIndex + 1) % challenges.size
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Nächste Challenge",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            if (!revealed) {
                Text(
                    text = "Lass dich überraschen! Rahmen: ${current.rahmen}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        revealed = true
                        onReveal()
                    },
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Challenge annehmen & aufdecken!")
                }
            } else {
                AnimatedVisibility(visible = revealed) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${current.titel} (${current.rahmen})",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = current.aufgabe,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { currentIndex = (currentIndex + 1) % challenges.size },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Casino, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Andere Challenge")
                            }
                            Button(
                                onClick = { onOpenMaps(current.mapsQuery) },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ort suchen")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    selectedDauer: String,
    onDauerChange: (String) -> Unit,
    selectedBudget: String,
    onBudgetChange: (String) -> Unit,
    selectedWetter: String,
    onWetterChange: (String) -> Unit,
    selectedZielgruppe: String,
    onZielgruppeChange: (String) -> Unit,
    selectedEnergie: String,
    onEnergieChange: (String) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎛️ Smarte Filter",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onReset) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Zurücksetzen")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            FilterSectionTitle("⏱️ Zeitfenster")
            FilterChipRow(ActivityRepository.dauern, selectedDauer, onDauerChange)

            FilterSectionTitle("💶 Budget")
            FilterChipRow(ActivityRepository.budgets, selectedBudget, onBudgetChange)

            FilterSectionTitle("🌦️ Wetter")
            FilterChipRow(ActivityRepository.wetterOptionen, selectedWetter, onWetterChange)

            FilterSectionTitle("👥 Zielgruppe")
            FilterChipRow(ActivityRepository.zielgruppen, selectedZielgruppe, onZielgruppeChange)

            FilterSectionTitle("⚡ Energie-Level")
            FilterChipRow(ActivityRepository.energieLevels, selectedEnergie, onEnergieChange)

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Ergebnisse anzeigen")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun FilterSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun FilterChipRow(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(option) }
            )
        }
    }
}

@Composable
fun ShakeResultDialog(
    item: FreizeitAktivitaet,
    onDismiss: () -> Unit,
    onReroll: () -> Unit,
    onMapClick: () -> Unit,
    onShareClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = item.emoji, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Zufalls-Treffer!",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.titel,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = item.beschreibung, style = MaterialTheme.typography.bodyMedium)
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = "⏱️ ${item.dauer}  •  💶 ${item.budget}  •  📍 ${item.ort}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "👥 ${item.zielgruppe}  •  ⚡ Energie: ${item.energieLevel}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onReroll) {
                    Icon(Icons.Default.Casino, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Neu würfeln")
                }
                Button(onClick = onMapClick) {
                    Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Maps")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onShareClick) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Teilen")
            }
        }
    )
}

@Composable
fun AddActivityDialog(
    onDismiss: () -> Unit,
    onAdd: (FreizeitAktivitaet) -> Unit
) {
    var titel by remember { mutableStateOf("") }
    var beschreibung by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("💡") }
    var kategorie by remember { mutableStateOf("Draußen") }
    var dauer by remember { mutableStateOf("1-2 Std") }
    var budget by remember { mutableStateOf("Kostenlos") }
    var ort by remember { mutableStateOf("In der Nähe") }
    var mapsQuery by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("💡 Eigene Aktivität speichern", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = emoji,
                        onValueChange = { if (it.length <= 4) emoji = it },
                        label = { Text("Emoji") },
                        modifier = Modifier.width(78.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = titel,
                        onValueChange = { titel = it },
                        label = { Text("Titel der Idee *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                OutlinedTextField(
                    value = beschreibung,
                    onValueChange = { beschreibung = it },
                    label = { Text("Kurzbeschreibung") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
                OutlinedTextField(
                    value = mapsQuery,
                    onValueChange = { mapsQuery = it },
                    label = { Text("Google Maps Suchbegriff (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Text("Kategorie & Rahmen:", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Draußen", "Zuhause", "Freunde", "Kultur & City").forEach { kat ->
                        FilterChip(
                            selected = kategorie == kat,
                            onClick = {
                                kategorie = kat
                                ort = if (kat == "Zuhause") "Zuhause" else "In der Nähe"
                            },
                            label = { Text(kat) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("15 Min", "30 Min", "1-2 Std", "Halber Tag").forEach { d ->
                        FilterChip(
                            selected = dauer == d,
                            onClick = { dauer = d },
                            label = { Text(d) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Kostenlos", "Max 5 €", "Max 20 €").forEach { b ->
                        FilterChip(
                            selected = budget == b,
                            onClick = { budget = b },
                            label = { Text(b) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titel.isNotBlank()) {
                        onAdd(
                            FreizeitAktivitaet(
                                id = (System.currentTimeMillis() % 100000).toInt() + 1000,
                                titel = titel.trim(),
                                kategorie = kategorie,
                                dauer = dauer,
                                budget = budget,
                                wetter = "Egal",
                                ort = ort,
                                zielgruppe = "Alle",
                                interesse = "Action",
                                mapsQuery = mapsQuery.ifBlank { titel.trim() },
                                emoji = emoji.ifBlank { "💡" },
                                beschreibung = beschreibung.ifBlank { "Eigene gespeicherte Freizeit-Idee." },
                                energieLevel = "Mittel"
                            )
                        )
                    }
                },
                enabled = titel.isNotBlank()
            ) {
                Text("Speichern")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        }
    )
}

@Composable
fun HelpDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("🧭 So funktioniert WHAT NOW?", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("📱 Shake-to-Decide:\nSchüttle einfach dein Smartphone (oder tippe unten rechts auf 'Überrasch mich!'), um sofort eine passende Aktivität zu würfeln.")
                Text("🎁 Mystery-Challenge:\nNimm spontane Blind-Date-Challenges an und würfle bei Bedarf direkt die nächste Aufgabe.")
                Text("✅ Bucket-List & Favoriten:\nMarkiere Ideen mit ❤️ als Favorit oder mit ✔️ als erledigt – dein Fortschritt bleibt gespeichert.")
                Text("🔄 Direkt-Updates:\nNeue APK-Versionen sind mit demselben Schlüssel signiert und können direkt über bestehende Versionen installiert werden.")
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Alles klar!")
            }
        }
    )
}
