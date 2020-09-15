package com.unidt.milvus.config;

import io.milvus.client.IndexType;
import io.milvus.client.MetricType;

public class Constants {

    // milvus主机
    public static final String MILVUS_HOST = "10.200.187.41";

    // milvus端口
    public static final int MILVUS_PORT = 19530;

    // 一次最大插入向量数
    public static final long MAX_VECTOR_COUNT = 100000;

    // 度量种类
    public static final int L2 = 1;

    public static final int IP = 2;

    // 索引种类
    public static final int FLAT = 1;

    public static final int IVFLAT = 2;

    public static final int SQ8 = 3;

    public static final int NSG = 4;

    public static final int HNSW = 11;

    public static final int IVFPQ = 6;

    // 搜索参数key
    public static final String SEARCH_PROPERTY = "nprobe";

    // 搜索参数value
    public static final int SEARCH_VALUE = 20;

    // API OK 200
    public static final int API_CODE_OK = 200;
    //
    // 未授权
    public static final int API_CODE_FORBIDDEN = 400;

    // 保存错误
    public static final int API_TOKEN_DEADLINE = 401;

    // 未找到
    public static final int API_CODE_NOT_FOUND = 404;

    // 冲突
    public static final int API_CODE_CORRUPT = 409;

    // 已删除
    public static final int API_CODE_DELETED = 410;

    // 服务器内部错误
    public static final int API_CODE_INNER_ERROR = 500;
}
