package com.unidt.milvus.Service;

import com.unidt.milvus.api.DeleteCollectionAPI;
import com.unidt.milvus.config.Constants;
import com.unidt.milvus.config.ReturnResult;
import com.unidt.milvus.dto.DeleteCollectionDto;
import com.unidt.milvus.factory.ClientFactory;
import io.milvus.client.HasCollectionResponse;
import io.milvus.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 删除集合的业务逻辑类
 */
@Service
public class DeleteCollectionService implements DeleteCollectionAPI {

    private static final Logger LOG
            = LoggerFactory.getLogger(com.unidt.milvus.Service.DeleteCollectionService.class);

    @Override
    public String deleteCollection(DeleteCollectionDto dto) {
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

        // 开始删除集合
        Response dropCollectionResponse = clientFactory.getClient().dropCollection(dto.getCollectionName());

        // 断开与服务器的连接
        clientFactory.disconnectClient();

        // 返回结果
        if (!dropCollectionResponse.ok()) {
            LOG.info("删除集合失败");
            return ReturnResult.createResult(Constants.API_CODE_FORBIDDEN, "删除集合失败").toJson();
        }
        return ReturnResult.createResult().toJson();
    }
}
