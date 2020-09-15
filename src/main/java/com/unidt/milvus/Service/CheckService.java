package com.unidt.milvus.Service;

import com.unidt.milvus.api.CheckAPI;
import com.unidt.milvus.config.Constants;
import com.unidt.milvus.config.ReturnResult;
import com.unidt.milvus.dto.CheckDto;
import com.unidt.milvus.factory.ClientFactory;
import io.milvus.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 返回集合基本信息的服务类
 */
@Service
public class CheckService implements CheckAPI {

    private static final Logger LOG
            = LoggerFactory.getLogger(com.unidt.milvus.Service.CheckService.class);

    @Override
    public String checkCollection(CheckDto dto) {
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

        // 刷新集合
        Response flushResponse = clientFactory.getClient().flush(dto.getCollectionName());

        GetCollectionRowCountResponse getCollectionRowCountResponse =
                clientFactory.getClient().getCollectionRowCount(dto.getCollectionName());
        long rowCount = getCollectionRowCountResponse.getCollectionRowCount();

        DescribeIndexResponse describeIndexResponse =
                clientFactory.getClient().describeIndex(dto.getCollectionName());
        String indexParams = describeIndexResponse.getIndex().get().getParamsInJson();
        String indexType = describeIndexResponse.getIndex().get().getIndexType().toString();

        return ReturnResult.createResult().append("集合名", dto.getCollectionName())
                .append("当前向量数", String.valueOf(rowCount))
                .append("索引类型", indexType)
                .append("索引参数", indexParams)
                .toJson();
    }
}
