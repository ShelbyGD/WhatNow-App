package com.whatnow.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.whatnow.app.ui.theme.WhatNowTheme

class MainActivity : ComponentActivity() {
    private lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shakeDetector = ShakeDetector(this) {
            // Bei Schütteln des Handys wird hier eine Zufallsidee ausgelöst!
        }
        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            WhatNowTheme(darkTheme = darkTheme) {
                MainScreen(onToggleTheme = { darkTheme = !darkTheme })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onToggleTheme: () -> Unit) {
    val context = LocalContext.current
    val list = remember { ActivityRepository.getInitialActivities() }
    var favorites by remember { mutableStateOf(setOf<Int>()) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WHAT NOW? – Schluss mit Langeweile") },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(Icons.Default.Settings, contentDescription = "Theme wechseln")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            // Tageszeit-Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            ) {
                Text(text = ActivityRepository.getTageszeitHinweis(), modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium)
            }

            // Suchfeld
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Aktivität suchen...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Mystery Card
            MysteryCard(onReveal = {})

            Spacer(modifier = Modifier.height(8.dp))

            // Liste der Aktivitäten
            val filtered = list.filter { it.titel.contains(searchQuery, ignoreCase = true) }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filtered) { item ->
                    val isFav = favorites.contains(item.id)
                    AktivitaetKarte(
                        item = item,
                        isFavorite = isFav,
                        onFavoriteClick = {
                            favorites = if (isFav) favorites - item.id else favorites + item.id
                        },
                        onMapClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(item.mapsQuery)}"))
                            context.startActivity(intent)
                        },
                        onShareClick = {
                            ShakeDetector.shareActivity(context, item.titel, item.dauer, item.budget, item.mapsQuery)
                        }
                    )
                }
            }
        }
    }
}
