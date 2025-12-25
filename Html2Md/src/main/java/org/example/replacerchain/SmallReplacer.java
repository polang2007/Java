package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 16. <small> → 忽略（Markdown 无小号字）*/
public class SmallReplacer implements Replacer {
    private static final Pattern TAG = Pattern.compile("<(/?)small\\s*>", 32);
    public void doReplacer(StringBuilder sb) {
        Matcher m = TAG.matcher(sb);
        int delta = 0;
        while (m.find()) {
            sb.replace(m.start() + delta, m.end() + delta, "");
            delta -= (m.end() - m.start());
        }
    }
}
