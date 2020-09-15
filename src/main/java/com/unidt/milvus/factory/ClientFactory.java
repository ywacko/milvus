package com.unidt.milvus.factory;

import com.unidt.milvus.config.Constants;
import io.milvus.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;

/**
 * 返回client的对象类
 */
public class ClientFactory {

    private static final Logger LOG
            = LoggerFactory.getLogger(com.unidt.milvus.factory.ClientFactory.class);
    private int port;
    private String host;
    private MilvusClient client;

    public ClientFactory() {
        port = Constants.MILVUS_PORT;
        host = Constants.MILVUS_HOST;
        client = new MilvusGrpcClient(Level.ALL);
    }


    public ClientFactory(String host, int port) {
        this.port = port;
        this.host = host;
        client = new MilvusGrpcClient(Level.ALL);
    }

    public void connectClient() {
        ConnectParam connectParam = new ConnectParam.Builder().withHost(host)
                .withPort(port).build();
        try {
            Response connectResponse = client.connect(connectParam);
        } catch (ConnectFailedException e) {
            LOG.info("连接 Milvus 服务器失败: " + e.getMessage());
        }
    }

    public void disconnectClient() {
        try {
            Response disconnectResponse = client.disconnect();
        } catch (InterruptedException e) {
            System.out.println("断开 Milvus 服务器失败" + e.getMessage());
        }
    }

    public MilvusClient getClient() {
        return client;
    }
}
