package be.yellowduck.gpx

import java.io.OutputStream

interface IWriter {
    fun toStream(gpxFile: GPXFile, stream: OutputStream)
    fun toString(gpxFile: GPXFile): String
}