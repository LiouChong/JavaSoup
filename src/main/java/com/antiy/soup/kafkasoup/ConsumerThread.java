package com.antiy.soup.kafkasoup;

import com.antiy.soup.csdnsoup.CSDNSoup;

import java.util.List;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/9/3 14:41
 */
public class ConsumerThread extends Thread{
    private String html;
    private CSDNSoup csdnSoup;
    private List list;

    public ConsumerThread(String html, CSDNSoup csdnSoup, List list) {
        this.html = html;
        this.csdnSoup = csdnSoup;
        this.list = list;
    }

    @Override
    public void run() {
        System.out.println("--------------------------------");
        System.out.println("--------------------------------");
        System.out.println("开始第一个--" + System.currentTimeMillis());
        List<String> title = csdnSoup.getTitle(html);
        list.add(title);


        System.out.println("结束第一个--" + System.currentTimeMillis());
        System.out.println("--------------------------------");
        System.out.println("--------------------------------");
    }
}
