package org.example.replacerchain;

public interface Replacer {
    /**
     * 把 html 中的对应标签替换为 Markdown 格式
     * 实现类可原地修改 StringBuilder，也可返回新字符串
     */
    void doReplacer(StringBuilder html);
}