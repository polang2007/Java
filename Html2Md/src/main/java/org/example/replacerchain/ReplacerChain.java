package org.example.replacerchain;

import java.util.ArrayList;
import java.util.List;

public class ReplacerChain implements Replacer {
    private final List<Replacer> replacers = new ArrayList<>();

    public ReplacerChain addReplacer(Replacer replacer) {
        replacers.add(replacer);
        return this;
    }

    @Override
    public void doReplacer(StringBuilder html) {
        replacers.forEach(r -> r.doReplacer(html));
    }
}
