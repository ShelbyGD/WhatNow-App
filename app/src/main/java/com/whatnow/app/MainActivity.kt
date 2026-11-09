package com.whatnow.app

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                dynamicLightColorScheme(LocalContext.current)
            } else {
                lightColorScheme()
            }
            MaterialTheme(colorScheme = colorScheme) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    WhatNowScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatNowScreen() {
    val context = LocalContext.current
    val ideas = remember { mutableStateListOf(*initialIdeas.toTypedArray()) }
    val favorites = remember { mutableStateListOf<Int>() }

    var maxTime by remember { mutableStateOf<Int?>(null) }
    var maxCost by remember { mutableStateOf<Int?>(null) }
    var homeOnly by remember { mutableStateOf<Boolean?>(null) }
    var friendsOnly by remember { mutableStateOf<Boolean?>(null) }
    var weather by remember { mutableStateOf("Egal") }
    var showFavsOnly by remember { mutableStateOf(false) }

    var randomIdea by remember { mutableStateOf<Idea?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val openMaps: (String) -> Unit = { query ->
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(query)}"))
        context.startActivity(intent)
    }

    val filteredIdeas = ideas.filter { idea ->
        (maxTime == null || idea.minutes <= maxTime!!) &&
        (maxCost == null || idea.cost <= maxCost!!) &&
        (homeOnly == null || idea.isHome == homeOnly) &&
        (friendsOnly == null || idea.forFriends == friendsOnly) &&
        (weather == "Egal" || idea.weather == "Egal" || idea.weather == weather) &&
        (!showFavsOnly || favorites.contains(idea.id))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("WHAT NOW?", fontWeight = FontWeight.ExtraBold)
                        Text("Schluss mit Langeweile", style = MaterialTheme.typography.labelSmall)
                    }
                },
                actions = {
                    IconButton(onClick = { showFavsOnly = !showFavsOnly }) {
                        Icon(
                            imageVector = if (showFavsOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favoriten",
                            tint = if (showFavsOnly) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Eigene Idee")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (filteredIdeas.isNotEmpty()) randomIdea = filteredIdeas.random()
                    else if (ideas.isNotEmpty()) randomIdea = ideas.random()
                },
                icon = { Icon(Icons.Default.Casino, contentDescription = null) },
                text = { Text("Überrasch mich!") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // 1. Schnellkategorien (1-Klick-Presets)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = { maxTime = 30; maxCost = 5; homeOnly = null; friendsOnly = null; weather = "Egal" },
                    label = { Text("⚡ 30 Min & max. 5 €") }
                )
                AssistChip(
                    onClick = { maxCost = 0; maxTime = null; homeOnly = null; friendsOnly = null },
                    label = { Text("💸 100% Kostenlos") }
                )
                AssistChip(
                    onClick = { homeOnly = true; maxTime = null; maxCost = null },
                    label = { Text("🏠 Für Zuhause") }
                )
                AssistChip(
                    onClick = { friendsOnly = true; homeOnly = null; maxTime = null; maxCost = null },
                    label = { Text("👥 Mit Freunden") }
                )
                AssistChip(
                    onClick = { maxTime = null; maxCost = null; homeOnly = null; friendsOnly = null; weather = "Egal"; showFavsOnly = false },
                    label = { Text("🔄 Alle zeigen") }
                )
            }

            // 2. Detaillierte Filter-Leisten
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    // Zeit & Budget
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(15 to "15 Min", 30 to "30 Min", 60 to "1 Std", 120 to "2+ Std").forEach { (min, label) ->
                            FilterChip(
                                selected = maxTime == min,
                                onClick = { maxTime = if (maxTime == min) null else min },
                                label = { Text("⏱️ $label") }
                            )
                        }
                        listOf(0 to "0 €", 5 to "Max 5 €", 20 to "Max 20 €").forEach { (eur, label) ->
                            FilterChip(
                                selected = maxCost == eur,
                                onClick = { maxCost = if (maxCost == eur) null else eur },
                                label = { Text("💶 $label") }
                            )
                        }
                    }
                    // Ort, Modus & Wetter
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(selected = homeOnly == false, onClick = { homeOnly = if (homeOnly == false) null else false }, label = { Text("📍 In der Nähe") })
                        FilterChip(selected = homeOnly == true, onClick = { homeOnly = if (homeOnly == true) null else true }, label = { Text("🏠 Zuhause") })
                        FilterChip(selected = friendsOnly == false, onClick = { friendsOnly = if (friendsOnly == false) null else false }, label = { Text("👤 Alleine") })
                        FilterChip(selected = friendsOnly == true, onClick = { friendsOnly = if (friendsOnly == true) null else true }, label = { Text("👥 Freunde") })
                        FilterChip(selected = weather == "Sonnig", onClick = { weather = if (weather == "Sonnig") "Egal" else "Sonnig" }, label = { Text("☀️ Sonnig") })
                        FilterChip(selected = weather == "Regen", onClick = { weather = if (weather == "Regen") "Egal" else "Regen" }, label = { Text("🌧️ Regen") })
                    }
                }
            }

            Text(
                text = "${filteredIdeas.size} passende Aktivitäten gefunden",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp)
            )

            // 3. Ergebnis-Liste
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredIdeas, key = { it.id }) { idea ->
                    IdeaCard(
                        idea = idea,
                        isFav = favorites.contains(idea.id),
                        onToggleFav = {
                            if (favorites.contains(idea.id)) favorites.remove(idea.id) else favorites.add(idea.id)
                        },
                        onOpenMaps = openMaps
                    )
                }
            }
        }
    }

    if (randomIdea != null) {
        RandomIdeaDialog(
            idea = randomIdea!!,
            onDismiss = { randomIdea = null },
            onReroll = { if (filteredIdeas.isNotEmpty()) randomIdea = filteredIdeas.random() },
            onOpenMaps = openMaps
        )
    }

    if (showAddDialog) {
        AddIdeaDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { newIdea -> ideas.add(0, newIdea); showAddDialog = false }
        )
    }
}
