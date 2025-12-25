package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 10. <ul> → 忽略（后面 <li> 负责 * ）*/
public class UlReplacer implements Replacer {
    private static final Pattern TAG = Pattern.compile("<(/?)ul\\s*>", 32);
    public void doReplacer(StringBuilder sb) {
        Matcher m = TAG.matcher(sb);
        int delta = 0;
        while (m.find()) {
            sb.replace(m.start() + delta, m.end() + delta, "");
            delta -= (m.end() - m.start());
        }
    }
}
