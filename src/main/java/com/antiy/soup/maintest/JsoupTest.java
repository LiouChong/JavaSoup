package com.antiy.soup.maintest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/9/2 17:01
 */
public class JsoupTest {
    public static void main(String[] args) {
        List<String> titles = new ArrayList<>();
        List<String> urls = new ArrayList<>();

        String html =
            "<html>" +
                "<div id=\"blog_list\">" +
                    "<div class=\"blog_title\">" +
                        "<a href=\"url1\">第一篇博客</a>" +
                    "</div>" +
                    "<div class=\"blog_title\">" +
                        "<a href=\"url2\">第二篇博客</a>" +
                    "</div>" +
                    "<div class=\"blog_title\">" +
                        "<a href=\"url3\">第三篇博客</a>" +
                    "</div>" +
                "</div>" +
            "</html>";
        Document doc = Jsoup.parse(html);

        Elements elements = doc.select("div[class=blog_title]");

        for (Element element : elements) {
            System.out.println(element.select("a").attr("href"));
        }


    }

}
