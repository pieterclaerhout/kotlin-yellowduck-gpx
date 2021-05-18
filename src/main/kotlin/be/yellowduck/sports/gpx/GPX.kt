package be.yellowduck.sports.gpx

import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.FileInputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

class GPX {

    companion object {

        fun parse(path: String): GPXFile {
            val fis = FileInputStream(path)
            return parse(fis)
        }

        fun parse(inputStream: InputStream): GPXFile {

            val gpx = GPXFile()

            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()

            try {

                val xmlInput = InputSource(inputStream)
                val doc = dBuilder.parse(xmlInput)

                if (!doc.hasChildNodes()) {
                    throw Exception("Empty GPX document")
                }

                if (doc.firstChild.nodeName != "gpx") {
                    throw Exception("Not a GPX document")
                }

                gpx.version = parseVersion(doc)
                gpx.creator = parseCreator(doc)

                for (item in doc.getElementsByTagName("trk").items()) {
                    val track = parseTrack(item)
                    gpx.tracks.add(track)
                }

            } catch (e: Exception) {
                if (e.message == "Premature end of file.") {
                    throw Exception("Empty GPX document")
                }
                throw e
            }

            return gpx

        }

        private fun parseVersion(doc: org.w3c.dom.Document): String {
            val node = doc.firstChild
            return node.stringAttribute("version")
        }

        private fun parseCreator(doc: org.w3c.dom.Document): String {

            val node = doc.firstChild

            val creator = node.stringAttribute("creator")
            if (!creator.isNullOrBlank()) {
                return creator
            }

            for (metadata in node.childrenByName("metadata")) {
                for (child in metadata.childrenByName("creator")) {
                    return child.textContent
                }
            }

            return ""

        }

        private fun parseTrack(node: Node): Track {
            var trackName = node.firstChildByName("name")?.textContent.orEmpty()
            var track = Track(trackName)
            for (child in node.childrenByName("trkseg")) {
                val segment = parseSegment(child)
                track.segments.add(segment)
            }
            return track
        }

        private fun parseSegment(node: Node): Segment {
            var segment = Segment()
            for (child in node.childrenByName("trkpt")) {
                val trackPoint = parseTrackPoint(child)
                segment.points.add(trackPoint)
            }
            return segment
        }

        private fun parseTrackPoint(node: Node): TrackPoint {

            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            var parsedDate = LocalDateTime.parse(node.firstChildByName("time")?.textContent ?: "", formatter)

            return TrackPoint(
                lat = node.doubleAttribute("lat"),
                lon = node.doubleAttribute("lon"),
                ele = node.firstChildByName("ele")?.textContent?.toDoubleOrNull() ?: 0.0,
                time = parsedDate,
            )

        }

    }

}
