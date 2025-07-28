package be.yellowduck.gpx

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Polyline {

    companion object {

        fun encode(coords: List<TrackPoint>): String {
            val result: MutableList<String> = mutableListOf()

            var prevLat = 0
            var prevLong = 0

            for (coord in coords) {
                val iLat = (coord.lat * 1e5).toInt()
                val iLong = (coord.lon * 1e5).toInt()

                val deltaLat = encodeValue(iLat - prevLat)
                val deltaLong = encodeValue(iLong - prevLong)

                prevLat = iLat
                prevLong = iLong

                result.add(deltaLat)
                result.add(deltaLong)
            }

            return result.joinToString("")

        }

        private fun encodeValue(value: Int): String {
            val actualValue = if (value < 0) (value shl 1).inv() else (value shl 1)
            val chunks: List<Int> = splitIntoChunks(actualValue)
            return chunks.map { (it + 63).toChar() }.joinToString("")
        }

        private fun splitIntoChunks(toEncode: Int): List<Int> {
            val chunks = mutableListOf<Int>()
            var value = toEncode
            while (value >= 32) {
                chunks.add((value and 31) or (0x20))
                value = value shr 5
            }
            chunks.add(value)
            return chunks
        }

        fun decodeToGPX(polyline: String, name: String=""): GPXFile {
            val points = decode(polyline)
            val segment = Segment(points = points.toMutableList())
            val track = Track(name = name, segments = mutableListOf(segment))
            return GPXFile(name = name, tracks = mutableListOf(track))
        }

        fun decode(polyline: String): List<TrackPoint> {

            val coordinateChunks: MutableList<MutableList<Int>> = mutableListOf()
            coordinateChunks.add(mutableListOf())

            for (char in polyline.toCharArray()) {

                var value = char.code - 63

                val isLastOfChunk = (value and 0x20) == 0
                value = value and (0x1F)

                coordinateChunks.last().add(value)

                if (isLastOfChunk) {
                    coordinateChunks.add(mutableListOf())
                }

            }

            coordinateChunks.removeAt(coordinateChunks.lastIndex)

            val coordinates: MutableList<Double> = mutableListOf()

            for (coordinateChunk in coordinateChunks) {
                var coordinate = coordinateChunk.mapIndexed { i, chunk -> chunk shl (i * 5) }.reduce { i, j -> i or j }

                if (coordinate and 0x1 > 0) {
                    coordinate = (coordinate).inv()
                }

                coordinate = coordinate shr 1
                coordinates.add((coordinate).toDouble() / 100000.0)
            }

            val points: MutableList<TrackPoint> = mutableListOf()
            var previousX = 0.0
            var previousY = 0.0

            for (i in 0..coordinates.size - 1 step 2) {

                if (coordinates[i] == 0.0 && coordinates[i + 1] == 0.0) {
                    continue
                }

                previousX += coordinates[i + 1]
                previousY += coordinates[i]

                points.add(
                    TrackPoint(
                        lon = round(previousX, 5),
                        lat = round(previousY, 5)
                    )
                )
            }

            return points

        }

        private fun round(value: Double, precision: Int) =
            (value * 10.0.pow(precision.toDouble())).toInt().toDouble() / 10.0.pow(precision.toDouble())

        fun simplify(points: List<TrackPoint>, epsilon: Double): List<TrackPoint> {

            var dmax = 0.0
            var index = 0
            val end = points.size

            for (i in 1..(end - 2)) {
                val d = perpendicularDistance(points[i], points[0], points[end - 1])
                if (d > dmax) {
                    index = i
                    dmax = d
                }
            }

            return if (dmax > epsilon) {
                val recResults1: List<TrackPoint> = simplify(points.subList(0, index + 1), epsilon)
                val recResults2: List<TrackPoint> = simplify(points.subList(index, end), epsilon)
                listOf(recResults1.subList(0, recResults1.lastIndex), recResults2).flatMap { it.toList() }
            } else {
                listOf(points[0], points[end - 1])
            }

        }

        private fun perpendicularDistance(pt: TrackPoint, lineFrom: TrackPoint, lineTo: TrackPoint): Double {
            return abs((lineTo.lon - lineFrom.lon) * (lineFrom.lat - pt.lat) - (lineFrom.lon - pt.lon) * (lineTo.lat - lineFrom.lat)) /
                    sqrt(
                        (lineTo.lon - lineFrom.lon).pow(2.0) + (lineTo.lat - lineFrom.lat).pow(2.0)
                    )
        }

    }

}
