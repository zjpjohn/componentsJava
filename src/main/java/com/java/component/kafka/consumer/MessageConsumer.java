package com.java.component.kafka.consumer;

import com.java.component.kafka.common.KafkaConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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
 * Module Desc:com.java.component.kafka.consumer
 * User: zjprevenge
 * Date: 2016/9/6
 * Time: 15:38
 */

public abstract class MessageConsumer implements MessageHandle, CallBack {

    private Properties properties;

    private KafkaConsumer<String, String> consumer;

    private String topicName;

    private LinkedBlockingQueue<ConsumerRecord<String, String>> recordsQueue = new LinkedBlockingQueue<>(10000);

    private ExecutorService messageHandle = Executors.newFixedThreadPool(2);

    public MessageConsumer(Properties properties, String consumerGroup, String topicName) {
        this.properties = properties;
        properties.setProperty(KafkaConstants.KAFKA_CONSUMER_GROUP, consumerGroup);
        this.consumer = new KafkaConsumer<String, String>(properties);
        this.topicName = topicName;
        init();
    }

    public MessageConsumer(Properties properties, String topicName) {
        this.properties = properties;
        this.consumer = new KafkaConsumer<String, String>(properties);
        this.topicName = topicName;
        init();
    }

    public void init() {
        messageHandle.submit(new MessageTask());
        messageHandle.submit(new HandleTask());
    }

    public void fetchMessage() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            try {
                for (ConsumerRecord<String, String> record : records) {
                    recordsQueue.put(record);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("fetch message error:{}", e);
            }
        }
    }

    /**
     * 处理消息
     */
    public void onMessage() {
        while (true) {
            ConsumerRecord<String, String> record = null;
            try {
                record = recordsQueue.take();
                handle(record.value());
                //消息处理成功
                onSuccess();
            } catch (InterruptedException e) {
                //处理消息失败
                onError(e);
            } catch (Exception e) {
                //处理消息失败
                onError(e);
            }
        }
    }

    /**
     * 成功回调方法
     */
    @Override
    public void onSuccess() {
        consumer.commitAsync();
    }

    /**
     * 失败回调
     *
     * @param throwable
     */
    @Override
    public void onError(Throwable throwable) {

    }

    /**
     * 拉取消的任务
     */
    private class MessageTask implements Runnable {

        @Override
        public void run() {
            fetchMessage();
        }
    }

    /**
     * 处理消息任务
     */
    private class HandleTask implements Runnable {

        @Override
        public void run() {
            onMessage();
        }
    }
}
