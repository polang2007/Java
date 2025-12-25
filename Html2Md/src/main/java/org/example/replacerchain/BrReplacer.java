package org.example.replacerchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 7. <br> → 两个空格 + 换行（Markdown 硬换行）*/
public class BrReplacer implements Replacer {
    private static final Pattern TAG = Pattern.compile("<br\\s*/?>", 32);
    public void doReplacer(StringBuilder sb) {
        Matcher m = TAG.matcher(sb);
        int delta = 0;
        while (m.find()) {
            String md = "  \n";
            sb.replace(m.start() + delta, m.end() + delta, md);
            delta += md.length() - (m.end() - m.start());
        }
    }
}
