package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 12. <li> → * / 1.  （根据父 ul/ol 决定，这里简化成 * ）*/
public class LiReplacer implements Replacer {
    private static final Pattern TAG = Pattern.compile("<(/?)li\\s*>", 32);
    public void doReplacer(StringBuilder sb) {
        Matcher m = TAG.matcher(sb);
        int delta = 0;
        while (m.find()) {
            String md = m.group(1).isEmpty() ? "\n* " : "";
            sb.replace(m.start() + delta, m.end() + delta, md);
            delta += md.length() - (m.end() - m.start());
        }
    }
}
