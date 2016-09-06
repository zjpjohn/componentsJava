package com.java.component.kafka.producer;

import com.java.component.kafka.common.ConnectionPool;
import com.java.component.kafka.common.KafkaConstants;
import com.java.component.kafka.common.PoolBase;
import com.java.component.kafka.common.PoolConfig;
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
 * Time: 15:02
 */

public class KafkaConnectionPool extends PoolBase<Producer<String, String>> implements ConnectionPool<Producer<String, String>> {

    private static final long serialVersionUID = 7171466384560676149L;


    public KafkaConnectionPool(final PoolConfig poolConfig,
                               final String brokers) {
        this(poolConfig, brokers,
                KafkaConstants.DEFAULT_ACKS,
                KafkaConstants.COMPRESSION_TYPE_CONFIG,
                KafkaConstants.BATCH_SIZE_CONFIG);
    }

    public KafkaConnectionPool(final PoolConfig poolConfig,
                               final String brokers,
                               final String acks,
                               final String codec,
                               final String batch) {
        super(poolConfig, new KafkaConnectionFactory(brokers, acks, codec, batch));
    }

    public KafkaConnectionPool(final PoolConfig poolConfig, final Properties properties) {
        super(poolConfig, new KafkaConnectionFactory(properties));
    }

    /**
     * 获取连接
     *
     * @return
     */
    @Override
    public Producer<String, String> getConnection() {
        return super.getResource();
    }

    /**
     * 返回连接
     *
     * @param conn
     */
    @Override
    public void returnConnection(Producer<String, String> conn) {
        super.returnResource(conn);
    }

    /**
     * 废弃连接
     *
     * @param conn
     */
    @Override
    public void invalidateConnection(Producer<String, String> conn) {
        super.invalidateResource(conn);
    }
}
