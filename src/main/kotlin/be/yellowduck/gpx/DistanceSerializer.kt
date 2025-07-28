package be.yellowduck.gpx

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.IOException


class DistanceSerializer : KSerializer<Distance>, StdSerializer<Distance>(Distance::class.java) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(
        value: Distance?, jgen: JsonGenerator, provider: SerializerProvider?
    ) {
        if (value == null) {
            jgen.writeNull()
        } else {
            jgen.writeNumber(value.meters)
        }
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Distance", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Distance) {
        encoder.encodeDouble(value.meters)
    }

    override fun deserialize(decoder: Decoder): Distance {
        return Distance(decoder.decodeDouble())
    }

}
