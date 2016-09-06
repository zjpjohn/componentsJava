package com.java.component.kafka.producer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * ━━━━━━oooo━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃stay hungry stay foolish
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━萌萌哒━━━━━━
 * Module Desc:com.java.component.kafka.producer
 * User: zjprevenge
 * Date: 2016/9/6
 * Time: 16:27
 */

public class MessageProvider {

    private KafkaConnectionPool producerPool;

    public MessageProvider() {
    }

    public MessageProvider(KafkaConnectionPool producerPool) {
        this.producerPool = producerPool;
    }

    public KafkaConnectionPool getProducerPool() {
        return producerPool;
    }

    public void setProducerPool(KafkaConnectionPool producerPool) {
        this.producerPool = producerPool;
    }

    /**
     * 发送消息
     *
     * @param topicName
     * @param message
     */
    public void sendMessage(String topicName, String message) {
        Producer<String, String> producer = producerPool.getConnection();
        try {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topicName, message);
            producer.send(producerRecord);
        } finally {
            producerPool.returnConnection(producer);
        }
    }
}
