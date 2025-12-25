package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 8. <pre> → ``` ``` */
public class PreReplacer implements Replacer {
    private static final String TAG = "<(?i)(/?)pre\\s*>";
    public void doReplacer(StringBuilder sb) {
        // Use String.replaceAll with a case-insensitive pattern
        String replacedHtml = sb.toString().replaceAll(TAG, System.lineSeparator() + "```" + System.lineSeparator());
        sb.setLength(0);
        sb.append(replacedHtml);
    }
}
