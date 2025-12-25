package org.example.html2md;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Html2Md {
    private static final Html2Md instance = new Html2Md();

    private Html2Md() {
    }

    public static Html2Md getInstance() {
        return instance;
    }

    static String baseUri;
    static Map<String, BiConsumer<Element, StringBuilder>> nodeMap = new HashMap<String, BiConsumer<Element, StringBuilder>>() {{
        put("a", Html2Md::nodeAReplace);
        put("img", Html2Md::nodeImgReplace);
        put("h1", Html2Md::nodeH16Replace);
        put("h2", Html2Md::nodeH16Replace);
        put("h3", Html2Md::nodeH16Replace);
        put("h4", Html2Md::nodeH16Replace);
        put("h5", Html2Md::nodeH16Replace);
        put("h6", Html2Md::nodeH16Replace);
        put("li", Html2Md::nodeLiReplace);
        put("ul", Html2Md::nodeUlReplace);
        put("strong", Html2Md::nodeStrongReplace);
        put("code", Html2Md::nodeCodeReplace);
        put("pre", Html2Md::nodePreReplace);
    }};


    public void parse(String url, String html, StringBuilder sb) {
        Html2Md.baseUri = url;
        Node node = Jsoup.parse(html);
        parse(node, sb);
    }
    public StringBuilder parse(String url, String html) {
        StringBuilder sb = new StringBuilder();
        parse(url, html, sb);
        //StringEscapeUtils.unescapeHtml4()方法用于将HTML实体字符转义为对应的字符
        return new StringBuilder(StringEscapeUtils.unescapeHtml4(sb.toString()).trim());
    }
    private String urlRegex(String line) {
        // 按指定模式在字符串查找
        String pattern = "(http|https)://(www.)?(\\w+(\\.)?)+";
        String result = "";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            result=m.group(0);
            System.out.println("Found value: " + m.group(0) );
        } else {
            System.out.println("NO MATCH");
        }
        return result;
    }
    //深度优先遍历
    private void parse(Node node, StringBuilder sb) {
        //如果是文本节点，直接输出文本
        if (node instanceof TextNode) {
            sb.append(((TextNode) node).text());
            return;
        }
        //如果是nodeMap中定义的标签节点，则调用对应的替换方法
        if (nodeMap.containsKey(node.nodeName())) {
            nodeReplace((Element) node, sb, nodeMap.get(node.nodeName()));
        } else {
            //否则遍历子节点
            node.childNodes().forEach(child -> {
                        parse(child, sb);
                    }
            );
            //如果是块级元素，则换行
            if ("br*p*div*hr".contains(node.nodeName())) {
                sb.append("  \r\n");
            }
        }
    }

    //替换节点方法
    private void nodeReplace(Element element, StringBuilder sb, BiConsumer<Element, StringBuilder> consumer) {
        consumer.accept(element, sb);
    }

    //a标签替换
    private static void nodeAReplace(Element element, StringBuilder sb) {
        //TODO: 处理a标签的href属性,有可能是页面内部跳转,目前CSDN跳转的id都在a标签中，可以直接不动。发现有其他情况再来处理
        //TODO: a标签目前有转义字符，需要处理
        //如果a标签没有href和id属性，则不处理
        String href = "";
        try {
            href = URLDecoder.decode(element.attributes().get("href"), String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        element.attr("href", href);
        if ((element.attr("href") == null) && (element.attr("id") == null)) {
            //遍历子节点
            element.childNodes().forEach(child -> {
                        instance.parse(child, sb);
                    }
            );
            return;
        }
        //如果a标签是页面内部跳转，则原样输出
        if (element.attr("href").startsWith("#") || (element.attr("id") != null)) {
            sb.append(element.outerHtml().substring(0, element.outerHtml().indexOf(">") + 1));
            //遍历子节点
            element.childNodes().forEach(child -> {
                        instance.parse(child, sb);
                    }
            );
            sb.append("</a>");
            return;
        }
//        String href = (element.attr("href").startsWith("http")) ? element.attr("href") : baseUri + element.attr("href");
        href = completeImageUrlSafe(element.attr("href"), baseUri);
        sb.append("[");
        //遍历子节点
        element.childNodes().forEach(child -> {
                    instance.parse(child, sb);
                }
        );
        sb.append("](" + href + ")");
    }

    //Img标签替换
    private static void nodeImgReplace(Element element, StringBuilder sb) {
        //TODO: 将图片下载，转换成base64编码，然后插入到md文档中
//        String href = (element.attr("src").startsWith("http")) ? element.attr("src") : baseUri + element.attr("href");
        String href = completeImageUrlSafe(element.attr("src"), baseUri);
        sb.append("![" + element.attr("alt") + "]").append("(" + href + ")");
//        sb.append("![" + element.attr("alt") + "]").append("(https:" + element.attr("src") + ")");
    }

    //H1-H6标签替换
    private static void nodeH16Replace(Element element, StringBuilder sb) {
        int level = Integer.parseInt(element.nodeName().substring(1));
        level = level == 6 ? 5 : level;
        sb.append(StringUtils.repeat("#", level) + " ");
        //遍历子节点
        element.childNodes().forEach(child -> {
                    instance.parse(child, sb);
                }
        );

        sb.append("  \r\n");
    }

    //Li标签替换
    private static void nodeLiReplace(Element element, StringBuilder sb) {
        sb.append("- ");
        //遍历子节点
        element.childNodes().forEach(child -> {
                    instance.parse(child, sb);
                }
        );
        sb.append("  \r\n");
    }

    //Ul标签替换
    private static void nodeUlReplace(Element element, StringBuilder sb) {
        boolean isNested;
        //如果ul标签是父节点，则是嵌套的ul，先换行
        if (element.parent().nodeName().equals("li")) {
            sb.append("  \r\n");
            isNested = true;
        } else {
            isNested = false;
        }
        //遍历子节点
        element.childNodes().forEach(child -> {
                    if (isNested) {
                        sb.append("  ");
                    }
                    instance.parse(child, sb);
                }
        );
        sb.append("  \r\n");
    }

    //Strong标签替换
    private static void nodeStrongReplace(Element element, StringBuilder sb) {
        sb.append("**");
        //遍历子节点
        element.childNodes().forEach(child -> {
                    instance.parse(child, sb);
                }
        );
        sb.append("**");
    }
    //Code标签替换
    private static void nodeCodeReplace(Element element, StringBuilder sb) {
        //TODO: 不好判断代码块内容是否是一个简单词汇，还是代码块，目前认为是代码块会用<pre>标签包裹,主要再pre标签中处理
        //如果代码块内容长度大于30，是代码块，则用三个反引号包裹，否则用一个反引号包裹
//        if (element.text().length() > 20) {
//            sb.append("```java  \r\n");
//            sb.append(element.html().replaceAll("\n", "  \r\n").replaceAll("<.+?>", ""));
//            sb.append("  \r\n```");
//        } else {
//            sb.append("`" + element.text() + "`");
//        }
        sb.append("`" + element.text() + "`");
    }
    //Pre标签替换
    private static void nodePreReplace(Element element, StringBuilder sb) {
        sb.append("```java  \r\n");
        sb.append(element.html().replaceAll("\n", "  \r\n").replaceAll("<.+?>", ""));
        sb.append("  \r\n```  \r\n");
    }
    public static String completeImageUrl(String imageUrl, String currentPageUrl) throws URISyntaxException {
        // 处理空值情况（根据需求决定是否抛出异常）
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be empty");
        }

        URI baseUri = new URI(currentPageUrl);
        URI resolvedUri = baseUri.resolve(imageUrl);
        return resolvedUri.normalize().toString();
    }

    // 重载方法：添加默认异常处理
    public static String completeImageUrlSafe(String imageUrl, String currentPageUrl) {
        try {
            return completeImageUrl(imageUrl, currentPageUrl);
        } catch (URISyntaxException e) {
            return imageUrl; // 或返回null/默认值，根据需求调整
        }
    }
}
