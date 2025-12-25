package org.example.replacerchain;

public class DivReplacer implements Replacer {
    @Override
    public void doReplacer(StringBuilder sb) {
        // Use String.replaceAll with a case-insensitive pattern
        String replacedHtml = sb.toString().replaceAll("(?i)<div\\s*>", "  "+System.lineSeparator());
        replacedHtml = replacedHtml.replaceAll("(?i)</div\\s*>", "");
        sb.setLength(0); // Clear the StringBuilder
        sb.append(replacedHtml); // Append the replaced HTML back to the StringBuilder
    }
}
