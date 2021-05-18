package be.yellowduck.sports.gpx

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PolylineTests {

    @Test
    fun testCoordinateToJSON() {

        val coordinate = TrackPoint(lon = 16.34528, lat = 48.1969)
        val coordinateJson = Json.encodeToString(coordinate)

        assertThat(coordinateJson).isEqualTo("""{"lat":48.1969,"lon":16.34528}""")

    }

    @Test
    fun testCoordinateToJSONPretty() {

        val format = Json { prettyPrint = true }

        val coordinate = TrackPoint(lon = 16.34528, lat = 48.1969)
        val coordinateJson = format.encodeToString(coordinate)

        assertThat(coordinateJson).isEqualTo(
            """{
    "lat": 48.1969,
    "lon": 16.34528
}"""
        )

    }

    @Test
    fun testEncode() {

        val coordinates = listOf(
            TrackPoint(lon = 16.34528, lat = 48.1969),
            TrackPoint(lon = 16.34725, lat = 48.19732),
            TrackPoint(lon = 16.40988, lat = 48.22858)
        )

        val polyline = Polyline.encode(coordinates)

        assertThat(polyline).isEqualTo("smdeH_mwbBsAiKkbEmfK")

    }

    @Test
    fun testDecode() {

        val polyline =
            "smdeH_mwbB@cAM_AMo@Ca@Ic@OYE]G]GYE_@G_@I]G_@Ci@I_@Ic@OYI_@Ia@K_@Ga@M[M[MWKYEa@E]A[@[SKKYIYKSI]K]KWI_@KYMUIUGWK[MSMQIWKWGa@EYG_@E[A[M[I]E]MQE[IWMSG[Kc@GYGg@G]Ka@Kc@Ia@Ic@Ig@Em@Ic@Ca@Ia@I[Gc@Ic@Ic@Ge@Ge@K]Em@Kg@Gs@Mi@Gk@Mm@Og@Gk@Om@Gk@Mi@Q_@Q]S]U_@Ua@W[]WWQQSOOKUIUQWS_@Q_@S[K_@O]Oa@Eq@UYQ_@Qe@UUOWQMG[QIM]KUB_@^YTQJa@Hi@He@Fe@Fe@He@H_@F]L_@Hc@De@F]H_@J_@Bc@Jc@F_@H_@D_@?_@Aa@@]D_@F_@PWF[D]D[D[FU@a@@UBa@?a@?[D]@c@JYTe@B_@Do@Lm@Ns@De@Dm@Ls@Hg@Ji@Jk@Ha@Hi@Pi@Ee@I_@G]Ga@G_@G[Ia@Ka@I]O]Y[YYYUW_@[UY[WYWYSWUUSUWWWSUUU[YWYYW[WYYUQHQKSMU]WUS_@UWYQUOQa@[a@Y[]_@Y]Y[WWQ?SOYSYW_@UUOUAQSIm@SSQ[U[U]WWYWS]UWWSSYQWUUQQQMQSQBUKYO[G[Ka@M]Ma@C_@Ma@I_@M]ESIUCUBYAS?SBS@WEOO@[?[KSW@WASEUAUGYCYEWBUNs@Ig@IQHOFWHYHWJYI]WMa@Ok@Sg@Y]O[MUMUG]MYU]QYQe@Oe@WUM]UUOe@Se@Oa@Qg@Qo@S_@O_@QUMYM_@Mc@a@q@I[Qc@So@Kc@Qa@Se@S]O_@Qi@Ke@[i@Q_@MUMYGYQ?QBKVUMYG_@@WIYKUOSQMQSIS[SYOKQ[Ec@K]M]IYCg@Cu@Eo@GY@_@@c@E]G[KYQUU[Sc@Sg@Oq@Si@Se@Q_@O_@MUKUM[MOQUO[Sa@Oc@Qe@M_@QYQc@Sc@Si@Se@M_@Qa@Qe@Ug@Sc@Sg@Ug@Ue@Qe@Ua@Oa@Se@Q]Qe@Oc@Si@Oe@Ua@Qc@Qa@M[Qa@KYKYKWKYQYOSSSSQO[O_@Se@Oa@U]]c@Y_@Mc@SYQYYc@Ee@UYSM[g@Ua@Ma@SQOWKiAQa@@[WWQc@S[M]Si@M_@Qc@WOOc@Cm@Q[Wg@Y_@Wo@Mg@Qe@M]Oc@Yi@UUMc@Qi@Wi@G_@"

        val decoded = Polyline.decode(polyline)
        assertThat(decoded.size).isEqualTo(460)
        assertThat(decoded.first()).isEqualTo(TrackPoint(lon = 16.34528, lat = 48.1969))
        assertThat(decoded.get(10)).isEqualTo(TrackPoint(lon = 16.34725, lat = 48.19732))
        assertThat(decoded.last()).isEqualTo(TrackPoint(lon = 16.40988, lat = 48.22858))

    }

    @Test
    fun testDecodeToGPX() {

        val polyline = "smdeH_mwbBsAiKkbEmfK"

        val gpx = Polyline.decodeToGPX(polyline, "my route")
        assertThat(gpx.toString()).isEqualTo("""<?xml version="1.0" ?>
<gpx version="1.1" creator="sports.yellowduck.be" xmlns="http://www.topografix.com/GPX/1/1">
  <metadata>
    <name>my route</name>
  </metadata>
  <trk>
    <name>my route</name>
    <trkseg>
      <trkpt lat="48.1969" lon="16.34528"></trkpt>
      <trkpt lat="48.19732" lon="16.34725"></trkpt>
      <trkpt lat="48.22858" lon="16.40987"></trkpt>
    </trkseg>
  </trk>
</gpx>""")

    }

    @Test
    fun testSimplify() {

        val polyline =
            "smdeH_mwbB@cAM_AMo@Ca@Ic@OYE]G]GYE_@G_@I]G_@Ci@I_@Ic@OYI_@Ia@K_@Ga@M[M[MWKYEa@E]A[@[SKKYIYKSI]K]KWI_@KYMUIUGWK[MSMQIWKWGa@EYG_@E[A[M[I]E]MQE[IWMSG[Kc@GYGg@G]Ka@Kc@Ia@Ic@Ig@Em@Ic@Ca@Ia@I[Gc@Ic@Ic@Ge@Ge@K]Em@Kg@Gs@Mi@Gk@Mm@Og@Gk@Om@Gk@Mi@Q_@Q]S]U_@Ua@W[]WWQQSOOKUIUQWS_@Q_@S[K_@O]Oa@Eq@UYQ_@Qe@UUOWQMG[QIM]KUB_@^YTQJa@Hi@He@Fe@Fe@He@H_@F]L_@Hc@De@F]H_@J_@Bc@Jc@F_@H_@D_@?_@Aa@@]D_@F_@PWF[D]D[D[FU@a@@UBa@?a@?[D]@c@JYTe@B_@Do@Lm@Ns@De@Dm@Ls@Hg@Ji@Jk@Ha@Hi@Pi@Ee@I_@G]Ga@G_@G[Ia@Ka@I]O]Y[YYYUW_@[UY[WYWYSWUUSUWWWSUUU[YWYYW[WYYUQHQKSMU]WUS_@UWYQUOQa@[a@Y[]_@Y]Y[WWQ?SOYSYW_@UUOUAQSIm@SSQ[U[U]WWYWS]UWWSSYQWUUQQQMQSQBUKYO[G[Ka@M]Ma@C_@Ma@I_@M]ESIUCUBYAS?SBS@WEOO@[?[KSW@WASEUAUGYCYEWBUNs@Ig@IQHOFWHYHWJYI]WMa@Ok@Sg@Y]O[MUMUG]MYU]QYQe@Oe@WUM]UUOe@Se@Oa@Qg@Qo@S_@O_@QUMYM_@Mc@a@q@I[Qc@So@Kc@Qa@Se@S]O_@Qi@Ke@[i@Q_@MUMYGYQ?QBKVUMYG_@@WIYKUOSQMQSIS[SYOKQ[Ec@K]M]IYCg@Cu@Eo@GY@_@@c@E]G[KYQUU[Sc@Sg@Oq@Si@Se@Q_@O_@MUKUM[MOQUO[Sa@Oc@Qe@M_@QYQc@Sc@Si@Se@M_@Qa@Qe@Ug@Sc@Sg@Ug@Ue@Qe@Ua@Oa@Se@Q]Qe@Oc@Si@Oe@Ua@Qc@Qa@M[Qa@KYKYKWKYQYOSSSSQO[O_@Se@Oa@U]]c@Y_@Mc@SYQYYc@Ee@UYSM[g@Ua@Ma@SQOWKiAQa@@[WWQc@S[M]Si@M_@Qc@WOOc@Cm@Q[Wg@Y_@Wo@Mg@Qe@M]Oc@Yi@UUMc@Qi@Wi@G_@"

        val coordinates = Polyline.decode(polyline)

        val simplified = Polyline.simplify(coordinates, 0.0001)
        assertThat(simplified.size).isEqualTo(39)
        assertThat(simplified.first()).isEqualTo(TrackPoint(lon = 16.34528, lat = 48.1969))
        assertThat(simplified.last()).isEqualTo(TrackPoint(lon = 16.40988, lat = 48.22858))

    }

}
