package com.unidt.milvus.Service;

import com.unidt.milvus.api.CreateCollectionAPI;
import com.unidt.milvus.config.Constants;
import com.unidt.milvus.config.ReturnResult;
import com.unidt.milvus.dto.CreateCollectionDto;
import com.unidt.milvus.factory.ClientFactory;
import com.unidt.milvus.factory.ParmFactory;
import io.milvus.client.CollectionMapping;
import io.milvus.client.MetricType;
import io.milvus.client.Response;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 创造集合的业务逻辑类
 */
@Service
public class CreateCollectionService implements CreateCollectionAPI {

    private static final Logger LOG
            = LoggerFactory.getLogger(com.unidt.milvus.Service.CreateCollectionService.class);

    @Override
    public String createCollection(CreateCollectionDto dto) {
        if (dto == null) {
            LOG.info("参数错误");
            return ReturnResult.createResult(Constants.API_CODE_FORBIDDEN, "参数错误").toJson();
        }

        // 连接milvus服务器
        ClientFactory clientFactory = new ClientFactory();
        clientFactory.connectClient();

        // 开始创建集合
        CollectionMapping collectionMapping = ParmFactory.getCollectionMapping(dto);
        Response createCollectionResponse = clientFactory.getClient().createCollection(collectionMapping);

        // 断开与服务器的连接
        clientFactory.disconnectClient();

        // 判断集合是否存在，存在返回200否则返回400
        if (!createCollectionResponse.ok()) {
            LOG.info("创建集合失败");
            return ReturnResult.createResult(Constants.API_CODE_FORBIDDEN, "创建集合失败").toJson();
        }
        Document result = ReturnResult.createResult();
        return result.append("创建的集合名为: ", dto.getCollectionName())
                .append("集合度量类型为: ", MetricType.valueOf(dto.getMetricType()).toString())
                .toJson();
    }
}
