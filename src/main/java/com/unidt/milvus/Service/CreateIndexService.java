package com.unidt.milvus.Service;

import com.unidt.milvus.api.CreateIndexAPI;
import com.unidt.milvus.config.Constants;
import com.unidt.milvus.config.ReturnResult;
import com.unidt.milvus.dto.CreateIndexDto;
import com.unidt.milvus.factory.ClientFactory;
import com.unidt.milvus.factory.ParmFactory;
import io.milvus.client.HasCollectionResponse;
import io.milvus.client.Index;
import io.milvus.client.IndexType;
import io.milvus.client.Response;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 添加索引的业务逻辑类
 */
@Service
public class CreateIndexService implements CreateIndexAPI {

    private static final Logger LOG
            = LoggerFactory.getLogger(com.unidt.milvus.Service.CreateIndexService.class);

    @Override
    public String createIndex(CreateIndexDto dto) {
        if (dto == null) {
            LOG.info("参数错误");
            return ReturnResult.createResult(Constants.API_CODE_FORBIDDEN, "参数错误").toJson();
        }

        // 连接milvus服务器
        ClientFactory clientFactory = new ClientFactory();
        clientFactory.connectClient();

        // 查询集合是否存在
        HasCollectionResponse hasCollectionResponse
                = clientFactory.getClient().hasCollection(dto.getCollectionName());
        if (!hasCollectionResponse.hasCollection()) {
            LOG.info("集合不存在");
            return ReturnResult.createResult(Constants.API_CODE_FORBIDDEN, "集合不存在").toJson();
        }

        // 计时开始
        long startTime =  System.currentTimeMillis();

        // 开始创建索引
        Index index = ParmFactory.getIndex(dto);
        Response createIndexResponse = clientFactory.getClient().createIndex(index);

        // 计时结束
        long endTime =  System.currentTimeMillis();
        float usedTime = endTime - startTime;

        // 断开与服务器的连接
        clientFactory.disconnectClient();

        // 返回结果
        if (!createIndexResponse.ok()) {
            LOG.info("创建索引失败");
            return ReturnResult.createResult(Constants.API_CODE_FORBIDDEN, "创建索引失败").toJson();
        }

        LOG.info("总耗时： " + usedTime);
        Document result = ReturnResult.createResult();
        return result.append("索引类型是: ", IndexType.valueOf(dto.getIndexType()).toString())
                .append("本次创建索引耗时: ", usedTime)
                .toJson();
    }
}
