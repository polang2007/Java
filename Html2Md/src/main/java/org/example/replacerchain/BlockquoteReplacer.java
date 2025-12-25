package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 13. <blockquote> → > > */
public class BlockquoteReplacer implements Replacer {
    private static final Pattern TAG = Pattern.compile("<(/?)blockquote\\s*>", 32);
    public void doReplacer(StringBuilder sb) {
        Matcher m = TAG.matcher(sb);
        int delta = 0;
        while (m.find()) {
            String md = m.group(1).isEmpty() ? "\n> " : "\n";
            sb.replace(m.start() + delta, m.end() + delta, md);
            delta += md.length() - (m.end() - m.start());
        }
    }
}
