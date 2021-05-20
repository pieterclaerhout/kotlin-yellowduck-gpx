package be.yellowduck.gpx

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DistanceTests {

    @Test
    fun test() {

        val dist = Distance(123456.0)

        assertThat(dist.meters).isEqualTo(123456.0)
        assertThat(dist.kilometers).isEqualTo(123.456)
        assertThat(dist.formattedKilometers).isEqualTo("123,46 km")
        assertThat(dist.miles).isEqualTo(76.72840273461777)
        assertThat(dist.formattedMiles).isEqualTo("76,73 mi")

    }

}
