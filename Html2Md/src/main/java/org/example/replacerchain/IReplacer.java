package org.example.replacerchain;

public class IReplacer implements Replacer {
    @Override
    public void doReplacer(StringBuilder sb) {
        // Use String.replaceAll with a case-insensitive pattern
        String html = sb.toString();
        String replacedHtml = html.replaceAll("(?i)<(/?)(i|em)\\s*>", "*");
        sb.setLength(0); // Clear the StringBuilder
        sb.append(replacedHtml); // Append the replaced HTML back to the StringBuilder
    }
}


