package org.example.replacerchain;

public class Html2Md {
    final static ReplacerChain simpleTextReplacerChain = new ReplacerChain();
    final static ReplacerChain basicReplacerChain = new ReplacerChain();
    static {
        simpleTextReplacerChain
                .addReplacer(new DivReplacer())
                .addReplacer(new BReplacer())
                .addReplacer(new IReplacer())
                .addReplacer(new UReplacer());
        basicReplacerChain
                .addReplacer(simpleTextReplacerChain)
                .addReplacer(new AReplacer())
                .addReplacer(new CodeReplacer())
                .addReplacer(new ImgReplacer())
                .addReplacer(new PReplacer())
                .addReplacer(new PreReplacer())
                .addReplacer(new SpanReplacer());
    }
    public static StringBuilder parse(String html) {
        final StringBuilder sb = new StringBuilder(html);
        basicReplacerChain.doReplacer(sb);
        return sb;
    }
}
