package be.yellowduck.gpx

import com.sun.net.httpserver.HttpServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.URL
import java.time.ZoneOffset

class ParserTests {

    private fun serverWithData(endPointURL: String = "/", data: String = ""): Pair<HttpServer, URL> {

        val port = 8000
        val httpServer = HttpServer.create(InetSocketAddress(port), 0)

        val bytes = data.toByteArray()
        httpServer.createContext(endPointURL) { exchange ->
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.size.toLong())
            exchange.responseBody.write(bytes)
            exchange.close()
        }
        httpServer.start()

        return Pair(httpServer, URL("http://localhost:${port}${endPointURL}"))

    }

    @Test
    fun testParserInvalidURL() {

        val (httpServer, url) = serverWithData()

        try {

            val exception = assertThrows<GPXDocumentException> {
                GPX.parse(url)
            }
            assertThat(exception.message).isEqualTo("Not a GPX document")

        } finally {
            httpServer.stop(0)
        }

    }

    @Test
    fun testParserValidURL() {

        val path = "src/test/resources/Gravelgrinders_Gent_4.gpx"
        val data = File(path).readText(Charsets.UTF_8)

        val (httpServer, url) = serverWithData(data = data)

        try {
            val gpx = GPX.parse(url)
            assertThat(gpx.version).isEqualTo("1.1")
            assertThat(gpx.creator).isEqualTo("StravaGPX")
            assertThat(gpx.tracks).isNotNull
            assertThat(gpx.tracks.size).isEqualTo(1)
        } finally {
            httpServer.stop(0)
        }

    }

    @Test
    fun testParserFile() {

        val path = "src/test/resources/Gravelgrinders_Gent_4.gpx"

        val gpx = GPX.parse(path)
        assertThat(gpx.version).isEqualTo("1.1")
        assertThat(gpx.creator).isEqualTo("StravaGPX")
        assertThat(gpx.tracks).isNotNull
        assertThat(gpx.tracks.size).isEqualTo(1)

        val distance = gpx.distance
        assertThat(distance.meters).isEqualTo(95997.70371545701)
        assertThat(distance.kilometers).isEqualTo(95.99770371545701)
        assertThat(distance.miles).isEqualTo(59.66296066840088)

        val track = gpx.tracks.first()
        assertThat(track.name).isEqualTo("Gravelgrinders Gent 4")
        assertThat(track.segments.size).isEqualTo(1)

        val segment = track.segments.first()
        assertThat(segment.points.size).isEqualTo(15478)

        val point = segment.points.first()
        assertThat(point.lat).isEqualTo(51.003349)
        assertThat(point.lon).isEqualTo(3.804193)
        assertThat(point.ele).isEqualTo(2.6)
        assertThat(point.time?.toEpochSecond(ZoneOffset.UTC)).isEqualTo(1600587025)

    }

    @Test
    fun testEmptyGPX() {
        val exception = assertThrows<GPXDocumentException> {
            GPX.parse("src/test/resources/empty.gpx")
        }
        assertThat(exception.message).isEqualTo("Not a GPX document")
    }

    @Test
    fun testNotAGPXFile() {
        val exception = assertThrows<GPXDocumentException> {
            GPX.parse("src/test/resources/no_gpx.gpx")
        }
        assertThat(exception.message).isEqualTo("Not a GPX document")
    }

    @Test
    fun testTrackSimple() {
        val gpx = GPX.parse("src/test/resources/test_track_simple.gpx")
        assertThat(gpx.name).isEqualTo("foo")
        assertThat(gpx.tracks.size).isEqualTo(1)
        assertThat(gpx.tracks.first().segments.size).isEqualTo(1)
        assertThat(gpx.tracks.first().segments.first().points.size).isEqualTo(2)
    }

}
