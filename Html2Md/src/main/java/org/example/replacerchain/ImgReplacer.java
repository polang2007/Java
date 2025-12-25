package org.example.replacerchain;

import okhttp3.ResponseBody;
import org.example.RetrofitManager;
import retrofit2.Response;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImgReplacer implements Replacer {

    // TODO: 图片标签正则表达式问题：不能匹配双斜杠(相对路径)的情况
    private static final String TAG = "<(?i)img\\s+(?:[^>]*?\\s+)?src=[\"'](.*?)[\"'][^>]*?>";

    /* 下载 -> base64 -> data URI */
    private static String toDataUri(String pic, String url) {
        Response<ResponseBody> resp = RetrofitManager.getInstance().getResponse(url);
        if (!resp.isSuccessful()) return null;
        ResponseBody body = resp.body();
        if (body == null) return null;
        byte[] bytes = null;
        try {
            bytes = body.bytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String base64 = Base64.getEncoder().encodeToString(bytes);
        return "  " + System.lineSeparator() + "[" + pic + "]:data:image/png;base64," + base64;
    }

    @Override
    public void doReplacer(StringBuilder sb) {
        Pattern pattern = Pattern.compile(TAG);
        Matcher matcher = pattern.matcher(sb);
        // 创建一个新的 StringBuilder 来构建替换后的文本
        StringBuilder replacedHtml = new StringBuilder();
        int lastEnd = 0;
        // 用于存储捕获的 $1 内容
//        List<String> capturedUrls = new ArrayList<>();
        Map<String, String> capturedUrls = new HashMap<>();
        while (matcher.find()) {
            // 将匹配前的部分添加到替换后的文本中
            replacedHtml.append(sb, lastEnd, matcher.start());
            // 捕获 $1 的内容
            String capturedUrl = matcher.group(1);
//            System.out.println("-------------------------" + capturedUrl);
            String key = capturedUrl.substring(capturedUrl.lastIndexOf("/") + 1).replaceAll("\\.\\w+$", "");
            capturedUrls.put(key, capturedUrl);
            // 进行替换
            replacedHtml.append("![][").append(key).append("]");
            // 更新 lastEnd 以便在下一次迭代中使用
            lastEnd = matcher.end();
        }
        // 将剩余的部分添加到替换后的文本中
        replacedHtml.append(sb.substring(lastEnd));
        sb.setLength(0);
        sb.append(replacedHtml);
        capturedUrls.keySet().forEach(key -> {
            sb.append(toDataUri(key, capturedUrls.get(key)));
        });
    }
}

