package org.example;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import org.example.file.FileWriter;
import org.example.readability.AnalyzeByJSoup;
import org.example.readability.GetContent;
import org.example.readability.Readability;
import org.example.replacerchain.Html2Md;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import java.util.Optional;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Html2Md {
    public static void main(String[] args) {
        String url = "https://blog.csdn.net/u010445301/article/details/111322569?spm=1001.2014.3001.5506";
        String url2 = "https://blog.csdn.net/weixin_45395059/article/details/126006369?spm=1001.2014.3001.5506";
        String url3 = "https://www.tzkczc.com/26_26045/13042057.html";
        String url4 = "https://www.chinanews.com.cn/gn/2025/02-20/10371783.shtml";
        String url5 = "https://cloud.tencent.com/developer/inventory/26218/article/1071304";
        BehaviorSubject<String> observeUrl = BehaviorSubject.createDefault(url);
        observeUrl.map(s -> RetrofitManager.getInstance().getBodyStrSync(s))
                .filter(Optional::isPresent)
                .map(Optional::get)
//                .map(GetContent::getContent)
                .map(s ->  new AnalyzeByJSoup().parse(s).getString("id.content_views.0@all"))
                .map(html -> UrlResolver.resolveUrlByJsoup(html, observeUrl.getValue()))
                .map(s -> Jsoup.clean(s, Safelist.relaxed()))
//                .map(s -> Html2Md.getInstance().parse(observeUrl.getValue(), s))
                .map(Html2Md::parse)
                .subscribe(s -> FileWriter.writeToFile("test1.md", s.toString()));
    }
}