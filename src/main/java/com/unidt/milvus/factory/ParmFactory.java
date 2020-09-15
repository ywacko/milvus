package com.unidt.milvus.factory;

import com.google.gson.JsonObject;
import com.unidt.milvus.config.Constants;
import com.unidt.milvus.dto.CreateCollectionDto;
import com.unidt.milvus.dto.CreateIndexDto;
import com.unidt.milvus.dto.InsertVectorDto;
import com.unidt.milvus.dto.SearchVectorDto;
import io.milvus.client.*;

public class ParmFactory {


    /**
     * 创建索引的参数
     * @param dto
     * @return
     */
    public static Index getIndex(CreateIndexDto dto) {
        JsonObject indexParm = new JsonObject();
        switch (dto.getIndexType()) {
            /**
             * HNSW类型索引
             * val: 11
             * M range:[5, 48]
             * efConstruction range:[100, 500]
             */
            case Constants.HNSW: {
                indexParm.addProperty("M", dto.getBigM());
                indexParm.addProperty("efConstruction", dto.getEfConstruction());
                break;
            }
            /**
             * IVFPQ类型索引
             * val: 6
             * nlist range:[1, 999999]
             * m is decided by dim and have a couple of results.
             */
            case Constants.IVFPQ: {
                indexParm.addProperty("nlist", dto.getNlist());
                indexParm.addProperty("m", dto.getSmallM());
                break;
            }
            /**
             * NSG类型索引，milvus的算法
             * val: 4
             * search_length range:[10, 300]
             * out_degree range:[5, 300]
             * candidate_pool_size range:[50, 1000]
             * knng range:[5, 300]
             */
            case Constants.NSG: {
                indexParm.addProperty("search_length", dto.getSearchLength());
                indexParm.addProperty("out_degree", dto.getOutDegree());
                indexParm.addProperty("candidate_pool_size", dto.getCandidatePoolSize());
                indexParm.addProperty("knng", dto.getKNNG());
                break;
            }
            /**
             * FLAT(1)/IVFLAT(2)/SQ8(3)
             * nlist range:[1, 999999]
             */
            case Constants.FLAT: case Constants.IVFLAT: case Constants.SQ8: {
                indexParm.addProperty("nlist", dto.getNlist());
                break;
            }
            /**
             * 默认为SQ8
             */
            default: {
                indexParm.addProperty("nlist", dto.getNlist());
                dto.setIndexType(Constants.SQ8);
            }
        }
        Index index = new Index.Builder(dto.getCollectionName(), IndexType.valueOf(dto.getIndexType()))
                .withParamsInJson(indexParm.toString()).build();
        return index;
    }

    /**
     * 搜索的参数
     * @param dto
     * @return
     */
    public static SearchParam getSearchParm(SearchVectorDto dto) {
        JsonObject searchParm = new JsonObject();
        switch (dto.getVectorsIndexType()) {
            /**
             * HNSW索引类型
             * ef range:[topk, 4096]
             */
            case Constants.HNSW: {
                searchParm.addProperty("ef", dto.getEf());
                break;
            }
            /**
             * NSG索引类型
             * search_length range:[10, 300]
             */
            case Constants.NSG: {
                searchParm.addProperty("search_length", dto.getSearchLength());
                break;
            }
            /**
             * FLAT/IVFLAT/SQ8/IVFPQ 索引类型
             * nprobe range:[1,999999]
             */
            case Constants.FLAT: case Constants.IVFLAT: case Constants.SQ8: case Constants.IVFPQ: {
                searchParm.addProperty("nprobe", dto.getNProbe());
                break;
            }
            default:
        }
        SearchParam searchParam = new SearchParam.Builder(dto.getCollectionName())
                .withFloatVectors(dto.getVectorsToSearch())
                .withTopK(dto.getTopK())
                .withParamsInJson(searchParm.toString())
                .build();
        return searchParam;
    }

    /**
     * 创建集合的参数
     * @param dto
     * @return
     */
    public static CollectionMapping getCollectionMapping(CreateCollectionDto dto) {
        int metricTypeNum;
        switch (dto.getMetricType()) {
            /**
             * 度量规则为IP
             */
            case Constants.IP: {
                metricTypeNum = 2;
                break;
            }
            /**
             * 默认度量规则为L2
             */
            default: {
                metricTypeNum = Constants.L2;
                dto.setMetricType(1);
            }
        }
        MetricType metricType = MetricType.valueOf(metricTypeNum);
        CollectionMapping collectionMapping = new CollectionMapping
                .Builder(dto.getCollectionName(), dto.getDimension())
                .withIndexFileSize(dto.getIndexFileSize())
                .withMetricType(metricType)
                .build();
        dto.setMetricType(metricTypeNum);
        return collectionMapping;
    }

    /**
     * 插入向量的参数
     * @param dto
     * @return
     */
    public static InsertParam getInsertParam(InsertVectorDto dto) {
        return new InsertParam.Builder(dto.getCollectionName())
                .withFloatVectors(dto.getVectors()).build();
    }

}
