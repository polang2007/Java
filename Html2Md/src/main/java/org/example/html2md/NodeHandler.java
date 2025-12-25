package org.example.html2md;

import org.jsoup.nodes.Element;

@FunctionalInterface
public interface NodeHandler {
    void handle(Element element, StringBuilder sb, Html2Md context);
}
