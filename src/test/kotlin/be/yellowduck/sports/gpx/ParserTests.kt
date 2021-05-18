package be.yellowduck.sports.gpx

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.ZoneOffset

class ParserTests {

    @Test
    fun testParser() {

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
        val exception = assertThrows<Exception> {
            GPX.parse("src/test/resources/empty.gpx")
        }
        assertThat(exception.message).isEqualTo("Empty GPX document")
    }

    @Test
    fun testNotAGPXFile() {
        val exception = assertThrows<Exception> {
            GPX.parse("src/test/resources/no_gpx.gpx")
        }
        assertThat(exception.message).isEqualTo("Not a GPX document")
    }

}
