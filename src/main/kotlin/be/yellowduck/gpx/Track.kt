package be.yellowduck.gpx

/**
 * A track is sorted list of segments.
 *
 * @property name The name of the segment
 * @property segments The sorted list of segments in this track
 *
 * @since v1.0.0
 */
data class Track(
    val name: String = "",
    val segments: MutableList<Segment> = mutableListOf()
) {

    /**
     * Returns the total distance of the track
     */
    val distance: Distance
        get() {
            return Distance(segments.sumOf { it.distance.meters })
        }

    /**
     * Returns the total elevation gain of the track
     */
    val elevationGain: Double
        get() {
            return segments.sumOf { it.elevationGain }
        }

    /**
     * Returns the segment as an encoded polyline string.
     *
     * [Encoded Polyline Algorithm Format](https://developers.google.com/maps/documentation/utilities/polylinealgorithm)
     *
     * @return A string with the encoded polyline
     */
    fun toPolyline(): String {
        val coords: MutableList<TrackPoint> = mutableListOf()
        segments.forEach {
            coords.addAll(it.points)
        }
        return Polyline.encode(coords)
    }

}