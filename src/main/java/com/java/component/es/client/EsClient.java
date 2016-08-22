package com.java.component.es.client;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

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
 * Module Desc:com.java.component.es.client
 * User: zjprevenge
 * Date: 2016/8/22
 * Time: 18:51
 */

public class EsClient implements InitializingBean, DisposableBean, FactoryBean<TransportClient> {

    private static final Logger log = LoggerFactory.getLogger(EsClient.class);

    private String ips;

    private TransportClient transportClient;

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    @Override
    public void destroy() throws Exception {
        if (transportClient != null) {
            transportClient.close();
        }
    }

    @Override
    public TransportClient getObject() throws Exception {
        return transportClient;
    }

    @Override
    public Class<?> getObjectType() {
        return transportClient.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkArgument(StringUtils.isNotBlank(ips), "ips must not be empty...");
        log.info("init transport from config:{}", ips);
        Settings settings = ImmutableSettings.builder()
                .put("client.transport.ping_timeout", 10)
                .put("client.transport.sniff", "true")
                .put("client.transport.ignore_cluster_name", "true")
                .build();
        transportClient = new TransportClient(settings);
        String[] split = ips.split(";");
        for (String ipPort : split) {
            String[] sp = ipPort.split(":");
            String ip = sp[0];
            Integer port = Integer.decode(sp[1]);
            transportClient.addTransportAddress(new InetSocketTransportAddress(ip, port));
        }

    }
}
