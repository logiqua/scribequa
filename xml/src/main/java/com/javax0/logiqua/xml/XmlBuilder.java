package com.javax0.logiqua.xml;

import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;
import com.javax0.logiqua.scripts.ConstantValueNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class XmlBuilder {

    private final Document xmlObject;
    private final Engine engine;

    public static XmlBuilder from(Document xmlObject, Engine engine) {
        return new XmlBuilder(xmlObject, engine);
    }

    private XmlBuilder(Document xmlObject, Engine engine) {
        this.xmlObject = xmlObject;
        this.engine = engine;
    }

    public Script build() {
        final var rootNode = xmlObject.getDocumentElement();
        return build(xmlObject.getDocumentElement());
    }

    private Script build(Node node) throws IllegalArgumentException {
        if (node instanceof Element element) {
            final var tag = element.getTagName();
            return switch (tag) {
                case "op" -> {
                    final var symbol = element.getAttribute("symbol");
                    final var nodeBuilder = engine.getOp(symbol);
                    final var nodes = element.getChildNodes();
                    if (nodes.getLength() == 1 && nodes.item(0) instanceof Text) {
                        yield nodeBuilder.subscripts(new ConstantValueNode<>(nodes.item(0).getTextContent()));
                    }
                    final var script = new ArrayList<Script>();
                    for (int i = 0; i < nodes.getLength(); i++) {
                        final var n = nodes.item(i);
                        if (n instanceof Element) {
                            script.add(build(n));
                        }
                    }
                    yield nodeBuilder.subscripts(script.toArray(Script[]::new));
                }
                case "var" -> {
                    final var nodeBuilder = engine.getOp("var");
                    final var nodes = element.getChildNodes();
                    if (nodes.getLength() != 0) {
                        throw new IllegalArgumentException("The tag 'var' must not have any child nodes");
                    }
                    final var id = element.getAttribute("id");
                    yield nodeBuilder.subscripts(new ConstantValueNode<>(id));
                }
                case "constant" -> {
                    final var intValue = element.getAttribute("integer");
                    final var floatValue = element.getAttribute("float");
                    final var stringValue = element.getAttribute("string");
                    if (intValue.isEmpty() && floatValue.isEmpty() && stringValue.isEmpty()) {
                        throw new IllegalArgumentException("The tag 'constant' must have one of the attributes 'integer', 'float', or 'string'");
                    }
                    if (!intValue.isEmpty() && !floatValue.isEmpty() || !intValue.isEmpty() && !stringValue.isEmpty() || !floatValue.isEmpty() && !stringValue.isEmpty()) {
                        throw new IllegalArgumentException("The tag 'constant' must have only one of the attributes 'integer', 'float', or 'string'");
                    }
                    if (!intValue.isEmpty()) {
                        yield new ConstantValueNode<>(Long.parseLong(intValue));
                    } else if (!floatValue.isEmpty()) {
                        yield new ConstantValueNode<>(Double.parseDouble(floatValue));
                    } else {
                        yield new ConstantValueNode<>(stringValue);
                    }
                }
                case "list" -> {
                    final var nodes = element.getChildNodes();
                    final var script = new ArrayList<>();
                    for (int i = 0; i < nodes.getLength(); i++) {
                        final var n = nodes.item(i);
                        if (n instanceof Element e) {
                            final var eScript = build(e);
                            if (eScript instanceof ConstantValueNode<?>) {
                                script.add(eScript.evaluate());
                            } else {
                                throw new IllegalArgumentException("The node " + e + " is not supported in a list");
                            }
                        }
                    }
                    yield new ConstantValueNode<>(script.toArray(Object[]::new));
                }
                case "true" -> new ConstantValueNode<>(true);
                case "false" -> new ConstantValueNode<>(false);
                case "null" -> new ConstantValueNode<>(null);
                default -> throw new IllegalArgumentException("The tag '" + tag + "' is not supported");
            };
        } else if (node instanceof Text text) {
            return new ConstantValueNode<>(text.getData());
        }
        throw new IllegalArgumentException("The node " + node + " is not supported");
    }
}
