package be.yellowduck.sports.gpx

data class Distance(
    private val lengthInMeters: Double = 0.0
) {

    companion object {
        const val metersPerKilometer = 1000.0
        const val metersPerMile = 1609.0
    }

    var meters: Double = lengthInMeters
    var kilometers: Double =  lengthInMeters / metersPerKilometer
    var miles: Double =  lengthInMeters / metersPerMile

}