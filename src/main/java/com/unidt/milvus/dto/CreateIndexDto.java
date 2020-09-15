package com.unidt.milvus.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateIndexDto {

    @NotNull(message = "集合名不能为空")
    private String collectionName;
    private int indexType = 1;

    // 下面是索引参数，有初始值

    /**
     * HNSW
     * M range:[5, 48]
     * efConstruction range:[100, 500]
     */
    private int bigM = 48;
    private int efConstruction = 200;

    /**
     * IVFPQ
     * nlist range:[1, 999999]
     * m is decided by dim and have a couple of results.
     *
     *
     * FLAT(1)/IVFLAT(2)/SQ8(3)
     * nlist range:[1, 999999]
     */

    private int nlist = 16384;
    private int smallM = 12;



    /**
     * NSG
     * search_length range:[10, 300]
     * out_degree range:[5, 300]
     * candidate_pool_size range:[50, 1000]
     * knng range:[5, 300]
     */
    private int searchLength = 45;
    private int outDegree = 50;
    private int candidatePoolSize = 300;
    private int kNNG = 100;

}
