package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 17. <span> → 忽略（纯行内容器）*/
public class SpanReplacer implements Replacer {
    private static final String TAG = "<(?i)(/?)span\\s*>";
    public void doReplacer(StringBuilder sb) {
        // Use String.replaceAll with a case-insensitive pattern
        String replacedHtml = sb.toString().replaceAll(TAG, "");
        sb.setLength(0);
        sb.append(replacedHtml);
    }
}
