package be.yellowduck.sports.gpx

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.stream.XMLStreamWriter

internal fun NodeList.items() : List<Node> {
    var items = mutableListOf<Node>()
    for (i in 0..this.length - 1) {
        items.add(item(i))
    }
    return items
}

internal fun NodeList.itemsByName(name: String) : List<Node> {
   return this.items().filter { it.nodeName == name }
}

internal fun NodeList.firstItemByName(name: String) : Node? {
    return this.itemsByName(name).firstOrNull()
}

internal fun Node.childrenByName(name: String) : List<Node> {
    return this.childNodes.itemsByName(name)
}

internal fun Node.firstChildByName(name: String) : Node? {
    return this.childNodes.firstItemByName(name)
}

internal fun Node.doubleAttribute(name: String) : Double {
    return this.attributes.getNamedItem(name).nodeValue.toDoubleOrNull() ?: 0.0
}

internal fun Node.stringAttribute(name: String) : String {
    return this.attributes.getNamedItem(name).nodeValue ?: ""
}

internal fun XMLStreamWriter.document(init: XMLStreamWriter.() -> Unit): XMLStreamWriter {
    this.writeStartDocument()
    this.init()
    this.writeEndDocument()
    return this
}

internal fun XMLStreamWriter.element(name: String, init: XMLStreamWriter.() -> Unit): XMLStreamWriter {
    this.writeStartElement(name)
    this.init()
    this.writeEndElement()
    return this
}

internal fun XMLStreamWriter.element(name: String, content: String) {
    element(name) {
        writeCharacters(content)
    }
}

internal fun XMLStreamWriter.attribute(name: String, value: String) = writeAttribute(name, value)
