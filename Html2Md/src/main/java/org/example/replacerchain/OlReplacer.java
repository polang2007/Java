package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 11. <ol> → 忽略（后面 <li> 负责 1. ）*/
public class OlReplacer implements Replacer {
    private static final Pattern TAG = Pattern.compile("<(/?)ol\\s*>", 32);
    public void doReplacer(StringBuilder sb) {
        Matcher m = TAG.matcher(sb);
        int delta = 0;
        while (m.find()) {
            sb.replace(m.start() + delta, m.end() + delta, "");
            delta -= (m.end() - m.start());
        }
    }
}
