package com.antiy.soup.kafkasoup;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import sun.plugin.javascript.navig.Link;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/9/3 10:02
 */
public class SoupConsumer {
    private static final KafkaUtil kafkaUtil = new KafkaUtil();

    public static void main(String[] args) {
        List<List<String>> lists = Collections.synchronizedList(new LinkedList<>());

        kafkaUtil.getMsg(kafkaUtil.getKafkaConsumer(), Executors.newFixedThreadPool(3), lists);
    }
}
