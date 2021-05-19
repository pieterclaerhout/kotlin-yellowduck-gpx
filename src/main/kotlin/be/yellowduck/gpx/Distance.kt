package be.yellowduck.gpx

/**
 * Distance is a class that can express a distance between two points.
 *
 * Internally, the distance is in meters.
 *
 * @param _meters The distance expressed in meters
 * @property kilometers The distance expressed in kilometers
 * @property miles The distance expressed in miles
 * @constructor Creates a new distance given a distance in [_meters]
 */
data class Distance(
    private var _meters: Double = 0.0
) {

    /**
     * Returns the distance in meters
     */
    val meters: Double = _meters

    /**
     * Returns the distance in kilometers
     */
    val kilometers: Double = meters / 1000.0

    /**
     * Returns the distance in miles
     */
    val miles: Double = meters / 1609.0

    /**
     * Add the distance to the existing distance
     *
     * @param distance The distance to add
     */
    fun add(distance: Distance) {
        _meters += distance.meters
    }

}