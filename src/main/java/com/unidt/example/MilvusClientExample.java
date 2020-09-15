package com.unidt.example;

import com.google.gson.JsonObject;
import edu.emory.mathcs.backport.java.util.Arrays;
import io.milvus.client.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class MilvusClientExample {

    /**
     *
     * @param vectorCount
     * @param dimension
     * @return 随机的向量组
     */
    static List<List<Float>> generateVectors(long vectorCount, long dimension) {
        SplittableRandom splittableRandom = new SplittableRandom();
        List<List<Float>> vectors = new ArrayList<>(1000000);
        for (int i = 0; i < vectorCount; i++) {
            splittableRandom = splittableRandom.split();
            DoubleStream doubleStream = splittableRandom.doubles(dimension);
            List<Float> vector = doubleStream.boxed().map(Double::floatValue)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }

        return vectors;
    }

    static float[][] generateVectors1(long vectorCount, long dimension) {
        float[][] vectors = new float[(int) vectorCount][(int) dimension];
        for (int i = 0; i < vectorCount; i++) {
            for (int j = 0; j < dimension; j++) {
                float num = (float) Math.random();
                vectors[i][j] = num;
            }
        }
        return vectors;
    }

    /**
     *
     * @param vector
     * @return 点积正常化的一个向量
     */
    static List<Float> nomorlizeVector(List<Float> vector) {
        float squareSum = vector.stream().map(x -> x * x).reduce((float)0, Float::sum);
        final float norm = (float)Math.sqrt(squareSum);
        vector = vector.stream().map(x -> x / norm).collect(Collectors.toList());
        return vector;
    }

    public static void main(String[] args) throws InterruptedException, ConnectFailedException {
        final String host = "10.200.187.41";
        final int port = 19530;

        // 创建milvus客户
        MilvusClient client = new MilvusGrpcClient(Level.ALL);

        System.out.println("开始连接服务器");
        // 连接到服务器
        ConnectParam connectParam = new ConnectParam.Builder().withHost(host)
                .withPort(port).build();
        try {
            Response connectResponse = client.connect(connectParam);
        } catch (ConnectFailedException e) {
            System.out.println("Failed to connect to Milvus server: " + e.toString());
            throw e;
        }
        System.out.println("连接服务器成功");

        // 查询集合的索引类型
        // ShowCollectionInfoResponse showCollectionInfoResponse = client.showCollectionInfo("test2");
        // CollectionInfo collectionInfo = showCollectionInfoResponse.getCollectionInfo().get();
        // System.out.println(collectionInfo.getPartitionInfos().get(0).getSegmentInfos().get(0).getIndexName());
        // System.out.println(collectionInfo.getPartitionInfos().size());
        // System.out.println(collectionInfo.getPartitionInfos().get(0).getSegmentInfos().size());


        // 查看是否连接成功
        boolean connected = client.isConnected();

        // 创建一个集合
        System.out.println("开始创建集合");
        final String collectionName = "testNSG";
        final long dimension = 512;
        final long indexFileSize = 1024; // 索引文件的大小，单位MB
        final MetricType metricType = MetricType.L2; // 选用点积作为测量标准
        CollectionMapping collectionMapping = new CollectionMapping
                .Builder(collectionName, dimension)
                .withIndexFileSize(indexFileSize)
                .withMetricType(metricType)
                .build();
        Response createCollectionResponse = client.createCollection(collectionMapping);
        System.out.println("集合创建成功");

        // 查询集合是否存在
        HasCollectionResponse hasCollectionResponse = client.hasCollection(collectionName);

        // 描述集合
        DescribeCollectionResponse describeCollectionResponse =
                client.describeCollection(collectionName);

        // 插入随机向量
        System.out.println("开始插入向量");
        final int vectorCount = 10000;
        List<List<Float>> vectors = generateVectors(vectorCount,dimension);
        vectors = vectors.stream().map(MilvusClientExample::nomorlizeVector)
                .collect(Collectors.toList());
        System.out.println(vectors);
        InsertParam insertParam = new InsertParam.Builder(collectionName)
                .withFloatVectors(vectors).build();
        InsertResponse insertResponse = client.insert(insertParam);
        List<Long> vectorIds = insertResponse.getVectorIds();
        System.out.println("插入向量成功");

        // 刷新集合
        Response flushResponse = client.flush(collectionName);

        // 得到当前集合的向量数
        GetCollectionRowCountResponse getCollectionRowCountResponse =
                client.getCollectionRowCount(collectionName);

        // 为集合创建索引
        System.out.println("开始创建索引");
        final IndexType indexType = IndexType.IVF_SQ8; // 有不同索引和不同参数
        JsonObject indexParmsJson = new JsonObject();
        indexParmsJson.addProperty("nlist", 16384);
        Index index = new Index.Builder(collectionName, indexType)
                .withParamsInJson(indexParmsJson.toString()).build();
        Response createIndexResponse = client.createIndex(index);
        System.out.println("创建索引成功");

        // 描述集合的索引
        DescribeIndexResponse describeIndexResponse = client.describeIndex(collectionName);
        System.out.println(describeIndexResponse.getIndex().get().getIndexType().getVal());


        // 查询向量
        System.out.println("开始查询向量");
        final int searchBatchSize = 5;
        List<List<Float>> vectorsToSearch = vectors.subList(0, searchBatchSize);
        final int topK = 10;
        // 索引不同，查询的参数也不同
        JsonObject searchParmsJson = new JsonObject();
        searchParmsJson.addProperty("nprobe", 20);
        SearchParam searchParam = new SearchParam.Builder(collectionName)
                .withFloatVectors(vectorsToSearch)
                .withTopK(topK)
                .withParamsInJson(searchParmsJson.toString())
                .build();
        SearchResponse searchResponse = client.search(searchParam);
        if (searchResponse.ok()) {
            List<List<SearchResponse.QueryResult>> queryResultList =
                    searchResponse.getQueryResultsList();
            final double epsilon = 0.001;
            for (int i = 0; i < searchBatchSize; i++) {

                System.out.println("要比较的向量是： " + vectorsToSearch.get(i));
                List<SearchResponse.QueryResult> queryResult = queryResultList.get(i);
                System.out.println("数组长度是： " + queryResult.size());
                for (int j = 0; j < queryResult.size(); j++) {
                    System.out.println("第 " + i + " 组的第 " + j + " 个结果是： "
                            + queryResult.get(j));
                }

                SearchResponse.QueryResult firstQueryResult
                        = queryResultList.get(i).get(0);
                if (firstQueryResult.getVectorId() != vectorIds.get(i)
                    || Math.abs(1 - firstQueryResult.getDistance()) > epsilon) {
                    throw new AssertionError("Wrong results!");
                }
            }
            System.out.println("查询向量成功");
        }

        // 压缩集合
        // Compact the collection, erasing deleted data from disk and rebuild index in background (if
        // the data size after compaction is still larger than indexFileSize). Data was only
        // soft-deleted until you call compact.
        Response compactResponse = client.compact(collectionName);

        // Drop index for the collection
        Response dropIndexResponse = client.dropIndex(collectionName);

        // 断开连接
        try {
            Response disconnectResponse = client.disconnect();
        } catch (InterruptedException e) {
            System.out.println("Failed to connect to Milvus server: " + e.toString());
            throw e;
        }

    }
}
