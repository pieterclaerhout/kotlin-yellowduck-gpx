package be.yellowduck.sports.gpx

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import kotlin.math.*

data class TrackPoint(
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var ele: Double = 0.0,
    var time: LocalDateTime? = null,
) {

    companion object {
        const val earthRadius: Double = 6371000.0
    }

    fun distanceTo(other: TrackPoint): Double {
        return haversine(lat, lon, other.lat, other.lon)
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {

        val dLat = (lat2 - lat1) * (PI / 180.0)
        val dLon = (lon2 - lon1) * (PI / 180.0)

        val rLat1 = lat1 * (PI / 180.0)
        val rLat2 = lat2 * (PI / 180.0)

        val a1 = sin(dLat / 2) * sin(dLat / 2)
        val a2 = sin(dLon / 2) * sin(dLon / 2) * cos(rLat1) * cos(rLat2)

        val a = a1 + a2

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c

    }

}
