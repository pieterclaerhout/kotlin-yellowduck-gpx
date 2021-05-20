package be.yellowduck.gpx

import com.sun.xml.txw2.output.IndentingXMLStreamWriter
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import javax.xml.stream.XMLOutputFactory

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
    var tracks: MutableList<Track> = mutableListOf()
) {

    /**
     * Returns the total distance of all tracks in the GPX
     */
    val distance: Distance
        get() {
            return Distance(tracks.sumOf { it.distance.meters })
        }

    /**
     * This function allows you to output the GPX to an OutputStream as an XML-formatted file.
     *
     * @param stream The OutputStream to write to
     */
    fun toStream(stream: OutputStream) {

        val writer = IndentingXMLStreamWriter(XMLOutputFactory.newFactory().createXMLStreamWriter(stream, "UTF-8"))

        writer.setIndentStep("  ")
        writer.document {
            element("gpx") {
                if (!version.isNullOrBlank()) {
                    attribute("version", version)
                }
                if (!creator.isNullOrBlank()) {
                    attribute("creator", creator)
                }
                if (version == "1.0") {
                    attribute("xmlns", "http://www.topografix.com/GPX/1/0")
                }
                if (version == "1.1") {
                    attribute("xmlns", "http://www.topografix.com/GPX/1/1")
                }
                if (!name.isNullOrBlank()) {
                    element("metadata") {
                        element("name", name)
                    }
                }
                tracks.forEach { track ->
                    element("trk") {
                        if (!track.name.isNullOrBlank()) {
                            element("name", track.name)
                        }
                        track.segments.forEach { segment ->
                            element("trkseg") {
                                segment.points.forEach { point ->
                                    element("trkpt") {
                                        attribute("lat", point.lat.toString())
                                        attribute("lon", point.lon.toString())
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        writer.flush()

    }

    /**
     * This function returns the GPX as an XML string
     *
     * @return The GPX as an XML string
     */
    override fun toString(): String {
        var stream = ByteArrayOutputStream()
        toStream(stream)
        return stream.toString("UTF-8")
    }

}