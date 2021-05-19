package be.yellowduck.gpx;

data class Segment(
    val points: MutableList<TrackPoint> = mutableListOf()
) {

    val distance: Distance
        get() {
            var distance = Distance()
            points.forEachIndexed { index, point ->
                if (index > 0) {
                    distance.add(points[index - 1].distanceTo(point))
                }
            }
            return distance
        }

    fun toPolyline(): String {
        return Polyline.encode(points);
    }

}
