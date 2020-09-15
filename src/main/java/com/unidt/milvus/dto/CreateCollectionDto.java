package com.unidt.milvus.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class CreateCollectionDto {

    @NotNull(message = "集合名不能为空")
    private String collectionName;

    @Max(value = 512, message = "向量最大维数不得超过512")
    private long dimension = 0;
    private long indexFileSize = 1024;

    private int metricType = -2;

}
