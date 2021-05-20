package be.yellowduck.gpx

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import kotlin.math.*

/**
 * TrackPoint defines a single waypoint.
 *
 * @property lat The latitude of the waypoint
 * @property lon The longitude of the waypoint
 * @property ele The elevation of the waypoint in meters
 * @property time The time associated with the waypoint
 * @constructor Creates a new trackpoint given [lat], [lon], [ele] and [time]
 *
 * @since v1.0.0
 */
@Serializable
data class TrackPoint(
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var ele: Double = 0.0,
    @Contextual var time: LocalDateTime? = null,
) {

    /**
     * distanceTo calculates the distance between this point and another point
     *
     * @param other The waypoint to calculate the distance to
     *
     */
    fun distanceTo(other: TrackPoint): Distance {
        return Distance(haversine(lat, lon, other.lat, other.lon))
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

        val earthRadius = 6371000.0
        return earthRadius * c

    }

}
