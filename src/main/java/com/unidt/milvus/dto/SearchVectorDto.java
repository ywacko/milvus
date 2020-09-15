package com.unidt.milvus.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SearchVectorDto {

    @NotNull (message = "集合名不能为空")
    private String collectionName;
    private int topK = 1;
    @NotNull (message = "被查找向量不能为空")
    private List<List<Float>> vectorsToSearch;
    private int vectorsIndexType;

    // 下面的是搜索参数，初始有默认值

    /**
     * HNSW
     * ef range:[topk, 4096]
     */
    private int ef = 200;

    /**
     * NSG
     * search_length range:[10, 300]
     */
    private int searchLength = 100;

    /**
     * FLAT/IVFLAT/SQ8/IVFPQ
     * nprobe range:[1,999999]
     */
    private int nProbe = 32;

}
