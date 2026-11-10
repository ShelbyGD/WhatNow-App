package com.whatnow.app

import java.util.Calendar

data class FreizeitAktivitaet(
    val id: Int,
    val titel: String,
    val kategorie: String, // "Freunde", "Zuhause", "Draußen", "Kostenlos"
    val dauer: String,     // "15 Min", "30 Min", "1-2 Std", "Halber Tag"
    val budget: String,    // "Kostenlos", "Max 5 €", "Max 20 €", "Egal"
    val wetter: String,    // "Sonnig", "Regen", "Egal"
    val ort: String,       // "Zuhause", "In der Nähe"
    val zielgruppe: String,// "Alle", "Kinder & Familie", "Teens", "Erwachsene"
    val interesse: String, // "Action", "Entspannung", "Kreativ", "Sport"
    val mapsQuery: String  // Suchbegriff für Google Maps Intent
)

object ActivityRepository {
    fun getInitialActivities(): List<FreizeitAktivitaet> {
        return listOf(
            FreizeitAktivitaet(1, "Spaziergang im Grünen", "Draußen", "30 Min", "Kostenlos", "Sonnig", "In der Nähe", "Alle", "Entspannung", "Park in der Nähe"),
            FreizeitAktivitaet(2, "Brettspiel-Abend", "Zuhause", "1-2 Std", "Kostenlos", "Egal", "Zuhause", "Kinder & Familie", "Action", "Spieleladen"),
            FreizeitAktivitaet(3, "Kaffee & Kuchen im Café", "Freunde", "1-2 Std", "Max 5 €", "Egal", "In der Nähe", "Alle", "Genuss", "Café"),
            FreizeitAktivitaet(4, "Fahrradtour zur Eisdiele", "Draußen", "1-2 Std", "Max 5 €", "Sonnig", "In der Nähe", "Kinder & Familie", "Sport", "Eisdiele"),
            FreizeitAktivitaet(5, "Film-Marathon auf dem Sofa", "Zuhause", "Halber Tag", "Kostenlos", "Regen", "Zuhause", "Alle", "Entspannung", "Kino"),
            FreizeitAktivitaet(6, "Geocaching Schatzsuche", "Draußen", "1-2 Std", "Kostenlos", "Sonnig", "In der Nähe", "Kinder & Familie", "Action", "Spielplatz"),
            FreizeitAktivitaet(7, "Kreatives Malen / Zeichnen", "Zuhause", "1-2 Std", "Kostenlos", "Egal", "Zuhause", "Alle", "Kreativ", "Bastelbedarf"),
            FreizeitAktivitaet(8, "Bowling mit Freunden", "Freunde", "1-2 Std", "Max 20 €", "Egal", "In der Nähe", "Teens", "Action", "Bowlingcenter"),
            FreizeitAktivitaet(9, "Heim-Workout / Yoga", "Zuhause", "30 Min", "Kostenlos", "Egal", "Zuhause", "Alle", "Sport", "Fitnessstudio"),
            FreizeitAktivitaet(10, "Bibliothek & Buch stöbern", "Draußen", "1-2 Std", "Kostenlos", "Regen", "In der Nähe", "Alle", "Entspannung", "Stadtbibliothek")
        )
    }

    fun getTageszeitHinweis(): String {
        val stunde = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (stunde) {
            in 5..11 -> "🌅 Morgen-Frische: Perfekt für Sport oder frische Luft!"
            in 12..17 -> "☀️ Tages-Power: Zeit für Unternehmungen und draußen sein."
            else -> "🌙 Abend-Modus: Gemütlich zu Hause oder entspannt unterwegs."
        }
    }
}
