package be.yellowduck.gpx

/**
 * Exception indicating that the file is not a GPX document
 *
 * @since v1.0.6
 */
class NotAGPXDocumentException(message: String = "Not a GPX document") : Exception(message) {
}