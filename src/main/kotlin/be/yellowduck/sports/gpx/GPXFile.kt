package be.yellowduck.sports.gpx

import com.sun.xml.txw2.output.IndentingXMLStreamWriter
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import javax.xml.stream.XMLOutputFactory

data class GPXFile(
    var name: String = "",
    var version: String = "1.1",
    var creator: String = "sports.yellowduck.be",
    var tracks: MutableList<Track> = mutableListOf()
) {

    val distance: Distance
        get() {
            return Distance(tracks.sumOf { it.distance.meters })
        }

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

    override fun toString(): String {
        var stream = ByteArrayOutputStream()
        toStream(stream)
        return stream.toString("UTF-8")
    }

}