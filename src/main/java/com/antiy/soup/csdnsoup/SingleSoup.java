package com.antiy.soup.csdnsoup;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/9/3 14:05
 */
public class SingleSoup extends Thread {
    private CSDNSoup csdnSoup;
    private String url;
    private Integer pageIndex;
    private List<List<String>> titles;
    private CountDownLatch countDownLatch = null;

    public SingleSoup(CSDNSoup c, String url, Integer pageIndex, List<List<String>> titles, CountDownLatch countDownLatch) {
        this.csdnSoup = c;
        this.url = url;
        this.pageIndex = pageIndex;
        this.titles = titles;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        for (int i = pageIndex; i < 10; i++) {
            oprete(i);
        }
    }

    private void oprete(Integer page) {
        // 拼装url
        String pageUrl = csdnSoup.getUrl(url, page);
        String htmlString = null;
        try {
            // 获取得到页面的html信息
            htmlString = csdnSoup.getHtmlString(pageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 解析html信息得到目标信息
        List<String> pageTitles = csdnSoup.getTitle(htmlString);
        titles.add(pageTitles);
        System.out.println("已完成第" + (page + 1) + "页");
        countDownLatch.countDown();
    }
}
