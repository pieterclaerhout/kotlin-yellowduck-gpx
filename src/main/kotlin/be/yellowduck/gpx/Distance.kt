package be.yellowduck.gpx

import java.text.DecimalFormat

/**
 * Distance is a class that can express a distance between two points.
 *
 * Internally, the distance is in meters.
 *
 * @param _meters The distance expressed in meters
 * @property kilometers The distance expressed in kilometers
 * @property miles The distance expressed in miles
 * @constructor Creates a new distance given a distance in [_meters]
 *
 * @since v1.0.0
 */
class Distance(
    private var _meters: Double = 0.0
) {

    /**
     * The format used to format the distances
     */
    private val formatter = DecimalFormat("#.##")

    /**
     * Returns the distance in meters
     */
    val meters: Double = _meters

    /**
     * Returns the distance in kilometers
     */
    val kilometers: Double = meters / 1000.0

    /**
     * Return the distance in formatted kilometers
     */
    val formattedKilometers: String = "${formatter.format(kilometers)} km"

    /**
     * Returns the distance in miles
     */
    val miles: Double = meters / 1609.0

    /**
     * Return the distance in formatted miles
     */
    val formattedMiles: String = "${formatter.format(miles)} mi"

}