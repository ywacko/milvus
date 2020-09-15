package com.unidt.milvus.Service;

import com.unidt.milvus.api.SearchVectorAPI;
import com.unidt.milvus.config.Constants;
import com.unidt.milvus.config.ReturnResult;
import com.unidt.milvus.dto.SearchVectorDto;
import com.unidt.milvus.factory.ClientFactory;
import com.unidt.milvus.factory.ParmFactory;
import io.milvus.client.*;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询向量的业务逻辑类
 */
@Service
public class SearchVectorService implements SearchVectorAPI {

    private static final Logger LOG
            = LoggerFactory.getLogger(com.unidt.milvus.Service.SearchVectorService.class);

    @Override
    public String searchVector(SearchVectorDto dto) {
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

        // 获得集合的索引类型
        DescribeIndexResponse describeIndexResponse
                = clientFactory.getClient().describeIndex(dto.getCollectionName());
        dto.setVectorsIndexType(describeIndexResponse.getIndex().get().getIndexType().getVal());

        // 开始查询
        SearchParam searchParam = ParmFactory.getSearchParm(dto);
        SearchResponse searchResponse = clientFactory.getClient().search(searchParam);

        // 输出查询结果
        if (!searchResponse.ok()) {
            LOG.info("查询向量失败");
            // 断开与服务器的连接
            clientFactory.disconnectClient();
            return ReturnResult.createResult(Constants.API_CODE_FORBIDDEN, "查询向量失败").toJson();
        }
        // 得到结果的ID
        List<List<Long>> resultsIds = searchResponse.getResultIdsList();
        // 得到结果的距离
        List<List<Float>> resultDistances = searchResponse.getResultDistancesList();

        // 将输出的结果，根据ID生成
        List<List<List<Float>>> searchResultLists = new ArrayList<>();
        for (List<Long> rowIds : resultsIds) {
            List<List<Float>> listToAdd = new ArrayList<>();
            for (Long eachId : rowIds) {
                // eachId代表每个结果对应的ID
                GetVectorByIdResponse getVectorByIdResponse
                        = clientFactory.getClient().getVectorById(dto.getCollectionName(), eachId);
                listToAdd.add(getVectorByIdResponse.getFloatVector());
            }
            // 将输出的向量组添加到结果list中
            searchResultLists.add(listToAdd);
        }

        // 计时结束
        long endTime =  System.currentTimeMillis();
        float usedTime = endTime - startTime;

        // 断开与服务器的连接
        clientFactory.disconnectClient();

        Document result = ReturnResult.createResult();
        return result
                // 添加查询出来的向量
                .append("searchResultLists", searchResultLists)
                .append("searchParam", searchParam.getParamsInJson())
                .append("resultDistance", resultDistances)
                .append("resultsByIds", resultsIds)
                .append("本次查询向量耗时", usedTime)
                .toJson();
    }

}
