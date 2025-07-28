package be.yellowduck.gpx

import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory
//import khttp.get
import org.xml.sax.SAXParseException

/**
 * This class can be used to parse an existing GPX file.
 *
 * @since v1.0.0
 */
class GPX {

    companion object {

        /**
         * Parse a GPS from a URL
         *
         * @param url The URL to parse the GPX from
         * @return A parsed GPXFile instance
         *
         * @throws GPXDocumentException
         */
        fun parse(url: URL): GPXFile {
            url.openStream().use {
                return parse(it)
            }
//            val r = get(url.toString())
//            return parse(r.text.byteInputStream())
        }

        /**
         * Parse a GPX from a file path
         *
         * @param path The path where the GPX file is located
         * @return A parsed GPXFile instance
         *
         * @throws GPXDocumentException
         */
        fun parse(path: String): GPXFile {
            val fis = FileInputStream(path)
            return parse(fis)
        }

        /**
         * Parse a GPX from an input stream
         *
         * @param inputStream The inputstream to parse
         * @return A parsed GPXFile instance
         *
         * @throws GPXDocumentException
         */
        fun parse(inputStream: InputStream): GPXFile {

            val gpx = GPXFile()

            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()

            try {

                val xmlInput = InputSource(inputStream)
                val doc = dBuilder.parse(xmlInput)

                if (doc.firstChild.nodeName != "gpx") {
                    throw GPXDocumentException()
                }

                gpx.name = parseName(doc)
                gpx.version = parseVersion(doc)
                gpx.creator = parseCreator(doc)

                for (item in doc.getElementsByTagName("trk").items()) {
                    val track = parseTrack(item)
                    gpx.tracks.add(track)
                }

            } catch (_: SAXParseException) {
                throw GPXDocumentException()
            }

            return gpx

        }

        private fun parseName(doc: org.w3c.dom.Document): String {
            return doc.getElementsByTagName("metadata").items().firstOrNull()?.let { metadata ->
                metadata.childNodes.itemsByName("name").firstOrNull()?.textContent
            } ?: ""
        }

        private fun parseVersion(doc: org.w3c.dom.Document): String {
            val node = doc.firstChild
            return node.stringAttribute("version")
        }

        private fun parseCreator(doc: org.w3c.dom.Document): String {

            val node = doc.firstChild

            val creator = node.stringAttribute("creator")
            if (creator.isNotBlank()) {
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
            val trackName = node.firstChildByName("name")?.textContent.orEmpty()
            val track = Track(trackName)
            for (child in node.childrenByName("trkseg")) {
                val segment = parseSegment(child)
                track.segments.add(segment)
            }
            return track
        }

        private fun parseSegment(node: Node): Segment {
            val segment = Segment()
            for (child in node.childrenByName("trkpt")) {
                val trackPoint = parseTrackPoint(child)
                segment.points.add(trackPoint)
            }
            return segment
        }

        private fun parseTrackPoint(node: Node): TrackPoint {
            val parsedDate = node.firstChildByName("time")?.textContent?.let {
                LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
            }

            return TrackPoint(
                lat = node.doubleAttribute("lat"),
                lon = node.doubleAttribute("lon"),
                ele = node.firstChildByName("ele")?.textContent?.toDoubleOrNull() ?: 0.0,
                time = parsedDate,
            )

        }

    }

}
