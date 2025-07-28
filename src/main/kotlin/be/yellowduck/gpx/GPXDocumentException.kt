package be.yellowduck.gpx

/**
 * Exception indicating that the file is not a GPX document
 *
 * @since v1.0.6
 */
class GPXDocumentException(message: String = "Not a GPX document") : Exception(message)