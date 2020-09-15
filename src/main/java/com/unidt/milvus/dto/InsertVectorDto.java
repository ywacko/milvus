package com.unidt.milvus.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class InsertVectorDto {

    @NotNull (message = "集合名不能为空")
    private String collectionName;
    @NotNull (message = "插入向量组不得为空")
    private List<List<Float>> vectors;

}
