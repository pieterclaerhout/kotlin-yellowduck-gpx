package be.yellowduck.gpx

import com.sun.xml.txw2.output.IndentingXMLStreamWriter
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import javax.xml.stream.XMLOutputFactory

class TCXWriter : IWriter {

    /**
     * This function allows you to output the GPX to an OutputStream as an XML-formatted TCX file.
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

        var previousPoint: TrackPoint? = null

        writer.document {
            element("TrainingCenterDatabase") {
                attribute(
                    "xsi:schemaLocation",
                    "http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd"
                )
                attribute("xmlns:ns5", "http://www.garmin.com/xmlschemas/ActivityGoals/v1")
                attribute("xmlns:ns3", "http://www.garmin.com/xmlschemas/ActivityExtension/v2")
                attribute("xmlns:ns2", "http://www.garmin.com/xmlschemas/UserProfile/v2")
                attribute("xmlns", "http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2")
                attribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
                element("Folders", "")
                element("Courses") {
                    gpxFile.tracks.forEach { track ->
                        element("Course") {
                            if (track.name.isNotBlank()) {
                                element("name", track.name)
                            }
                            element("Lap") {
                                element("DistanceMeters", track.distance.meters.toString())
                            }
                            element("Track") {
                                track.segments.forEach { segment ->
                                    segment.points.forEach { point ->
                                        element("Trackpoint") {
                                            element("Position") {
                                                element("LatitudeDegrees", point.lat.toString())
                                                element("LongitudeDegrees", point.lon.toString())
                                            }

                                            if (point.ele != 0.0) {
                                                element("AltitudeMeters", point.ele.toString())
                                            }

                                            point.time?.let { time ->
                                                element("Time", point.time.toString())
                                            }

                                            val distance: Double = previousPoint?.distanceTo(point)?.meters ?: 0.0
                                            element("DistanceMeters", distance.toString())
                                            previousPoint = point
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
     * This function returns the GPX as a TCX XML string
     *
     * @return The GPX as a TCX XML string
     */
    override fun toString(gpxFile: GPXFile): String {
        val stream = ByteArrayOutputStream()
        toStream(gpxFile, stream)
        return stream.toString("UTF-8")
    }

}