package com.antiy.soup.csdnsoup;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.awt.image.ImageWatched;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/9/2 17:10
 */
public class CSDNSoup {
    public static void main(String[] args) throws IOException, InterruptedException {
        poolThread();
    }

    private static void poolThread() throws IOException, InterruptedException {
        CSDNSoup csdnSoup = new CSDNSoup();
        String url = "https://bbs.csdn.net/forums/J2EE";
        List<List<String>> title = Collections.synchronizedList(new LinkedList<>());
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            SingleSoup singleSoup = new SingleSoup(csdnSoup, url, i, title, countDownLatch);
            executorService.submit(singleSoup);
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("共花取" + (end - start));
        executorService.shutdown();
    }

    static class SingleSoup extends Thread{
        private CSDNSoup csdnSoup;
        private String url;
        private Integer i;
        private List<List<String>> title;
        private CountDownLatch countDownLatch;

        public SingleSoup(CSDNSoup c, String url, Integer i, List<List<String>> title, CountDownLatch countDownLatch) {
            this.csdnSoup = c;
            this.url = url;
            this.i = i;
            this.title = title;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            String pageUrl = csdnSoup.getUrl(url, i);
            String htmlString = null;
            try {
                htmlString = csdnSoup.getHtmlString(pageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> pageTitles = csdnSoup.getTitle(htmlString);
            title.add(pageTitles);
            System.out.println("已完成第" + (i + 1) + "页");
            countDownLatch.countDown();
        }
    }

    private static void singleThread() throws IOException {
        CSDNSoup csdnSoup = new CSDNSoup();
        String url = "https://bbs.csdn.net/forums/J2EE";
        List<List<String>> title = new LinkedList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            String pageUrl = csdnSoup.getUrl(url, i);
            String htmlString = null;
            htmlString = csdnSoup.getHtmlString(pageUrl);
            List<String> pageTitles = csdnSoup.getTitle(htmlString);
            title.add(pageTitles);
            System.out.println("已完成第" + (i + 1) + "页");
        }
        long end = System.currentTimeMillis();
        System.out.println("共花取" + (end - start));
    }

    private List<String> getTitle(String html) {
        Document parse = Jsoup.parse(html);
        Elements select = parse.select("td[class=forums_topic]").select("a[class=forums_title]");
        List<String> pageTitleString = new LinkedList<>();
        for (Element element : select) {
            pageTitleString.add(element.text());
        }

        return pageTitleString;
    }

    private String getHtmlString(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        //设置头部信息进行模拟登录（添加登录后的Cookie）
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml," +
                "application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch, br");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpGet.setHeader("Cookie", "2760826815674153984988421e417b6ad0625da239c28233953cdac85d708a");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36" +
                " (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

        CloseableHttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        String content = "";

    if (statusCode == 200) {
        HttpEntity entity = response.getEntity();
        content = EntityUtils.toString(entity);

        EntityUtils.consume(entity);
    } else {
        System.out.println("请求失败: " + response.getEntity().getContent() + "-" + statusCode);
    }
        return content;
    }

    private String getUrl(String url, Integer page) {
        if (page == null || page == 0) {
            return url;
        } else {
            return url + "?page=" + (page + 1);
        }
    }
}
