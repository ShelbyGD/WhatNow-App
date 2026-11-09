package com.whatnow.app

data class Idea(
    val id: Int,
    val title: String,
    val desc: String,
    val minutes: Int,      // 15, 30, 60, 120, 240
    val cost: Int,         // 0, 5, 20, 50 (in Euro)
    val isHome: Boolean,   // true = Zuhause, false = Unterwegs/Nähe
    val weather: String,   // "Sonnig", "Regen", "Egal"
    val forFriends: Boolean,
    val emoji: String,
    val mapsQuery: String? = null
)

val initialIdeas = listOf(
    Idea(1, "Espresso & Leute beobachten", "Setz dich für eine halbe Stunde in ein kleines Café um die Ecke.", 30, 5, false, "Egal", false, "☕", "Café"),
    Idea(2, "Kugel Eis auf die Hand", "Hol dir 2 Kugeln deiner Lieblingssorte und dreh eine kleine Runde.", 30, 5, false, "Sonnig", false, "🍦", "Eisdiele"),
    Idea(3, "Öffentlichen Bücherschrank checken", "Schau, ob du ein spannendes Buch zum Tauschen in deiner Nähe findest.", 30, 0, false, "Sonnig", false, "📚", "Öffentlicher Bücherschrank"),
    Idea(4, "Power-Dehnen & Mobility", "15 Minuten Stretching für Nacken und Rücken mit deiner Lieblingsmusik.", 15, 0, true, "Egal", false, "🧘"),
    Idea(5, "Kühlschrank-Roulette kochen", "Koche etwas Neues nur aus den Zutaten, die du gerade zuhause hast.", 60, 0, true, "Egal", false, "🍳"),
    Idea(6, "Tischtennis-Duell im Park", "Schnappt euch Kellen und sucht die nächste Platte im Viertel.", 60, 0, false, "Sonnig", true, "🏓", "Tischtennisplatte Park"),
    Idea(7, "Spontanes Kiosk-Treffen", "Holt euch ein Kaltgetränk unter 5 € und setzt euch auf eine Bank.", 60, 5, false, "Sonnig", true, "🥤", "Kiosk"),
    Idea(8, "Analoger Spieleabend", "Kramt ein altes Brett- oder Kartenspiel raus – Handys kommen weg!", 120, 0, true, "Regen", true, "🎲"),
    Idea(9, "15-Minuten Speed-Aufräumen", "Stell den Timer auf 15 Min: Eine Schublade oder Ecke komplett klarschiff machen.", 15, 0, true, "Egal", false, "⚡"),
    Idea(10, "Golden-Hour Foto-Spaziergang", "Geh raus und versuche 5 richtig gute Fotos von deiner Umgebung zu machen.", 30, 0, false, "Sonnig", false, "📸", "Park Aussichtspunkt"),
    Idea(11, "Billiard oder Darts in der Kneipe", "Forder deine Freunde auf zwei Runden Darts oder Billard heraus.", 120, 20, false, "Regen", true, "🎱", "Billard Kneipe"),
    Idea(12, "Bäckerei-Snack & Parkbank", "Hol dir ein frisches Teilchen beim Bäcker und genieße die Pause.", 30, 5, false, "Sonnig", false, "🥐", "Bäckerei"),
    Idea(13, "5-Teile Kleiderschrank-Check", "Sortiere 5 Teile aus, die du seit einem Jahr nicht getragen hast.", 30, 0, true, "Regen", false, "👕"),
    Idea(14, "Flohmarkt oder Second-Hand stöbern", "Schau nach coolen Vintage-Sachen mit maximal 5 € Budget.", 60, 5, false, "Egal", false, "🛍️", "Second Hand Laden"),
    Idea(15, "Gemeinsam Pizza selber machen", "Jeder belegt seine eigene Hälfte – günstiger und besser als Lieferdienst.", 120, 20, true, "Regen", true, "🍕"),
    Idea(16, "Podcast-Spaziergang ohne Ziel", "Kopfhörer rein, neue Folge an und bei jeder Kreuzung spontan abbiegen.", 30, 0, false, "Egal", false, "🎧", "Grünanlage"),
    Idea(17, "Origami oder Papierflieger-Contest", "Faltet aus normalem Papier die verrücktesten Flieger – wer fliegt weiter?", 30, 0, true, "Regen", true, "✈️"),
    Idea(18, "Bouldern oder Kletterhalle", "Powert euch gemeinsam an den Wänden aus.", 120, 20, false, "Regen", true, "🧗", "Boulderhalle"),
    Idea(19, "Musik-Alben Blind-Tasting", "Jeder zeigt 3 Songs, die der andere garantiert noch nicht kennt.", 60, 0, true, "Egal", true, "🎵"),
    Idea(20, "Kino-Sneak-Preview oder Abendfilm", "Spontan ins nächste Kino gehen ohne vorher lange Trailer zu schauen.", 120, 20, false, "Regen", true, "🍿", "Kino"),
    Idea(21, "Skizzenbuch-Challenge", "Zeichne in 15 Minuten einen Gegenstand auf deinem Tisch ab.", 15, 0, true, "Egal", false, "✏️"),
    Idea(22, "Freibad oder Hallenbad-Bahnen", "Kopf frei schwimmen für eine Stunde.", 60, 5, false, "Egal", false, "🏊", "Schwimmbad"),
    Idea(23, "Straßenbahn-Safari", "Steig in eine Linie, die du sonst nie fährst, und schau dir ein neues Viertel an.", 60, 5, false, "Egal", false, "🚋", "Haltestelle"),
    Idea(24, "Handy-Fotos ausmisten", "Lösche 50 alte Screenshots und verwackelte Bilder für mehr Speicherplatz.", 15, 0, true, "Egal", false, "📱"),
    Idea(25, "Minigolf-Runde", "Der Klassiker für sonnige Nachmittage mit Freunden.", 120, 20, false, "Sonnig", true, "⛳", "Minigolf"),
    Idea(26, "Wohnzimmer-Picknick", "Decke auf den Boden, Snacks in die Mitte und gemütlich quatschen.", 60, 5, true, "Regen", true, "🧺"),
    Idea(27, "Fahrrad-Sprint ins Grüne", "Dreh eine schnelle 30-Minuten-Runde mit dem Rad.", 30, 0, false, "Sonnig", false, "🚲", "Radweg Park"),
    Idea(28, "Lokales Museum oder Ausstellung", "Kultur-Input direkt in der Nähe – oft gibt es günstige Tagestickets.", 120, 20, false, "Regen", false, "🖼️", "Museum"),
    Idea(29, "Liegestütz- oder Plank-Challenge", "Wie viele schaffst du heute? Schreib dir deinen Rekord auf.", 15, 0, true, "Egal", false, "💪"),
    Idea(30, "Sterne schauen & Nachtspaziergang", "Sucht euch einen dunklen Ort ohne viel Straßenlicht.", 60, 0, false, "Sonnig", true, "🌌", "Aussichtspunkt"),
    Idea(31, "Döner oder Falafel-Test", "Probiert einen Imbiss aus, bei dem ihr noch nie wart.", 60, 20, false, "Egal", true, "🥙", "Falafel Imbiss"),
    Idea(32, "Brief oder Postkarte schreiben", "Schreib jemandem eine echte Karte statt nur einer WhatsApp.", 15, 5, true, "Egal", false, "✉️")
)
