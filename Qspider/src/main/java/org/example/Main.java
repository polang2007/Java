package org.example;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String url = "https://blog.csdn.net/u010445301/article/details/111322569?spm=1001.2014.3001.5506";
        String url2 = "https://blog.csdn.net/weixin_45395059/article/details/126006369?spm=1001.2014.3001.5506";
        String url3 = "https://www.tzkczc.com/26_26045/13042057.html";
        String url4 = "https://www.chinanews.com.cn/gn/2025/02-20/10371783.shtml";
        String url5 = "https://cloud.tencent.com/developer/inventory/26218/article/1071304";
        String url6 = "https://www.smxcu.com/search.php?searchkey=从零开始&action=login&submit=";//燃文网搜索结果
        BehaviorSubject<String> observeUrl = BehaviorSubject.createDefault(url6);
        observeUrl.map(RetrofitManager::getBodyStrSync)
//                .map(s -> {
//                    Readability readability = new Readability(s);
//                    readability.init();
//                    return readability.outerHtml();
//                })
                .map(s -> new AnalyzeByJSoup().parse(s).getElements("class.content book.0@class.bookbox"))
                .subscribe(System.out::println);

    }
}