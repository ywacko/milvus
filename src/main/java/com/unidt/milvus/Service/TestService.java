package com.unidt.milvus.Service;

import com.unidt.milvus.api.TestAPI;
import com.unidt.milvus.config.Constants;
import com.unidt.milvus.config.ReturnResult;
import com.unidt.milvus.dto.TestDto;
import com.unidt.milvus.factory.ClientFactory;
import io.milvus.client.GetCollectionRowCountResponse;
import io.milvus.client.InsertParam;
import io.milvus.client.InsertResponse;
import io.milvus.client.Response;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
public class TestService implements TestAPI {

    private static final Logger LOG
            = LoggerFactory.getLogger(com.unidt.milvus.Service.TestService.class);

    @Override
    public String insertRandomVectors(TestDto dto) {
        if (dto == null) {
            LOG.info("参数错误");
            return ReturnResult.createResult(Constants.API_CODE_FORBIDDEN, "参数错误").toJson();
        }

        // 连接milvus服务器
        ClientFactory clientFactory = new ClientFactory();
        clientFactory.connectClient();

        // 计时开始
        long startTime =  System.currentTimeMillis();

        long vectorNum = dto.getVectorCount();
        do {
            long insertNum = vectorNum > Constants.MAX_VECTOR_COUNT
                    ? Constants.MAX_VECTOR_COUNT : vectorNum;
            vectorNum -= Constants.MAX_VECTOR_COUNT;
            List<List<Float>> vectors = generateVectors(insertNum, dto.getDimension());
            InsertParam insertParam = new InsertParam.Builder(dto.getCollectionName())
                    .withFloatVectors(vectors).build();
            InsertResponse insertResponse = clientFactory.getClient().insert(insertParam);
            if (!insertResponse.ok()) {
                LOG.info("插入向量失败");
                return ReturnResult.createResult(Constants.API_CODE_FORBIDDEN, "插入向量失败").toJson();
            }
            List<List<Float>> subVector = vectors.subList(0, 10);
            for (List<Float> rows : subVector) {
                System.out.println(rows);
            }
            // 刷新集合并查看当前集合的向量总数
            Response flushResponse = clientFactory.getClient().flush(dto.getCollectionName());
        }
        while (vectorNum > 0);

        // 断开与服务器的连接并输出结果
        GetCollectionRowCountResponse getCollectionRowCountResponse =
                clientFactory.getClient().getCollectionRowCount(dto.getCollectionName());
        long rowCount = getCollectionRowCountResponse.getCollectionRowCount();
        clientFactory.disconnectClient();

        // 计时结束
        long endTime =  System.currentTimeMillis();
        float usedTime = endTime - startTime;

        Document result = ReturnResult.createResult();
        return result.append("当前向量总数", String.valueOf(rowCount))
                .append("本次插入向量耗时", usedTime)
                .toJson();
    }

    static List<List<Float>> generateVectors(long vectorCount, long dimension) {
        SplittableRandom splittableRandom = new SplittableRandom();
        List<List<Float>> vectors = new ArrayList<>();
        for (int i = 0; i < vectorCount; i++) {
            splittableRandom = splittableRandom.split();
            DoubleStream doubleStream = splittableRandom.doubles(dimension);
            List<Float> vector = doubleStream.boxed().map(Double::floatValue)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        return vectors;
    }

}
