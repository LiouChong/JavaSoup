package com.antiy.soup.kafkasoup;


import com.antiy.soup.csdnsoup.CSDNSoup;
import org.omg.CORBA.INTERNAL;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/9/3 10:00
 */
public class SoupProvider {
    private static final String URL = "https://bbs.csdn.net/forums/J2EE";
    private static final Integer PAGE_COUNT = 20;

    public static void main(String[] args) throws InterruptedException {
        CSDNSoup csdnSoup = new CSDNSoup();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(PAGE_COUNT);
        for (int i = 0; i < PAGE_COUNT; i++) {
            ProviderThread singleSoup = new ProviderThread(csdnSoup, URL, i, countDownLatch);
            executorService.submit(singleSoup);
        }
        long l = System.currentTimeMillis();
        countDownLatch.await();
        System.out.println("爬取20页共花费 : " + (System.currentTimeMillis() - l ));
    }

}
