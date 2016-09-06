package com.java.component.kafka.producer;

import com.java.component.kafka.common.ConnectionFactory;
import com.java.component.kafka.common.KafkaConstants;
import com.java.component.kafka.exec.ConnectionException;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

/**
 * ━━━━━━南无阿弥陀佛━━━━━━
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
 * Time: 14:35
 */

public class KafkaConnectionFactory implements ConnectionFactory<Producer<String, String>> {


    private static final long serialVersionUID = -6325485139355903758L;

    private final Properties properties;

    public KafkaConnectionFactory(Properties properties) {
        String brokerServers = properties.getProperty(KafkaConstants.BROKER_SERVER_LIST);
        if (brokerServers == null) {
            throw new ConnectionException(KafkaConstants.BROKER_SERVER_LIST + " is required...");
        }
        this.properties = properties;
    }

    public KafkaConnectionFactory(final String brokers,
                                  final String acks,
                                  final String codec,
                                  final String batch) {
        properties = new Properties();
        properties.setProperty(KafkaConstants.BROKER_SERVER_LIST, brokers);
        properties.setProperty(KafkaConstants.REQUEST_ACKS_PROPERTY, acks);
        properties.setProperty(KafkaConstants.BATCH_SIZE_CONFIG, batch);
        properties.setProperty(KafkaConstants.COMPRESSION_TYPE_CONFIG, codec);

    }

    @Override
    public PooledObject<Producer<String, String>> makeObject() throws Exception {
        Producer<String, String> producer = createConnection();
        return new DefaultPooledObject<>(producer);
    }

    @Override
    public void destroyObject(PooledObject<Producer<String, String>> pooledObject) throws Exception {
        Producer<String, String> producer = pooledObject.getObject();
        if (producer != null) {
            producer.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<Producer<String, String>> pooledObject) {
        Producer<String, String> producer = pooledObject.getObject();
        return null != producer;
    }

    @Override
    public void activateObject(PooledObject<Producer<String, String>> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<Producer<String, String>> pooledObject) throws Exception {

    }

    /**
     * 创建连接对象
     *
     * @return
     * @throws Exception
     */
    @Override
    public Producer<String, String> createConnection() throws Exception {
        return new KafkaProducer<String, String>(properties);
    }
}
