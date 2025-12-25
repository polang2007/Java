package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 6. <p> → 两个换行 */
public class PReplacer implements Replacer {
    @Override
    public void doReplacer(StringBuilder sb) {
        // Use String.replaceAll with a case-insensitive pattern
        String replacedHtml = sb.toString().replaceAll("(?i)<p\\s*>", "  "+System.lineSeparator());
        replacedHtml = replacedHtml.replaceAll("(?i)</p\\s*>", "");
        sb.setLength(0); // Clear the StringBuilder
        sb.append(replacedHtml); // Append the replaced HTML back to the StringBuilder
    }
}
