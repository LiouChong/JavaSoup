package com.antiy.soup.kafkasoup;

import com.antiy.soup.csdnsoup.CSDNSoup;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/9/3 14:05
 */
public class ProviderThread extends Thread {
    private KafkaUtil kafkaUtil = new KafkaUtil();
    private CSDNSoup csdnSoup;
    private String url;
    private Integer pageIndex;
    private CountDownLatch countDownLatch;


    public ProviderThread(CSDNSoup c, String url, Integer pageIndex, CountDownLatch countDownLatch) {
        this.csdnSoup = c;
        this.url = url;
        this.pageIndex = pageIndex;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        // 拼装url
        String pageUrl = csdnSoup.getUrl(url, pageIndex);
        String htmlString = null;
        try {
            // 获取得到页面的html信息
            htmlString = csdnSoup.getHtmlString(pageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        KafkaProducer kafkaProducer = kafkaUtil.getKafkaProducer();
        kafkaUtil.sendMsg(kafkaProducer, htmlString);
        System.out.println("完成" + pageIndex + "页爬取");
        countDownLatch.countDown();
    }

}
