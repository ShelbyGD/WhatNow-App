package com.whatnow.app

data class Idea(
    val id: Int,
    val title: String,
    val desc: String,
    val minutes: Int,      // 15, 30, 60, 120
    val cost: Int,         // 0, 5, 20 (in Euro)
    val isHome: Boolean,   // true = Zuhause, false = Unterwegs
    val weather: String,   // "Sonnig", "Regen", "Egal"
    val forFriends: Boolean,
    val emoji: String,
    val group: String,     // "Alle", "Kinder", "Familie", "Teens", "Erwachsene"
    val interest: String,  // "Action & Sport", "Kreativ & DIY", "Entspannung", "Essen & Genuss", "Kultur & Wissen", "Spiele & Spaß"
    val mapsQuery: String? = null
)

val ageGroups = listOf("Alle", "Kinder", "Familie", "Teens", "Erwachsene")
val interestCategories = listOf(
    "Alle", "Spiele & Spaß", "Action & Sport", "Kreativ & DIY",
    "Essen & Genuss", "Entspannung", "Kultur & Wissen"
)

val initialIdeas = listOf(
    Idea(1, "Espresso & Leute beobachten", "Setz dich für 30 Min in ein kleines Café um die Ecke.", 30, 5, false, "Egal", false, "☕", "Erwachsene", "Essen & Genuss", "Café"),
    Idea(2, "Kugel Eis auf die Hand", "Hol dir 2 Kugeln deiner Lieblingssorte und dreh eine Runde.", 30, 5, false, "Sonnig", false, "🍦", "Alle", "Essen & Genuss", "Eisdiele"),
    Idea(3, "Öffentlicher Bücherschrank", "Stöbere nach einem kostenlosen Buch zum Tauschen in der Nähe.", 30, 0, false, "Sonnig", false, "📚", "Alle", "Kultur & Wissen", "Öffentlicher Bücherschrank"),
    Idea(4, "Power-Dehnen & Mobility", "15 Minuten Stretching für Nacken und Rücken mit Musik.", 15, 0, true, "Egal", false, "🧘", "Erwachsene", "Entspannung"),
    Idea(5, "Kühlschrank-Roulette", "Koche etwas Neues nur aus den Zutaten, die gerade da sind.", 60, 0, true, "Egal", false, "🍳", "Erwachsene", "Essen & Genuss"),
    Idea(6, "Tischtennis-Duell im Park", "Schnappt euch Kellen und sucht die nächste Platte im Viertel.", 60, 0, false, "Sonnig", true, "🏓", "Teens", "Action & Sport", "Tischtennisplatte Park"),
    Idea(7, "Spontanes Kiosk-Treffen", "Holt euch ein Getränk unter 5 € und setzt euch auf eine Bank.", 60, 5, false, "Sonnig", true, "🥤", "Teens", "Essen & Genuss", "Kiosk"),
    Idea(8, "Analoger Spieleabend", "Kramt ein Brett- oder Kartenspiel raus – Handys weg!", 120, 0, true, "Regen", true, "🎲", "Familie", "Spiele & Spaß"),
    Idea(9, "15-Minuten Speed-Aufräumen", "Timer auf 15 Min: Eine Schublade komplett klarschiff machen.", 15, 0, true, "Egal", false, "⚡", "Alle", "Entspannung"),
    Idea(10, "Golden-Hour Foto-Tour", "Geh raus und mach 5 künstlerische Fotos deiner Umgebung.", 30, 0, false, "Sonnig", false, "📸", "Teens", "Kreativ & DIY", "Park Aussichtspunkt"),
    Idea(11, "Billard oder Darts", "Fordere deine Freunde auf zwei Runden Darts oder Billard heraus.", 120, 20, false, "Regen", true, "🎱", "Erwachsene", "Spiele & Spaß", "Billard Kneipe"),
    Idea(12, "Bäckerei-Snack & Parkbank", "Hol dir ein frisches Teilchen beim Bäcker und genieße die Pause.", 30, 5, false, "Sonnig", false, "🥐", "Alle", "Essen & Genuss", "Bäckerei"),
    Idea(13, "Kleiderschrank-Check", "Sortiere 5 Teile aus, die du seit einem Jahr nicht getragen hast.", 30, 0, true, "Regen", false, "👕", "Teens", "Entspannung"),
    Idea(14, "Second-Hand & Flohmarkt", "Stöbere nach coolen Vintage-Schätzen mit kleinem Budget.", 60, 5, false, "Egal", false, "🛍️", "Teens", "Kultur & Wissen", "Second Hand Laden"),
    Idea(15, "Pizza-Werkstatt zuhause", "Jeder belegt seine eigene Hälfte mit Lieblingszutaten.", 120, 20, true, "Regen", true, "🍕", "Familie", "Essen & Genuss"),
    Idea(16, "Podcast-Spaziergang", "Kopfhörer rein, neue Folge an und spontan abbiegen.", 30, 0, false, "Egal", false, "🎧", "Erwachsene", "Entspannung", "Grünanlage"),
    Idea(17, "Papierflieger-Weltmeisterschaft", "Faltet Flieger aus Papier – wer schafft die weiteste Strecke?", 30, 0, true, "Regen", true, "✈️", "Kinder", "Kreativ & DIY"),
    Idea(18, "Bouldern & Kletterhalle", "Powert euch gemeinsam an den Kletterwänden aus.", 120, 20, false, "Regen", true, "🧗", "Teens", "Action & Sport", "Boulderhalle"),
    Idea(19, "Musik-Blind-Tasting", "Jeder spielt 3 Lieblingssongs vor, die sonst niemand kennt.", 60, 0, true, "Egal", true, "🎵", "Teens", "Kultur & Wissen"),
    Idea(20, "Spontaner Kino-Besuch", "Ab ins nächste Kino ohne stundenlang Trailer zu vergleichen.", 120, 20, false, "Regen", true, "🍿", "Alle", "Kultur & Wissen", "Kino"),
    Idea(21, "15-Minuten Skizzen-Challenge", "Zeichne einen Gegenstand vor dir möglichst detailgetreu ab.", 15, 0, true, "Egal", false, "✏️", "Alle", "Kreativ & DIY"),
    Idea(22, "Hallen- oder Freibad", "Eine Stunde Bahnen ziehen oder im Wasser planschen.", 60, 5, false, "Egal", false, "🏊", "Familie", "Action & Sport", "Schwimmbad"),
    Idea(23, "Straßenbahn-Safari", "Fahr mit einer fremden Linie bis zur Endhaltestelle und zurück.", 60, 5, false, "Egal", false, "🚋", "Familie", "Kultur & Wissen", "Haltestelle"),
    Idea(24, "Handy-Galerie ausmisten", "Lösche 50 alte Screenshots und doppelte Bilder.", 15, 0, true, "Egal", false, "📱", "Alle", "Entspannung"),
    Idea(25, "Minigolf-Turnier", "Der Klassiker für sonnige Nachmittage mit Familie oder Freunden.", 120, 20, false, "Sonnig", true, "⛳", "Familie", "Spiele & Spaß", "Minigolf"),
    Idea(26, "Wohnzimmer-Picknick", "Decke auf den Boden, Snacks in die Mitte und gemütlich quatschen.", 60, 5, true, "Regen", true, "🧺", "Familie", "Essen & Genuss"),
    Idea(27, "Fahrrad-Sprint ins Grüne", "Dreh eine schnelle 30-Minuten-Runde an der frischen Luft.", 30, 0, false, "Sonnig", false, "🚲", "Alle", "Action & Sport", "Radweg Park"),
    Idea(28, "Museum oder Ausstellung", "Kultur-Input direkt in der Nähe entdecken.", 120, 20, false, "Regen", false, "🖼️", "Erwachsene", "Kultur & Wissen", "Museum"),
    Idea(29, "Fitness-Rekordversuch", "Wie viele Liegestütze oder Kniebeugen schaffst du heute?", 15, 0, true, "Egal", false, "💪", "Teens", "Action & Sport"),
    Idea(30, "Sterne schauen", "Sucht euch einen dunklen Ort und bestimmt Sternbilder.", 60, 0, false, "Sonnig", true, "🌌", "Familie", "Entspannung", "Aussichtspunkt"),
    Idea(31, "Streetfood-Test", "Probiert einen Imbiss aus, bei dem ihr noch nie wart.", 60, 20, false, "Egal", true, "🥙", "Erwachsene", "Essen & Genuss", "Streetfood Imbiss"),
    Idea(32, "Postkarte schreiben", "Schreib jemandem eine echte Karte statt nur einer Nachricht.", 15, 5, true, "Egal", false, "✉️", "Erwachsene", "Kreativ & DIY"),
    Idea(33, "Deckenburg bauen", "Mit Stühlen, Decken und Kissen eine riesige Höhle im Wohnzimmer bauen.", 60, 0, true, "Regen", true, "🏰", "Kinder", "Spiele & Spaß"),
    Idea(34, "Abenteuerspielplatz entdecken", "Testet einen neuen großen Spielplatz im Nachbarviertel.", 60, 0, false, "Sonnig", true, "🛝", "Kinder", "Action & Sport", "Abenteuerspielplatz"),
    Idea(35, "Schatzsuche im Zimmer", "Verstecke 5 Gegenstände und zeichne eine kleine Schatzkarte.", 30, 0, true, "Egal", true, "🗺️", "Kinder", "Spiele & Spaß"),
    Idea(36, "Salzteig oder Knete basteln", "Aus Mehl, Salz und Wasser coole Figuren formen.", 60, 0, true, "Regen", true, "🎨", "Kinder", "Kreativ & DIY"),
    Idea(37, "Tierpark oder Wildgehege", "Ein Ausflug zu Ziegen, Rehen und Co. in der Umgebung.", 120, 5, false, "Sonnig", true, "🦌", "Familie", "Kultur & Wissen", "Tierpark Wildpark"),
    Idea(38, "Waffeln oder Pfannkuchen backen", "Schneller Teig, duftende Küche und glückliche Gesichter.", 30, 5, true, "Regen", true, "🧇", "Familie", "Essen & Genuss"),
    Idea(39, "Stopptanz-Party", "Musik laut aufdrehen – wer wackelt, wenn die Musik stoppt, macht Grimassen!", 15, 0, true, "Egal", true, "💃", "Kinder", "Spiele & Spaß"),
    Idea(40, "Steine bemalen & verstecken", "Bemalt flache Steine bunt und legt sie beim Spazieren für Finder aus.", 60, 0, false, "Sonnig", true, "🪨", "Kinder", "Kreativ & DIY", "Park"),
    Idea(41, "Hörspiel-Höhle", "Licht dimmen, Decke über den Kopf und eine spannende Folge hören.", 30, 0, true, "Regen", false, "📻", "Kinder", "Entspannung"),
    Idea(42, "Seifenblasen-Wettbewerb", "Aus Spüli und Wasser riesige Seifenblasen vom Balkon oder im Hof pusten.", 15, 0, false, "Sonnig", true, "🫧", "Kinder", "Spiele & Spaß"),
    Idea(43, "Stadt-Land-Fluss Extreme", "Mit lustigen neuen Kategorien wie 'Ausrede', 'Pizzabelag' oder 'Superkraft'.", 30, 0, true, "Egal", true, "📝", "Familie", "Spiele & Spaß"),
    Idea(44, "Skatepark oder Inliner-Runde", "Rollen unter die Füße und ab auf glatten Asphalt.", 60, 0, false, "Sonnig", true, "🛹", "Teens", "Action & Sport", "Skatepark"),
    Idea(45, "Bücherei-Stöbern", "Gemeinsam Bilderbücher, Comics oder Games in der Stadtbibliothek ausleihen.", 60, 0, false, "Regen", true, "📖", "Familie", "Kultur & Wissen", "Stadtbibliothek"),
    Idea(46, "Botanischer Garten", "Exotische Pflanzen anschauen und kurz wie im Urlaub fühlen.", 60, 5, false, "Sonnig", false, "🌿", "Erwachsene", "Entspannung", "Botanischer Garten"),
    Idea(47, "Mocktail- & Smoothie-Bar", "Mixt aus Säften, Obst und Eiswürfeln bunte Drinks wie im Urlaub.", 30, 5, true, "Egal", true, "🍹", "Familie", "Essen & Genuss"),
    Idea(48, "Pantomime-Duell (Scharade)", "Begriffe ohne Worte erklären – wer errät die meisten in 2 Minuten?", 30, 0, true, "Egal", true, "🎭", "Familie", "Spiele & Spaß"),
    Idea(49, "Waldbaden & Zapfen sammeln", "Durchatmen unter Bäumen und Naturmaterialien für zuhause suchen.", 60, 0, false, "Sonnig", true, "🌲", "Familie", "Entspannung", "Wald"),
    Idea(50, "Wochenmarkt-Bummel", "Frisches Obst oder Käse direkt am Stand probieren und einkaufen.", 60, 20, false, "Sonnig", false, "🍎", "Erwachsene", "Essen & Genuss", "Wochenmarkt"),
    Idea(51, "Origami-Tiere falten", "Mit einer kurzen Anleitung Frösche oder Kraniche aus Papier falten.", 30, 0, true, "Regen", false, "🦢", "Kinder", "Kreativ & DIY"),
    Idea(52, "Bowling-Abend", "Sucht euch eine Bahn und werft alle Pins um.", 120, 20, false, "Regen", true, "🎳", "Familie", "Spiele & Spaß", "Bowlingbahn"),
    Idea(53, "Kerzenschein-Entspannung", "Handy in den Flugmodus, Tee kochen und 15 Minuten Ruhe genießen.", 15, 0, true, "Regen", false, "🕯️", "Erwachsene", "Entspannung"),
    Idea(54, "Upcycling-Basteln", "Aus leeren Kartons und Rollen Roboter, Burgen oder Stiftehalter bauen.", 60, 0, true, "Regen", true, "🤖", "Kinder", "Kreativ & DIY"),
    Idea(55, "Trimm-Dich-Pfad / Calisthenics", "Kostenloses Outdoor-Workout an Stangen und Stationen im Park.", 60, 0, false, "Sonnig", false, "🤸", "Teens", "Action & Sport", "Calisthenics Park"),
    Idea(56, "Familien-Quizshow", "Jeder denkt sich 5 Quizfragen aus – wer wird heute Quiz-Champion?", 30, 0, true, "Egal", true, "🏆", "Familie", "Kultur & Wissen")
)
