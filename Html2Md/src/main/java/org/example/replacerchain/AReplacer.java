package org.example.replacerchain;

/* 5. <a href="..."> → [...](...) */
public class AReplacer implements Replacer {
    private static final String TAG = "<(?i)a\\s+(?:href=\"([^\"]+)\"\\s*)?[^>]*>(.*?)</a>";

    public void doReplacer(StringBuilder sb) {
        // Use String.replaceAll with a case-insensitive pattern
        String replacedHtml = sb.toString().replaceAll(TAG, "[$2]($1)");
        sb.setLength(0);
        sb.append(replacedHtml);
    }
}
