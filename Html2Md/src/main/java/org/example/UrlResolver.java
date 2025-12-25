package org.example;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlResolver {

    /**
     * 将相对 URL 转换为绝对 URL
     *
     * @param baseUrl   页面的基础 URL (例如: https://example.com/path/page.html)
     * @param targetUrl 链接中的相对路径 (例如: ../img/logo.png 或 //other.com/new)
     * @return 绝对路径 URL，如果解析失败则返回 null
     */
    public static String resolveUrl(String baseUrl, String targetUrl) {
        // 1. 空值检查
        if (baseUrl == null || targetUrl == null) {
            return null;
        }

        // 2. 去除首尾空格
        baseUrl = baseUrl.trim();
        targetUrl = targetUrl.trim();

        if (baseUrl.isEmpty() || targetUrl.isEmpty()) {
            return null;
        }

        try {
            // 3. 处理特殊情况：如果 targetUrl 本身就是完整的协议链接 (http/https)
            // 或者是协议相对链接 (//xxx)
            if (targetUrl.startsWith("http://") || targetUrl.startsWith("https://")) {
                return targetUrl;
            }

            // 处理协议相对链接，例如 //cdn.example.com/img.png
            if (targetUrl.startsWith("//")) {
                // 获取 baseUrl 的协议 (http 或 https)
                URL base = new URL(baseUrl);
                return base.getProtocol() + ":" + targetUrl;
            }

            // 4. 核心逻辑：使用 URI 的 resolve 方法
            // URI 会自动处理路径层级 (如 ../ 和 ./)
            URI baseUri = new URI(baseUrl);
            URI resolved = baseUri.resolve(targetUrl);

            // 5. 规范化路径，去除多余的 . 和 ..
            URI normalized = resolved.normalize();

            return normalized.toString();

        } catch (URISyntaxException | MalformedURLException e) {
            // 解析出错，例如格式不正确
            System.err.println("URL 解析错误: " + e.getMessage());
            return null;
        }
    }
    public static String resolveUrlByJsoup(String html, String baseUrl){
        Document doc = Jsoup.parse(html,baseUrl);
        Elements imgs = doc.select("img[src]");
        imgs.forEach(img -> img.attr("src", img.attr("abs:src")));
        return doc.html();
    }

    // --- 测试主函数 ---
    public static void main(String[] args) {
        String pageUrl = "https://www.example.com/blog/article.html";

        // 测试各种情况
        System.out.println(resolveUrl(pageUrl, "/static/logo.png"));
        // 输出: https://www.example.com/static/logo.png

        System.out.println(resolveUrl(pageUrl, "../contact.html"));
        // 输出: https://www.example.com/blog/../contact.html -> normalize后: https://www.example.com/contact.html

        System.out.println(resolveUrl(pageUrl, "images/pic.jpg"));
        // 输出: https://www.example.com/blog/images/pic.jpg

        System.out.println(resolveUrl(pageUrl, "//cdn.example.com/js/app.js"));
        // 输出: https://cdn.example.com/js/app.js (自动补全了协议)

        System.out.println(resolveUrl(pageUrl, "https://other-site.com/"));
        // 输出: https://other-site.com/ (保持不变)
    }
}

