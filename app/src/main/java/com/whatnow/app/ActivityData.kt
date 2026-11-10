package com.whatnow.app

import java.util.Calendar

data class FreizeitAktivitaet(
    val id: Int,
    val titel: String,
    val kategorie: String,       // "Draußen", "Zuhause", "Freunde", "Kultur & City"
    val dauer: String,           // "15 Min", "30 Min", "1-2 Std", "Halber Tag"
    val budget: String,          // "Kostenlos", "Max 5 €", "Max 20 €"
    val wetter: String,          // "Sonnig", "Regen", "Egal"
    val ort: String,             // "Zuhause", "In der Nähe"
    val zielgruppe: String,      // "Alle", "Kinder & Familie", "Teens", "Erwachsene"
    val interesse: String,       // "Action", "Entspannung", "Kreativ", "Sport", "Genuss", "Wissen"
    val mapsQuery: String,       // Suchbegriff für Google Maps Intent
    val emoji: String = "✨",
    val beschreibung: String = "Perfekt gegen Langeweile – direkt loslegen!",
    val energieLevel: String = "Mittel" // "Ruhig", "Mittel", "Aktiv"
)

data class MysteryChallenge(
    val titel: String,
    val rahmen: String,
    val aufgabe: String,
    val mapsQuery: String
)

object ActivityRepository {
    const val APP_VERSION = "v2.2.0"

    val kategorien = listOf("Alle", "Draußen", "Zuhause", "Freunde", "Kultur & City")
    val dauern = listOf("Alle", "15 Min", "30 Min", "1-2 Std", "Halber Tag")
    val budgets = listOf("Alle", "Kostenlos", "Max 5 €", "Max 20 €")
    val wetterOptionen = listOf("Egal", "Sonnig", "Regen")
    val zielgruppen = listOf("Alle", "Kinder & Familie", "Teens", "Erwachsene")
    val interessen = listOf("Alle", "Action", "Entspannung", "Kreativ", "Sport", "Genuss", "Wissen")
    val energieLevels = listOf("Alle", "Ruhig", "Mittel", "Aktiv")

    fun getInitialActivities(): List<FreizeitAktivitaet> {
        return listOf(
            FreizeitAktivitaet(
                id = 1,
                titel = "Spaziergang im Grünen",
                kategorie = "Draußen",
                dauer = "30 Min",
                budget = "Kostenlos",
                wetter = "Sonnig",
                ort = "In der Nähe",
                zielgruppe = "Alle",
                interesse = "Entspannung",
                mapsQuery = "Park in der Nähe",
                emoji = "🌳",
                beschreibung = "Frische Luft tanken, Handy wegstecken und eine neue Route im Park entdecken.",
                energieLevel = "Mittel"
            ),
            FreizeitAktivitaet(
                id = 2,
                titel = "Brettspiel- & Karten-Abend",
                kategorie = "Zuhause",
                dauer = "1-2 Std",
                budget = "Kostenlos",
                wetter = "Egal",
                ort = "Zuhause",
                zielgruppe = "Kinder & Familie",
                interesse = "Action",
                mapsQuery = "Spieleladen",
                emoji = "🎲",
                beschreibung = "Klassiker oder Kartenspiele herausholen und ein kleines Turnier starten.",
                energieLevel = "Ruhig"
            ),
            FreizeitAktivitaet(
                id = 3,
                titel = "Kaffee & Kuchen im neuen Café",
                kategorie = "Freunde",
                dauer = "1-2 Std",
                budget = "Max 5 €",
                wetter = "Egal",
                ort = "In der Nähe",
                zielgruppe = "Alle",
                interesse = "Genuss",
                mapsQuery = "Gemütliches Café",
                emoji = "☕",
                beschreibung = "Ein Café testen, in dem du noch nie warst, und das Lieblingsgetränk genießen.",
                energieLevel = "Ruhig"
            ),
            FreizeitAktivitaet(
                id = 4,
                titel = "Fahrradtour zur Eisdiele",
                kategorie = "Draußen",
                dauer = "1-2 Std",
                budget = "Max 5 €",
                wetter = "Sonnig",
                ort = "In der Nähe",
                zielgruppe = "Kinder & Familie",
                interesse = "Sport",
                mapsQuery = "Eisdiele",
                emoji = "🚲",
                beschreibung = "Schöne Radstrecke fahren und sich am Ziel zwei Kugeln Lieblingseis gönnen.",
                energieLevel = "Aktiv"
            ),
            FreizeitAktivitaet(
                id = 5,
                titel = "Film-Marathon & Heimkino",
                kategorie = "Zuhause",
                dauer = "Halber Tag",
                budget = "Kostenlos",
                wetter = "Regen",
                ort = "Zuhause",
                zielgruppe = "Alle",
                interesse = "Entspannung",
                mapsQuery = "Kino",
                emoji = "🍿",
                beschreibung = "Popcorn machen, Licht dimmen und eine Filmreihe oder einen Klassiker schauen.",
                energieLevel = "Ruhig"
            ),
            FreizeitAktivitaet(
                id = 6,
                titel = "Geocaching Schatzsuche",
                kategorie = "Draußen",
                dauer = "1-2 Std",
                budget = "Kostenlos",
                wetter = "Sonnig",
                ort = "In der Nähe",
                zielgruppe = "Kinder & Familie",
                interesse = "Action",
                mapsQuery = "Sehenswürdigkeit Park",
                emoji = "🧭",
                beschreibung = "Versteckte Caches in deiner Nachbarschaft per GPS aufspüren und loggen.",
                energieLevel = "Aktiv"
            ),
            FreizeitAktivitaet(
                id = 7,
                titel = "Urban Sketching & Zeichnen",
                kategorie = "Zuhause",
                dauer = "30 Min",
                budget = "Kostenlos",
                wetter = "Egal",
                ort = "Zuhause",
                zielgruppe = "Alle",
                interesse = "Kreativ",
                mapsQuery = "Künstlerbedarf",
                emoji = "🎨",
                beschreibung = "Stift & Papier schnappen: Zeichne den Blick aus deinem Fenster oder ein Comic.",
                energieLevel = "Ruhig"
            ),
            FreizeitAktivitaet(
                id = 8,
                titel = "Bowling-Match mit Freunden",
                kategorie = "Freunde",
                dauer = "1-2 Std",
                budget = "Max 20 €",
                wetter = "Egal",
                ort = "In der Nähe",
                zielgruppe = "Teens",
                interesse = "Action",
                mapsQuery = "Bowlingcenter",
                emoji = "🎳",
                beschreibung = "Wer wirft die meisten Strikes? Perfekt für Regentage oder den Abend.",
                energieLevel = "Mittel"
            ),
            FreizeitAktivitaet(
                id = 9,
                titel = "Power-Workout oder Mobility-Flow",
                kategorie = "Zuhause",
                dauer = "15 Min",
                budget = "Kostenlos",
                wetter = "Egal",
                ort = "Zuhause",
                zielgruppe = "Alle",
                interesse = "Sport",
                mapsQuery = "Calisthenics Park",
                emoji = "🧘",
                beschreibung = "15 Minuten Stretching, Yoga oder Bodyweight-Übungen mit Lieblingsmusik.",
                energieLevel = "Aktiv"
            ),
            FreizeitAktivitaet(
                id = 10,
                titel = "Stadtbibliothek & Buch-Entdeckung",
                kategorie = "Kultur & City",
                dauer = "1-2 Std",
                budget = "Kostenlos",
                wetter = "Regen",
                ort = "In der Nähe",
                zielgruppe = "Alle",
                interesse = "Wissen",
                mapsQuery = "Stadtbibliothek Buchhandlung",
                emoji = "📚",
                beschreibung = "Durch Regale stöbern, in ein neues Genre reinlesen und Ruhe genießen.",
                energieLevel = "Ruhig"
            ),
            FreizeitAktivitaet(
                id = 11,
                titel = "Golden-Hour Foto-Safari",
                kategorie = "Draußen",
                dauer = "30 Min",
                budget = "Kostenlos",
                wetter = "Sonnig",
                ort = "In der Nähe",
                zielgruppe = "Teens",
                interesse = "Kreativ",
                mapsQuery = "Aussichtspunkt",
                emoji = "📸",
                beschreibung = "Mach 10 außergewöhnliche Fotos von Spiegelungen, Architektur oder Natur.",
                energieLevel = "Mittel"
            ),
            FreizeitAktivitaet(
                id = 12,
                titel = "Kühlschrank-Roulette Kochen",
                kategorie = "Zuhause",
                dauer = "30 Min",
                budget = "Kostenlos",
                wetter = "Egal",
                ort = "Zuhause",
                zielgruppe = "Alle",
                interesse = "Genuss",
                mapsQuery = "Wochenmarkt Hofladen",
                emoji = "👨‍🍳",
                beschreibung = "Zaubere ein kreatives Gericht nur aus Zutaten, die schon zu Hause sind.",
                energieLevel = "Mittel"
            ),
            FreizeitAktivitaet(
                id = 13,
                titel = "Tischtennis-Duell im Park",
                kategorie = "Freunde",
                dauer = "30 Min",
                budget = "Kostenlos",
                wetter = "Sonnig",
                ort = "In der Nähe",
                zielgruppe = "Alle",
                interesse = "Sport",
                mapsQuery = "Tischtennisplatte Park",
                emoji = "🏓",
                beschreibung = "Schnelles Match an einer öffentlichen Platte oder Rundlauf mit Freunden.",
                energieLevel = "Aktiv"
            ),
            FreizeitAktivitaet(
                id = 14,
                titel = "Bouldern & Klettern",
                kategorie = "Freunde",
                dauer = "1-2 Std",
                budget = "Max 20 €",
                wetter = "Egal",
                ort = "In der Nähe",
                zielgruppe = "Erwachsene",
                interesse = "Sport",
                mapsQuery = "Boulderhalle Kletterhalle",
                emoji = "🧗",
                beschreibung = "Kombiniere Kraft und Köpfchen an spannenden Boulder-Routen.",
                energieLevel = "Aktiv"
            ),
            FreizeitAktivitaet(
                id = 15,
                titel = "Flohmarkt oder Second-Hand Schatzjagd",
                kategorie = "Kultur & City",
                dauer = "1-2 Std",
                budget = "Max 5 €",
                wetter = "Sonnig",
                ort = "In der Nähe",
                zielgruppe = "Alle",
                interesse = "Kreativ",
                mapsQuery = "Flohmarkt Second Hand Laden",
                emoji = "🏺",
                beschreibung = "Stöbere nach kuriosen Vintage-Schätzen, Schallplatten oder Büchern.",
                energieLevel = "Mittel"
            ),
            FreizeitAktivitaet(
                id = 16,
                titel = "Picknick beim Sonnenuntergang",
                kategorie = "Draußen",
                dauer = "1-2 Std",
                budget = "Max 5 €",
                wetter = "Sonnig",
                ort = "In der Nähe",
                zielgruppe = "Erwachsene",
                interesse = "Genuss",
                mapsQuery = "Seeufer Aussichtspunkt Park",
                emoji = "🧺",
                beschreibung = "Decke und Lieblingssnacks einpacken und den Abendhimmel beobachten.",
                energieLevel = "Ruhig"
            ),
            FreizeitAktivitaet(
                id = 17,
                titel = "Wohnzimmer-Escape-Game / Rätsel",
                kategorie = "Zuhause",
                dauer = "1-2 Std",
                budget = "Kostenlos",
                wetter = "Regen",
                ort = "Zuhause",
                zielgruppe = "Kinder & Familie",
                interesse = "Wissen",
                mapsQuery = "Escape Room",
                emoji = "🔐",
                beschreibung = "Verstecke Hinweise in der Wohnung oder löst gemeinsam ein Krimi-Rätsel.",
                energieLevel = "Mittel"
            ),
            FreizeitAktivitaet(
                id = 18,
                titel = "Minigolf oder Adventure-Golf",
                kategorie = "Freunde",
                dauer = "1-2 Std",
                budget = "Max 20 €",
                wetter = "Sonnig",
                ort = "In der Nähe",
                zielgruppe = "Kinder & Familie",
                interesse = "Action",
                mapsQuery = "Minigolf Schwarzlicht Minigolf",
                emoji = "⛳",
                beschreibung = "Draußen im Grünen oder drinnen bei Schwarzlicht den Bahnrekord knacken.",
                energieLevel = "Mittel"
            ),
            FreizeitAktivitaet(
                id = 19,
                titel = "15-Minuten Speed-Aufräum-Challenge",
                kategorie = "Zuhause",
                dauer = "15 Min",
                budget = "Kostenlos",
                wetter = "Egal",
                ort = "Zuhause",
                zielgruppe = "Alle",
                interesse = "Action",
                mapsQuery = "Recyclinghof Verschenkemarkt",
                emoji = "⚡",
                beschreibung = "Timer auf 15 Minuten, Lieblings-Songs laut aufdrehen und eine Ecke komplett befreien!",
                energieLevel = "Aktiv"
            ),
            FreizeitAktivitaet(
                id = 20,
                titel = "Museum, Galerie oder Ausstellung",
                kategorie = "Kultur & City",
                dauer = "1-2 Std",
                budget = "Max 20 €",
                wetter = "Regen",
                ort = "In der Nähe",
                zielgruppe = "Erwachsene",
                interesse = "Wissen",
                mapsQuery = "Museum Kunstgalerie",
                emoji = "🏛️",
                beschreibung = "Lass dich von Technik, Geschichte oder moderner Kunst in deiner Stadt inspirieren.",
                energieLevel = "Ruhig"
            ),
            FreizeitAktivitaet(
                id = 21,
                titel = "Sternenhimmel & Satelliten spotten",
                kategorie = "Draußen",
                dauer = "30 Min",
                budget = "Kostenlos",
                wetter = "Sonnig",
                ort = "In der Nähe",
                zielgruppe = "Alle",
                interesse = "Wissen",
                mapsQuery = "Sternwarte Anhöhe",
                emoji = "🌌",
                beschreibung = "Abends einen dunklen Ort suchen und Sternbilder oder die ISS am Himmel entdecken.",
                energieLevel = "Ruhig"
            ),
            FreizeitAktivitaet(
                id = 22,
                titel = "Streetfood- oder Wochenmarkt-Tour",
                kategorie = "Kultur & City",
                dauer = "1-2 Std",
                budget = "Max 20 €",
                wetter = "Sonnig",
                ort = "In der Nähe",
                zielgruppe = "Alle",
                interesse = "Genuss",
                mapsQuery = "Streetfood Wochenmarkt",
                emoji = "🌮",
                beschreibung = "Probiere eine Spezialität aus einer Küche, die du noch nie gegessen hast.",
                energieLevel = "Mittel"
            ),
            FreizeitAktivitaet(
                id = 23,
                titel = "Origami, DIY oder Upcycling-Projekt",
                kategorie = "Zuhause",
                dauer = "30 Min",
                budget = "Kostenlos",
                wetter = "Regen",
                ort = "Zuhause",
                zielgruppe = "Kinder & Familie",
                interesse = "Kreativ",
                mapsQuery = "Baumarkt Bastelladen",
                emoji = "✂️",
                beschreibung = "Aus Papier, Gläsern oder Holz etwas Neues für den Schreibtisch oder die Wohnung bauen.",
                energieLevel = "Ruhig"
            ),
            FreizeitAktivitaet(
                id = 24,
                titel = "Tagesausflug zum Naturpfad oder See",
                kategorie = "Draußen",
                dauer = "Halber Tag",
                budget = "Kostenlos",
                wetter = "Sonnig",
                ort = "In der Nähe",
                zielgruppe = "Alle",
                interesse = "Sport",
                mapsQuery = "Badesee Wanderweg Naturpfad",
                emoji = "🥾",
                beschreibung = "Rucksack packen, raus aus dem Alltag und ein neues Naturgebiet erkunden.",
                energieLevel = "Aktiv"
            )
        )
    }

    fun getMysteryChallenges(): List<MysteryChallenge> {
        return listOf(
            MysteryChallenge(
                titel = "Kompass-Zufallstour",
                rahmen = "30 Min | Kostenlos | Draußen",
                aufgabe = "✨ Geh vor die Tür und biege an den nächsten 5 Kreuzungen abwechselnd links und rechts ab. Entdecke einen Ort, den du noch nie bewusst gesehen hast!",
                mapsQuery = "Sehenswürdigkeit in der Nähe"
            ),
            MysteryChallenge(
                titel = "Farben-Foto-Safari",
                rahmen = "15 Min | Kostenlos | Überall",
                aufgabe = "📸 Suche in deiner Umgebung 5 spannende Motive in genau einer Signalfarbe (z. B. Gelb oder Türkis) und erstelle daraus eine kreative Collage!",
                mapsQuery = "Street Art Aussichtspunkt"
            ),
            MysteryChallenge(
                titel = "Analog-Detox-Stunde",
                rahmen = "1 Std | Kostenlos | Zuhause",
                aufgabe = "🕯️ Handy in den Flugmodus! Mach dir ein heißes Getränk, leg deine Lieblingsmusik auf und skizziere 3 Dinge, auf die du dich diesen Monat freust.",
                mapsQuery = "Teeladen Buchhandlung"
            ),
            MysteryChallenge(
                titel = "5-Euro-Kulinarik-Challenge",
                rahmen = "30 Min | Max 5 € | In der Nähe",
                aufgabe = "🥐 Geh in eine Bäckerei oder einen kleinen Laden und kaufe genau eine Spezialität unter 5 €, die du noch nie zuvor probiert hast!",
                mapsQuery = "Bäckerei Feinkost"
            ),
            MysteryChallenge(
                titel = "Spontan-Freude-Mission",
                rahmen = "15 Min | Kostenlos | Freunde",
                aufgabe = "💌 Schreib einer Person, von der du lange nichts gehört hast, eine echte Sprachnachricht oder schlage ein spontanes Treffen für diese Woche vor!",
                mapsQuery = "Eiscafé Park"
            )
        )
    }

    fun getTageszeitTitel(): String {
        val stunde = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (stunde) {
            in 5..11 -> "🌅 Guten Morgen! Morgen-Frische"
            in 12..17 -> "☀️ Hallo! Volle Tages-Power"
            in 18..21 -> "🌆 Guten Abend! Feierabend-Modus"
            else -> "🌙 Nacht-Eule! Späte Inspiration"
        }
    }

    fun getTageszeitHinweis(): String {
        val stunde = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (stunde) {
            in 5..11 -> "🌅 Morgen-Frische: Perfekt für Bewegung, frische Luft oder ein Café!"
            in 12..17 -> "☀️ Tages-Power: Ideal für Ausflüge, Sport, Freunde oder Entdeckungen draußen."
            in 18..21 -> "🌆 Abend-Modus: Zeit für Freunde, gutes Essen, Spiele oder Sonnenuntergang."
            else -> "🌙 Late-Night: Gemütlich zu Hause, Filme, Kreatives oder Sternegucken."
        }
    }

    fun isRecommendedForCurrentTime(item: FreizeitAktivitaet): Boolean {
        val stunde = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (stunde) {
            in 5..11 -> item.energieLevel == "Aktiv" || item.kategorie == "Draußen" || item.interesse == "Genuss"
            in 12..17 -> item.kategorie == "Draußen" || item.kategorie == "Freunde" || item.kategorie == "Kultur & City"
            in 18..21 -> item.kategorie == "Freunde" || item.kategorie == "Zuhause" || item.energieLevel != "Aktiv"
            else -> item.ort == "Zuhause" || item.energieLevel == "Ruhig"
        }
    }
}
