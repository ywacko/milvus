package com.unidt.milvus.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CheckDto {

    @NotNull(message = "集合名不能为空")
    private String collectionName;

}
