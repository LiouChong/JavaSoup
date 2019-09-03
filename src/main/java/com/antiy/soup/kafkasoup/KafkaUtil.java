package com.antiy.soup.kafkasoup;

import com.antiy.soup.csdnsoup.CSDNSoup;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * Author: Liuchong
 * Description:
 * date: 2019/9/3 13:27
 */
public class KafkaUtil {
    private static final String TOPIC = "soup";
    private static final CSDNSoup csdnSoup = new CSDNSoup();

    KafkaProducer getKafkaProducer(){
        Properties props = new Properties();

        props.put("bootstrap.servers", "192.168.116.115:9092");//xxx服务器ip
        props.put("acks", "all");//所有follower都响应了才认为消息提交成功，即"committed"
        props.put("retries", 0);//retries = MAX 无限重试，直到你意识到出现了问题:)
        props.put("batch.size", 16384);//producer将试图批处理消息记录，以减少请求次数.默认的批量处理消息字节数
        //batch.size当批量的数据大小达到设定值后，就会立即发送，不顾下面的linger.ms
        props.put("linger.ms", 1);//延迟1ms发送，这项设置将通过增加小的延迟来完成--即，不是立即发送一条记录，producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理
        props.put("buffer.memory", 33554432);//producer可以用来缓存数据的内存大小。
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<String, String>(props);
    }

    void sendMsg(KafkaProducer kafkaProducer,String msg) {
        kafkaProducer.send(new ProducerRecord<>(TOPIC, msg));
    }

    KafkaConsumer getKafkaConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.116.115:9092");
        // 消费者组名称（id）
        props.put("group.id", "my-replicated-topic");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        return consumer;
    }
    void getMsg(KafkaConsumer kafkaConsumer, ExecutorService executorService, List l) {
        kafkaConsumer.subscribe(Collections.singleton(TOPIC));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(500);
            for (ConsumerRecord<String, String> record : records) {
                ConsumerThread c = new ConsumerThread(record.value(), csdnSoup, l);
                executorService.submit(c);
            }
        }
    }
}
