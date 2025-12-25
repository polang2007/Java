package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 15. <cite> → 忽略（也可换成 *斜体* ）*/
public class CiteReplacer implements Replacer {
    private static final Pattern TAG = Pattern.compile("<(/?)cite\\s*>", 32);
    public void doReplacer(StringBuilder sb) {
        Matcher m = TAG.matcher(sb);
        int delta = 0;
        while (m.find()) {
            sb.replace(m.start() + delta, m.end() + delta, "");
            delta -= (m.end() - m.start());
        }
    }
}
