package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 9. <code> → ` ` */
public class CodeReplacer implements Replacer {
    private static final String TAG = "<(?i)(/?)code\\s*>";
    public void doReplacer(StringBuilder sb) {
//        Matcher m = TAG.matcher(sb);
//        int delta = 0;
//        while (m.find()) {
//            String md = m.group(1).isEmpty() ? "`" : "`";
//            sb.replace(m.start() + delta, m.end() + delta, md);
//            delta += md.length() - (m.end() - m.start());
//        }
        // Use String.replaceAll with a case-insensitive pattern
        String replacedHtml = sb.toString().replaceAll(TAG, "");
        sb.setLength(0);
        sb.append(replacedHtml);
    }
}
