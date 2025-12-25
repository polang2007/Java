package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 3. <u> → 忽略（Markdown 无原生下划线，也可换成 <u></u> 自行扩展）*/
public class UReplacer implements Replacer {
    public void doReplacer(StringBuilder sb) {
        // Use String.replaceAll with a case-insensitive pattern
        String html = sb.toString();
        String replacedHtml = html.replaceAll("(?i)<(/?)u\\s*>", "");
        sb.setLength(0); // Clear the StringBuilder
        sb.append(replacedHtml); // Append the replaced HTML back to the StringBuilder
    }
}
