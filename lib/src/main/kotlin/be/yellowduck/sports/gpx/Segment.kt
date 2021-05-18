package be.yellowduck.sports.gpx;

data class Segment(
    val points: MutableList<TrackPoint> = mutableListOf()
) {

    val distance: Distance
        get() {
            var distance = 0.0
            points.forEachIndexed { index, point ->
                if (index > 0) {
                    distance += points[index - 1].distanceTo(point)
                }
            }
            return Distance(distance)
        }


    fun toPolyline(): String {
        return Polyline.encode(points);
    }

}
