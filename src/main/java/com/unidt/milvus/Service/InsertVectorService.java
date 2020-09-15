package com.unidt.milvus.Service;

import com.unidt.milvus.api.InsertVectorAPI;
import com.unidt.milvus.config.Constants;
import com.unidt.milvus.config.ReturnResult;
import com.unidt.milvus.dto.InsertVectorDto;
import com.unidt.milvus.factory.ClientFactory;
import com.unidt.milvus.factory.ParmFactory;
import io.milvus.client.*;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 插入向量的业务逻辑类
 */
@Service
public class InsertVectorService implements InsertVectorAPI {

    private static final Logger LOG
            = LoggerFactory.getLogger(com.unidt.milvus.Service.InsertVectorService.class);

    @Override
    public String insertVector(InsertVectorDto dto) {
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

        // 开始插入向量
        InsertParam insertParam = ParmFactory.getInsertParam(dto);
        InsertResponse insertResponse = clientFactory.getClient().insert(insertParam);

        // 刷新集合并查看当前集合的向量总数
        Response flushResponse = clientFactory.getClient().flush(dto.getCollectionName());
        GetCollectionRowCountResponse getCollectionRowCountResponse =
                clientFactory.getClient().getCollectionRowCount(dto.getCollectionName());
        long rowCount = getCollectionRowCountResponse.getCollectionRowCount();

        // 计时结束
        long endTime =  System.currentTimeMillis();
        float usedTime = endTime - startTime;

        // 断开服务器连接
        clientFactory.disconnectClient();

        // 输出插入结果
        if (!insertResponse.ok()) {
            LOG.info("插入向量失败");
            return ReturnResult.createResult(Constants.API_CODE_FORBIDDEN, "插入向量失败").toJson();
        }

        Document result = ReturnResult.createResult();
        return result.append("插入向量数", dto.getVectors().size())
                .append("当前向量数", String.valueOf(rowCount))
                .append("本次插入向量耗时", usedTime)
                .toJson();
    }
}
