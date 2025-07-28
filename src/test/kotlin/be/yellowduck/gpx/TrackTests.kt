package be.yellowduck.gpx

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TrackTests {
    @Test
    fun testElevationGain() {
        val path = "src/test/resources/Gravelgrinders_Gent_4.gpx"
        val gpx = GPX.parse(path)
        assertThat(gpx.tracks.firstOrNull()?.elevationGain).isEqualTo(469.7999999999836)
    }
}