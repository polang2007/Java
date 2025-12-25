package org.example.readability;

public class GetContent {
    public static String getContent(String html) {
        Readability readability = new Readability(html);
        readability.init();
        return readability.getContent();
    }
}
