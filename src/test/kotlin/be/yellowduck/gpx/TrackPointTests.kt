package be.yellowduck.gpx

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class TrackPointTests {

    val now = LocalDateTime.ofEpochSecond(1621174415, 0, ZoneOffset.UTC)

    @Test
    fun newTrackPointConstructor() {

        val point = TrackPoint(
            lat = 48.1969,
            lon = 16.34528,
            ele = 2.6,
            time = now
        )

        assertThat(point.lat).isEqualTo(48.1969)
        assertThat(point.lon).isEqualTo(16.34528)
        assertThat(point.ele).isEqualTo(2.6)
        assertThat(point.time).isEqualTo(now)

    }

    @Test
    fun newTrackPointSetters() {

        val point = TrackPoint()
        point.lat = 48.1969
        point.lon = 16.34528
        point.ele = 2.6
        point.time = now

        assertThat(point.lat).isEqualTo(48.1969)
        assertThat(point.lon).isEqualTo(16.34528)
        assertThat(point.ele).isEqualTo(2.6)
        assertThat(point.time).isEqualTo(now)

    }

    @Test
    fun distanceTo() {

        val point1 = TrackPoint(lat = 12.5, lon = 43.5)
        val point2 = TrackPoint(lat = 13.5, lon = 45.5)

        val distance = point1.distanceTo(point2)
        assertThat(distance.meters).isEqualTo(243551.33090329584)

    }

    @Test
    fun trackPointSerialize() {
        val point = TrackPoint()
        point.lat = 48.1969
        point.lon = 16.34528
        point.ele = 2.6
        point.time = now

        val gpx = GPX.parse(
            GPXFile(
                tracks = mutableListOf(
                    Track(
                        segments = mutableListOf(
                            Segment(
                                points = mutableListOf(point)
                            )
                        )
                    )
                )
            ).toString().toByteArray().inputStream()
        )

        assertThat(gpx.tracks.firstOrNull()?.segments?.firstOrNull()?.points?.first()).isEqualTo(point)
    }


}