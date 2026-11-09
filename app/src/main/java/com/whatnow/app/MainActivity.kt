package com.whatnow.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val prefs = remember { getSharedPreferences("whatnow_prefs", Context.MODE_PRIVATE) }
            var isDark by remember { mutableStateOf(prefs.getBoolean("dark_mode", false)) }
            val ctx = LocalContext.current
            val scheme = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDark -> dynamicDarkColorScheme(ctx)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !isDark -> dynamicLightColorScheme(ctx)
                isDark -> darkColorScheme()
                else -> lightColorScheme()
            }
            MaterialTheme(colorScheme = scheme) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    WhatNowScreen(
                        isDark = isDark,
                        onToggleDark = { isDark = it; prefs.edit().putBoolean("dark_mode", it).apply() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatNowScreen(isDark: Boolean, onToggleDark: (Boolean) -> Unit) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("whatnow_prefs", Context.MODE_PRIVATE) }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val ideas = remember { mutableStateListOf(*initialIdeas.toTypedArray()) }
    val favorites = remember {
        val saved = prefs.getStringSet("fav_ids", emptySet())?.mapNotNull { it.toIntOrNull() } ?: emptyList()
        mutableStateListOf(*saved.toTypedArray())
    }
    fun toggleFav(id: Int) {
        if (favorites.contains(id)) favorites.remove(id) else favorites.add(id)
        prefs.edit().putStringSet("fav_ids", favorites.map { it.toString() }.toSet()).apply()
    }

    var tabIndex by remember { mutableStateOf(0) }
    var maxTime by remember { mutableStateOf<Int?>(null) }
    var maxCost by remember { mutableStateOf<Int?>(null) }
    var homeOnly by remember { mutableStateOf<Boolean?>(null) }
    var friendsOnly by remember { mutableStateOf<Boolean?>(null) }
    var weather by remember { mutableStateOf("Egal") }
    var selGroup by remember { mutableStateOf("Alle") }
    var selInterest by remember { mutableStateOf("Alle") }
    var showFavsOnly by remember { mutableStateOf(false) }

    var favDrawerExpanded by remember { mutableStateOf(true) }
    var randomIdea by remember { mutableStateOf<Idea?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(prefs.getBoolean("first_start", true)) }

    val openMaps: (String) -> Unit = { query ->
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(query)}")))
    }

    val filteredIdeas = ideas.filter { idea ->
        (maxTime == null || idea.minutes <= maxTime!!) &&
        (maxCost == null || idea.cost <= maxCost!!) &&
        (homeOnly == null || idea.isHome == homeOnly) &&
        (friendsOnly == null || idea.forFriends == friendsOnly) &&
        (weather == "Egal" || idea.weather == "Egal" || idea.weather == weather) &&
        (selGroup == "Alle" || idea.group == "Alle" || idea.group == selGroup) &&
        (selInterest == "Alle" || idea.interest == selInterest) &&
        (!showFavsOnly || favorites.contains(idea.id))
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🧭 WHAT NOW? 2.0", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                    Text("Menü & Favoriten", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(if (isDark) "🌙 Dunkles Design aktiv" else "☀️ Helles Design aktiv") },
                    selected = false,
                    onClick = { onToggleDark(!isDark) },
                    badge = { Switch(checked = isDark, onCheckedChange = onToggleDark) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.HelpOutline, null) },
                    label = { Text("Interaktive Hilfe starten") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; showHelpDialog = true }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Favorite, null, tint = MaterialTheme.colorScheme.error) },
                    label = { Text("Meine Favoriten (${favorites.size})") },
                    badge = { Icon(if (favDrawerExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null) },
                    selected = favDrawerExpanded,
                    onClick = { favDrawerExpanded = !favDrawerExpanded }
                )
                if (favDrawerExpanded) {
                    val favItems = ideas.filter { favorites.contains(it.id) }
                    if (favItems.isEmpty()) {
                        Text("Noch keine Favoriten ❤️ markiert.", modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp), style = MaterialTheme.typography.bodySmall)
                    } else {
                        LazyColumn(modifier = Modifier.heightIn(max = 260.dp).padding(horizontal = 16.dp)) {
                            items(favItems, key = { it.id }) { fav ->
                                ListItem(
                                    headlineContent = { Text("${fav.emoji} ${fav.title}", style = MaterialTheme.typography.bodyMedium) },
                                    supportingContent = { Text("${fav.minutes} Min • ${fav.group}") },
                                    trailingContent = {
                                        IconButton(onClick = { toggleFav(fav.id) }) { Icon(Icons.Default.Delete, null) }
                                    },
                                    modifier = Modifier.clickable { randomIdea = fav; scope.launch { drawerState.close() } }
                                )
                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menü")
                        }
                    },
                    title = {
                        Column {
                            Text("WHAT NOW?", fontWeight = FontWeight.ExtraBold)
                            Text("55+ Ideen gegen Langeweile", style = MaterialTheme.typography.labelSmall)
                        }
                    },
                    actions = {
                        IconButton(onClick = { onToggleDark(!isDark) }) {
                            Icon(if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode, "Theme")
                        }
                        IconButton(onClick = { showHelpDialog = true }) {
                            Icon(Icons.Default.HelpOutline, "Hilfe")
                        }
                        IconButton(onClick = { showAddDialog = true }) {
                            Icon(Icons.Default.AddCircle, "Eigene Idee")
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { if (filteredIdeas.isNotEmpty()) randomIdea = filteredIdeas.random() else randomIdea = ideas.random() },
                    icon = { Icon(Icons.Default.Casino, null) },
                    text = { Text("Überrasch mich!") }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                TabRow(selectedTabIndex = tabIndex) {
                    Tab(selected = tabIndex == 0, onClick = { tabIndex = 0 }, text = { Text("⚡ Schnell-Filter") })
                    Tab(selected = tabIndex == 1, onClick = { tabIndex = 1 }, text = { Text("👨‍👩‍👧‍👦 Alter & Interessen") })
                }

                if (tabIndex == 0) {
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(onClick = { maxTime = 30; maxCost = 5 }, label = { Text("⚡ 30 Min & max 5 €") })
                        AssistChip(onClick = { selGroup = "Kinder" }, label = { Text("🎈 Für Kinder") })
                        AssistChip(onClick = { selGroup = "Familie" }, label = { Text("👨‍👩‍👧‍👦 Familien-Zeit") })
                        AssistChip(onClick = { maxCost = 0 }, label = { Text("💸 100% Kostenlos") })
                        AssistChip(onClick = { maxTime = null; maxCost = null; homeOnly = null; friendsOnly = null; weather = "Egal"; selGroup = "Alle"; selInterest = "Alle"; showFavsOnly = false }, label = { Text("🔄 Reset") })
                    }
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(15 to "15 Min", 30 to "30 Min", 60 to "1 Std", 120 to "2+ Std").forEach { (m, l) ->
                                    FilterChip(selected = maxTime == m, onClick = { maxTime = if (maxTime == m) null else m }, label = { Text("⏱️ $l") })
                                }
                                listOf(0 to "0 €", 5 to "Max 5 €", 20 to "Max 20 €").forEach { (c, l) ->
                                    FilterChip(selected = maxCost == c, onClick = { maxCost = if (maxCost == c) null else c }, label = { Text("💶 $l") })
                                }
                            }
                            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                FilterChip(selected = homeOnly == false, onClick = { homeOnly = if (homeOnly == false) null else false }, label = { Text("📍 Unterwegs") })
                                FilterChip(selected = homeOnly == true, onClick = { homeOnly = if (homeOnly == true) null else true }, label = { Text("🏠 Zuhause") })
                                FilterChip(selected = friendsOnly == true, onClick = { friendsOnly = if (friendsOnly == true) null else true }, label = { Text("👥 Freunde/Gruppe") })
                                FilterChip(selected = weather == "Sonnig", onClick = { weather = if (weather == "Sonnig") "Egal" else "Sonnig" }, label = { Text("☀️ Sonnig") })
                                FilterChip(selected = weather == "Regen", onClick = { weather = if (weather == "Regen") "Egal" else "Regen" }, label = { Text("🌧️ Regen") })
                            }
                        }
                    }
                } else {
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("👥 Wer macht mit? (Altersgruppe / Familie)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                ageGroups.forEach { g ->
                                    FilterChip(selected = selGroup == g, onClick = { selGroup = g }, label = { Text(g) })
                                }
                            }
                            Text("🏷️ Worauf habt ihr Lust? (Interessen)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                interestCategories.forEach { cat ->
                                    FilterChip(selected = selInterest == cat, onClick = { selInterest = cat }, label = { Text(cat) })
                                }
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${filteredIdeas.size} passende Ideen (${selGroup} • ${selInterest})", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                }

                LazyColumn(contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 88.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredIdeas, key = { it.id }) { idea ->
                        IdeaCard(idea = idea, isFav = favorites.contains(idea.id), onToggleFav = { toggleFav(idea.id) }, onOpenMaps = openMaps)
                    }
                }
            }
        }
    }

    if (showHelpDialog) {
        InteractiveHelpDialog(onDismiss = { showHelpDialog = false; prefs.edit().putBoolean("first_start", false).apply() })
    }
    if (randomIdea != null) {
        RandomIdeaDialog(idea = randomIdea!!, onDismiss = { randomIdea = null }, onReroll = { if (filteredIdeas.isNotEmpty()) randomIdea = filteredIdeas.random() }, onOpenMaps = openMaps)
    }
    if (showAddDialog) {
        AddIdeaDialog(onDismiss = { showAddDialog = false }, onAdd = { ideas.add(0, it); showAddDialog = false })
    }
}
