package org.example.readability;

import java.util.HashMap;
import java.util.Map;

public class GetContent {
    private static Map<String, String> webPageContentLabels = new HashMap<>();
    static{webPageContentLabels.put("https://blog.csdn.net/", "id.content_views.0@all");}
    public static String getContent(String content, String url) {
        return webPageContentLabels.keySet().stream()
                .filter(url::startsWith)
                .findFirst()
                .map(key -> {
                    System.out.println("Get content from " + key);
                    return new AnalyzeByJSoup().parse(content).getString(webPageContentLabels.get(key));
                })
                .orElseGet(() -> {
                    System.out.println("Get content from default");
                    Readability readability = new Readability(content);
                    readability.init();
                    return readability.getContent();
                });
    }
}
