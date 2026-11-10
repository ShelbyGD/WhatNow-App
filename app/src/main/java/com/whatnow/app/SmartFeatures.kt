package com.whatnow.app

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.sqrt

class ShakeDetector(context: Context, private val onShake: () -> Unit) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var lastShakeTime: Long = 0
    var isEnabled: Boolean = true

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isEnabled) return
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            val gForce = sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

            if (gForce > 2.5f) {
                val now = System.currentTimeMillis()
                if (lastShakeTime + 1100 > now) return
                lastShakeTime = now
                onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    companion object {
        fun shareActivity(
            context: Context,
            title: String,
            duration: String,
            budget: String,
            mapsQuery: String
        ) {
            val text = buildString {
                appendLine("⚡ WHAT NOW? – Schluss mit Langeweile!")
                appendLine()
                appendLine("Wie wäre es heute damit?")
                appendLine("✨ $title")
                appendLine("⏱️ $duration | 💰 $budget")
                appendLine()
                appendLine("Bist du dabei? Lass uns das machen! 🙌")
                append("📍 In der Nähe suchen: https://www.google.com/maps/search/?api=1&query=${Uri.encode(mapsQuery)}")
            }
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(intent, "Aktivität teilen über..."))
        }

        fun openMaps(context: Context, mapsQuery: String) {
            val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(mapsQuery)}")
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}

class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences("whatnow_smart_prefs", Context.MODE_PRIVATE)

    fun isDarkMode(defaultDark: Boolean = true): Boolean =
        prefs.getBoolean("dark_mode", defaultDark)

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("dark_mode", enabled).apply()
    }

    fun isShakeEnabled(): Boolean =
        prefs.getBoolean("shake_enabled", true)

    fun setShakeEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("shake_enabled", enabled).apply()
    }

    fun getFavoriteIds(): Set<Int> =
        prefs.getStringSet("favorite_ids", emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet() ?: emptySet()

    fun saveFavoriteIds(ids: Set<Int>) {
        prefs.edit().putStringSet("favorite_ids", ids.map { it.toString() }.toSet()).apply()
    }

    fun getCompletedIds(): Set<Int> =
        prefs.getStringSet("completed_ids", emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet() ?: emptySet()

    fun saveCompletedIds(ids: Set<Int>) {
        prefs.edit().putStringSet("completed_ids", ids.map { it.toString() }.toSet()).apply()
    }

    fun getCustomActivities(): List<FreizeitAktivitaet> {
        val raw = prefs.getString("custom_activities_json", null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            buildList {
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    add(
                        FreizeitAktivitaet(
                            id = obj.optInt("id", 1000 + i),
                            titel = obj.optString("titel", "Eigene Idee"),
                            kategorie = obj.optString("kategorie", "Freunde"),
                            dauer = obj.optString("dauer", "1-2 Std"),
                            budget = obj.optString("budget", "Kostenlos"),
                            wetter = obj.optString("wetter", "Egal"),
                            ort = obj.optString("ort", "In der Nähe"),
                            zielgruppe = obj.optString("zielgruppe", "Alle"),
                            interesse = obj.optString("interesse", "Action"),
                            mapsQuery = obj.optString("mapsQuery", obj.optString("titel", "Freizeit")),
                            emoji = obj.optString("emoji", "💡"),
                            beschreibung = obj.optString("beschreibung", "Eigene gespeicherte Aktivität"),
                            energieLevel = obj.optString("energieLevel", "Mittel")
                        )
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveCustomActivities(list: List<FreizeitAktivitaet>) {
        val arr = JSONArray()
        list.forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("titel", item.titel)
                put("kategorie", item.kategorie)
                put("dauer", item.dauer)
                put("budget", item.budget)
                put("wetter", item.wetter)
                put("ort", item.ort)
                put("zielgruppe", item.zielgruppe)
                put("interesse", item.interesse)
                put("mapsQuery", item.mapsQuery)
                put("emoji", item.emoji)
                put("beschreibung", item.beschreibung)
                put("energieLevel", item.energieLevel)
            }
            arr.put(obj)
        }
        prefs.edit().putString("custom_activities_json", arr.toString()).apply()
    }
}
