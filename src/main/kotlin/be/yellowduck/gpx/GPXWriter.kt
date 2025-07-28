package be.yellowduck.gpx

import com.sun.xml.txw2.output.IndentingXMLStreamWriter
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.time.temporal.ChronoUnit
import javax.xml.stream.XMLOutputFactory

class GPXWriter : IWriter {

    /**
     * This function allows you to output the GPX to an OutputStream as an XML-formatted GPX file.
     *
     * @param stream The OutputStream to write to
     */
    override fun toStream(gpxFile: GPXFile, stream: OutputStream) {
        var factory: XMLOutputFactory
        try {
            factory = XMLOutputFactory.newFactory()
        } catch (_: NoSuchMethodError) {
            factory = XMLOutputFactory.newInstance()
        }

        val writer = IndentingXMLStreamWriter(factory.createXMLStreamWriter(stream, "UTF-8"))

        writer.setIndentStep("  ")
        writer.document {
            element("gpx") {
                if (gpxFile.version.isNotBlank()) {
                    attribute("version", gpxFile.version)
                }
                if (gpxFile.creator.isNotBlank()) {
                    attribute("creator", gpxFile.creator)
                }
                if (gpxFile.version == "1.0") {
                    attribute("xmlns", "http://www.topografix.com/GPX/1/0")
                }
                if (gpxFile.version == "1.1") {
                    attribute("xmlns", "http://www.topografix.com/GPX/1/1")
                }
                if (gpxFile.name.isNotBlank()) {
                    element("metadata") {
                        element("name", gpxFile.name)
                    }
                }
                gpxFile.tracks.forEach { track ->
                    element("trk") {
                        if (track.name.isNotBlank()) {
                            element("name", track.name)
                        }
                        track.segments.forEach { segment ->
                            element("trkseg") {
                                segment.points.forEach { point ->
                                    element("trkpt") {
                                        attribute("lat", point.lat.toString())
                                        attribute("lon", point.lon.toString())

                                        if (point.ele != 0.0) {
                                            element("ele", point.ele.toString())
                                        }

                                        point.time?.let { time ->
                                            element("time", time.truncatedTo(ChronoUnit.SECONDS).toString())
                                        }
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
        val stream = ByteArrayOutputStream()
        toStream(gpxFile, stream)
        return stream.toString("UTF-8")
    }

}