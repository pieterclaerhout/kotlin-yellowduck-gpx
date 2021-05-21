package be.yellowduck.gpx

import com.sun.xml.txw2.output.IndentingXMLStreamWriter
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import javax.xml.stream.XMLOutputFactory

class GPXWriter : IWriter {

    /**
     * This function allows you to output the GPX to an OutputStream as an XML-formatted GPX file.
     *
     * @param stream The OutputStream to write to
     */
    override fun toStream(gpxFile: GPXFile, stream: OutputStream) {

        val writer = IndentingXMLStreamWriter(XMLOutputFactory.newFactory().createXMLStreamWriter(stream, "UTF-8"))

        writer.setIndentStep("  ")
        writer.document {
            element("gpx") {
                if (!gpxFile.version.isNullOrBlank()) {
                    attribute("version", gpxFile.version)
                }
                if (!gpxFile.creator.isNullOrBlank()) {
                    attribute("creator", gpxFile.creator)
                }
                if (gpxFile.version == "1.0") {
                    attribute("xmlns", "http://www.topografix.com/GPX/1/0")
                }
                if (gpxFile.version == "1.1") {
                    attribute("xmlns", "http://www.topografix.com/GPX/1/1")
                }
                if (!gpxFile.name.isNullOrBlank()) {
                    element("metadata") {
                        element("name", gpxFile.name)
                    }
                }
                gpxFile.tracks.forEach { track ->
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
     * This function returns the GPX as a GPX XML string
     *
     * @return The GPX as a GPX XML string
     */
    override fun toString(gpxFile: GPXFile): String {
        var stream = ByteArrayOutputStream()
        toStream(gpxFile, stream)
        return stream.toString("UTF-8")
    }

}