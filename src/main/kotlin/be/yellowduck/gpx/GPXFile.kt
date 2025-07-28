package be.yellowduck.gpx

/**
 * This class is an abstraction of a GPX file
 *
 * @property name The name of the GPX file
 * @property version The GPX version which is used (1.0 or 1.1)
 * @property creator The name of the application that generated the GPX
 * @property tracks The sorted list of tracks in this GPX
 *
 * @since v1.0.0
 */
data class GPXFile(
    var name: String = "",
    var version: String = "1.1",
    var creator: String = "sports.yellowduck.be",
    var tracks: MutableList<Track> = mutableListOf(),
    val writers: Map<String, IWriter> = mapOf(
        "gpx" to GPXWriter(),
        "tcx" to TCXWriter(),
    )
) {

    /**
     * Returns the total distance of all tracks in the GPX
     */
    val distance: Distance
        get() {
            return Distance(tracks.sumOf { it.distance.meters })
        }

    /**
     * This can be used to write the data back to a GPX file
     */
    val asGPXString: String
        get() {
            return writers["gpx"]!!.toString(this)
        }

    /**
     * This can be used to write the data back to a TCX file
     */
    val asTCXString: String
        get() {
            return writers["tcx"]!!.toString(this)
        }

    /**
     * This function returns the GPX as an XML string
     *
     * @return The GPX as an XML string
     */
    override fun toString(): String {
        return asGPXString
    }

}