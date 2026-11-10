package com.whatnow.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF818CF8),
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1E1B4B),
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFF38BDF8),
    onSecondary = Color(0xFF082F49),
    secondaryContainer = Color(0xFF0C4A6E),
    onSecondaryContainer = Color(0xFFE0F2FE),
    tertiary = Color(0xFFFBBF24),
    onTertiary = Color(0xFF451A03),
    tertiaryContainer = Color(0xFF3B2106),
    onTertiaryContainer = Color(0xFFFEF3C7),
    background = Color(0xFF0B0F19),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF4F46E5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = Color(0xFF1E1B4B),
    secondary = Color(0xFF0284C7),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0C4A6E),
    tertiary = Color(0xFFD97706),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF451A03),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun WhatNowTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette
    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}

class MainActivity : ComponentActivity() {
    private lateinit var shakeDetector: ShakeDetector
    private var shakeEventCount by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefsManager = PreferencesManager(this)

        shakeDetector = ShakeDetector(this) {
            shakeEventCount++
        }.apply {
            isEnabled = prefsManager.isShakeEnabled()
        }

        setContent {
            var darkTheme by remember { mutableStateOf(prefsManager.isDarkMode(defaultDark = true)) }
            var shakeEnabled by remember { mutableStateOf(prefsManager.isShakeEnabled()) }

            WhatNowTheme(darkTheme = darkTheme) {
                MainScreen(
                    prefsManager = prefsManager,
                    isDarkTheme = darkTheme,
                    onToggleTheme = { enabled ->
                        darkTheme = enabled
                        prefsManager.setDarkMode(enabled)
                    },
                    isShakeEnabled = shakeEnabled,
                    onToggleShake = { enabled ->
                        shakeEnabled = enabled
                        shakeDetector.isEnabled = enabled
                        prefsManager.setShakeEnabled(enabled)
                    },
                    shakeTrigger = shakeEventCount
                )
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

private enum class ViewMode {
    ALL, SMART_TIME, FAVORITES, COMPLETED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    prefsManager: PreferencesManager,
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    isShakeEnabled: Boolean,
    onToggleShake: (Boolean) -> Unit,
    shakeTrigger: Int
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val customActivities = remember { mutableStateListOf(*prefsManager.getCustomActivities().toTypedArray()) }
    val allActivities by remember {
        derivedStateOf { customActivities + ActivityRepository.getInitialActivities() }
    }

    var favorites by remember { mutableStateOf(prefsManager.getFavoriteIds()) }
    var completed by remember { mutableStateOf(prefsManager.getCompletedIds()) }

    var searchQuery by remember { mutableStateOf("") }
    var viewMode by remember { mutableStateOf(ViewMode.ALL) }
    var selectedKategorie by remember { mutableStateOf("Alle") }
    var selectedDauer by remember { mutableStateOf("Alle") }
    var selectedBudget by remember { mutableStateOf("Alle") }
    var selectedWetter by remember { mutableStateOf("Egal") }
    var selectedZielgruppe by remember { mutableStateOf("Alle") }
    var selectedEnergie by remember { mutableStateOf("Alle") }

    var showFilterSheet by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showMysteryCard by remember { mutableStateOf(true) }
    var randomPickedItem by remember { mutableStateOf<FreizeitAktivitaet?>(null) }

    val resetAllFilters: () -> Unit = {
        searchQuery = ""
        viewMode = ViewMode.ALL
        selectedKategorie = "Alle"
        selectedDauer = "Alle"
        selectedBudget = "Alle"
        selectedWetter = "Egal"
        selectedZielgruppe = "Alle"
        selectedEnergie = "Alle"
    }

    val filteredList = allActivities.filter { item ->
        val matchesSearch = searchQuery.isBlank() ||
            item.titel.contains(searchQuery, ignoreCase = true) ||
            item.beschreibung.contains(searchQuery, ignoreCase = true) ||
            item.interesse.contains(searchQuery, ignoreCase = true)

        val matchesViewMode = when (viewMode) {
            ViewMode.ALL -> true
            ViewMode.SMART_TIME -> ActivityRepository.isRecommendedForCurrentTime(item)
            ViewMode.FAVORITES -> favorites.contains(item.id)
            ViewMode.COMPLETED -> completed.contains(item.id)
        }

        val matchesKategorie = selectedKategorie == "Alle" || item.kategorie == selectedKategorie
        val matchesDauer = selectedDauer == "Alle" || item.dauer == selectedDauer
        val matchesBudget = selectedBudget == "Alle" || item.budget == selectedBudget
        val matchesWetter = selectedWetter == "Egal" || item.wetter == "Egal" || item.wetter == selectedWetter
        val matchesZielgruppe = selectedZielgruppe == "Alle" || item.zielgruppe == "Alle" || item.zielgruppe == selectedZielgruppe
        val matchesEnergie = selectedEnergie == "Alle" || item.energieLevel == selectedEnergie

        matchesSearch && matchesViewMode && matchesKategorie && matchesDauer &&
            matchesBudget && matchesWetter && matchesZielgruppe && matchesEnergie
    }

    val pickRandomIdea: () -> Unit = {
        val pool = if (filteredList.isNotEmpty()) filteredList else allActivities
        if (pool.isNotEmpty()) {
            randomPickedItem = pool.random()
        }
    }

    LaunchedEffect(shakeTrigger) {
        if (shakeTrigger > 0) {
            pickRandomIdea()
            Toast.makeText(context, "🎲 Shake-to-Decide: Neue Idee gewürfelt!", Toast.LENGTH_SHORT).show()
        }
    }

    val activeFilterCount = listOf(
        selectedKategorie != "Alle",
        selectedDauer != "Alle",
        selectedBudget != "Alle",
        selectedWetter != "Egal",
        selectedZielgruppe != "Alle",
        selectedEnergie != "Alle"
    ).count { it }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(320.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        // Moderner Header im Hamburger-Menü
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(20.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_launcher),
                                        contentDescription = "App Icon",
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "WHAT NOW?",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "Smarter Freizeit-Kompass",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                val progress = if (allActivities.isNotEmpty()) {
                                    completed.size.toFloat() / allActivities.size.toFloat()
                                } else 0f

                                Text(
                                    text = "Bucket-List Fortschritt: ${completed.size} / ${allActivities.size} erledigt",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "ANSICHTEN",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                        )

                        NavigationDrawerItem(
                            icon = { Icon(Icons.Default.Explore, contentDescription = null) },
                            label = { Text("Alle Ideen entdecken (${allActivities.size})") },
                            selected = viewMode == ViewMode.ALL,
                            onClick = {
                                viewMode = ViewMode.ALL
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        NavigationDrawerItem(
                            icon = { Icon(Icons.Default.WbSunny, contentDescription = null) },
                            label = { Text("Passend zur Tageszeit") },
                            selected = viewMode == ViewMode.SMART_TIME,
                            onClick = {
                                viewMode = ViewMode.SMART_TIME
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            label = { Text("Meine Favoriten (${favorites.size})") },
                            selected = viewMode == ViewMode.FAVORITES,
                            onClick = {
                                viewMode = ViewMode.FAVORITES
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            },
                            label = { Text("Erledigte Aktivitäten (${completed.size})") },
                            selected = viewMode == ViewMode.COMPLETED,
                            onClick = {
                                viewMode = ViewMode.COMPLETED
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "SMARTE TOOLS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                        )

                        NavigationDrawerItem(
                            icon = { Icon(Icons.Default.Casino, contentDescription = null) },
                            label = { Text("Zufalls-Generator starten") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                pickRandomIdea()
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        NavigationDrawerItem(
                            icon = { Icon(Icons.Default.AddCircleOutline, contentDescription = null) },
                            label = { Text("Eigene Idee hinzufügen") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                showAddDialog = true
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        NavigationDrawerItem(
                            icon = { Icon(Icons.Default.Tune, contentDescription = null) },
                            label = { Text("Smart-Filter öffnen") },
                            badge = {
                                if (activeFilterCount > 0) {
                                    Badge { Text("$activeFilterCount") }
                                }
                            },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                showFilterSheet = true
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        NavigationDrawerItem(
                            icon = { Icon(Icons.Default.RestartAlt, contentDescription = null) },
                            label = { Text("Filter & Suche zurücksetzen") },
                            selected = false,
                            onClick = {
                                resetAllFilters()
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "DARSTELLUNG & EINSTELLUNGEN",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                        )

                        // Dark / Light Modus als Menüpunkt
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(if (isDarkTheme) "Dunkles Design aktiv" else "Helles Design aktiv")
                            },
                            badge = {
                                Switch(
                                    checked = isDarkTheme,
                                    onCheckedChange = { onToggleTheme(it) }
                                )
                            },
                            selected = false,
                            onClick = { onToggleTheme(!isDarkTheme) },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        // Shake-Sensor Schalter im Menü
                        NavigationDrawerItem(
                            icon = { Icon(Icons.Default.Vibration, contentDescription = null) },
                            label = { Text("Shake-to-Decide Sensor") },
                            badge = {
                                Switch(
                                    checked = isShakeEnabled,
                                    onCheckedChange = { onToggleShake(it) }
                                )
                            },
                            selected = false,
                            onClick = { onToggleShake(!isShakeEnabled) },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        // Mystery-Card ein-/ausblenden
                        NavigationDrawerItem(
                            icon = { Icon(Icons.Default.CardGiftcard, contentDescription = null) },
                            label = { Text("Blind-Date Challenge zeigen") },
                            badge = {
                                Switch(
                                    checked = showMysteryCard,
                                    onCheckedChange = { showMysteryCard = it }
                                )
                            },
                            selected = false,
                            onClick = { showMysteryCard = !showMysteryCard },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        NavigationDrawerItem(
                            icon = { Icon(Icons.Default.HelpOutline, contentDescription = null) },
                            label = { Text("Hilfe & Funktionsübersicht") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                showHelpDialog = true
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }

                    // Dezente Versionsnummer ganz unten im Menü
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.padding(bottom = 10.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                        )
                        Text(
                            text = "WHAT NOW? • Version ${ActivityRepository.APP_VERSION}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
                        )
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
                            Icon(Icons.Default.Menu, contentDescription = "Menü öffnen")
                        }
                    },
                    title = {
                        Column {
                            Text(
                                text = "WHAT NOW?",
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = when (viewMode) {
                                    ViewMode.ALL -> "${filteredList.size} Ideen gegen Langeweile"
                                    ViewMode.SMART_TIME -> "Passend zur aktuellen Tageszeit"
                                    ViewMode.FAVORITES -> "Deine gespeicherten Favoriten"
                                    ViewMode.COMPLETED -> "Deine erledigte Bucket-List"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    actions = {
                        // Kein Zahnrad-Icon mehr oben rechts; stattdessen direkter Filter- und Hinzufügen-Button
                        BadgedBox(
                            badge = {
                                if (activeFilterCount > 0) {
                                    Badge { Text("$activeFilterCount") }
                                }
                            }
                        ) {
                            IconButton(onClick = { showFilterSheet = true }) {
                                Icon(Icons.Default.Tune, contentDescription = "Filter")
                            }
                        }
                        IconButton(onClick = { showAddDialog = true }) {
                            Icon(Icons.Default.AddCircleOutline, contentDescription = "Eigene Idee")
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = pickRandomIdea,
                    icon = { Icon(Icons.Default.Casino, contentDescription = null) },
                    text = { Text("Überrasch mich!") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 88.dp
                )
            ) {
                // Tageszeit-Banner mit Schnell-Umschalter
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = ActivityRepository.getTageszeitTitel(),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = ActivityRepository.getTageszeitHinweis(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            AssistChip(
                                onClick = {
                                    viewMode = if (viewMode == ViewMode.SMART_TIME) ViewMode.ALL else ViewMode.SMART_TIME
                                },
                                label = {
                                    Text(if (viewMode == ViewMode.SMART_TIME) "Alle zeigen" else "Filtern")
                                }
                            )
                        }
                    }
                }

                // Suchfeld
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Aktivität, Thema oder Ort suchen...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Suche löschen")
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Horizontale Kategorie-Chips + Schnellfilter
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActivityRepository.kategorien.forEach { kat ->
                            FilterChip(
                                selected = selectedKategorie == kat,
                                onClick = { selectedKategorie = kat },
                                label = { Text(kat) }
                            )
                        }
                        FilterChip(
                            selected = selectedBudget == "Kostenlos",
                            onClick = {
                                selectedBudget = if (selectedBudget == "Kostenlos") "Alle" else "Kostenlos"
                            },
                            label = { Text("💸 Kostenlos") }
                        )
                        FilterChip(
                            selected = selectedDauer == "15 Min" || selectedDauer == "30 Min",
                            onClick = {
                                selectedDauer = if (selectedDauer == "30 Min") "Alle" else "30 Min"
                            },
                            label = { Text("⚡ 30 Min") }
                        )
                    }
                }

                // Mystery Card
                if (showMysteryCard && viewMode == ViewMode.ALL && searchQuery.isBlank()) {
                    item {
                        MysteryCard(
                            onReveal = {},
                            onOpenMaps = { query -> ShakeDetector.openMaps(context, query) }
                        )
                    }
                }

                // Leerer Zustand, falls keine Treffer
                if (filteredList.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🔍 Keine passende Aktivität gefunden", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Setze die Filter zurück oder füge deine eigene Freizeit-Idee hinzu.")
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(onClick = resetAllFilters) {
                                    Text("Alle Filter zurücksetzen")
                                }
                            }
                        }
                    }
                }

                // Liste der Aktivitäten
                items(filteredList, key = { it.id }) { item ->
                    val isFav = favorites.contains(item.id)
                    val isDone = completed.contains(item.id)
                    AktivitaetKarte(
                        item = item,
                        isFavorite = isFav,
                        onFavoriteClick = {
                            favorites = if (isFav) favorites - item.id else favorites + item.id
                            prefsManager.saveFavoriteIds(favorites)
                        },
                        isCompleted = isDone,
                        onCompletedClick = {
                            completed = if (isDone) completed - item.id else completed + item.id
                            prefsManager.saveCompletedIds(completed)
                        },
                        onMapClick = {
                            ShakeDetector.openMaps(context, item.mapsQuery)
                        },
                        onShareClick = {
                            ShakeDetector.shareActivity(
                                context = context,
                                title = item.titel,
                                duration = item.dauer,
                                budget = item.budget,
                                mapsQuery = item.mapsQuery
                            )
                        }
                    )
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            selectedDauer = selectedDauer,
            onDauerChange = { selectedDauer = it },
            selectedBudget = selectedBudget,
            onBudgetChange = { selectedBudget = it },
            selectedWetter = selectedWetter,
            onWetterChange = { selectedWetter = it },
            selectedZielgruppe = selectedZielgruppe,
            onZielgruppeChange = { selectedZielgruppe = it },
            selectedEnergie = selectedEnergie,
            onEnergieChange = { selectedEnergie = it },
            onReset = resetAllFilters,
            onDismiss = { showFilterSheet = false }
        )
    }

    if (randomPickedItem != null) {
        val current = randomPickedItem!!
        ShakeResultDialog(
            item = current,
            onDismiss = { randomPickedItem = null },
            onReroll = pickRandomIdea,
            onMapClick = {
                ShakeDetector.openMaps(context, current.mapsQuery)
            },
            onShareClick = {
                ShakeDetector.shareActivity(
                    context = context,
                    title = current.titel,
                    duration = current.dauer,
                    budget = current.budget,
                    mapsQuery = current.mapsQuery
                )
            }
        )
    }

    if (showAddDialog) {
        AddActivityDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { newItem ->
                customActivities.add(0, newItem)
                prefsManager.saveCustomActivities(customActivities.toList())
                showAddDialog = false
            }
        )
    }

    if (showHelpDialog) {
        HelpDialog(onDismiss = { showHelpDialog = false })
    }
}
