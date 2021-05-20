package be.yellowduck.gpx

/**
 * Exception indicating that the file is an empty
 *
 * @since v1.0.6
 */
class EmptyGPXDocumentException(message: String = "Empty GPX document") : Exception(message) {
}