package com.unidt.milvus.api;

import com.unidt.milvus.dto.InsertVectorDto;

public interface InsertVectorAPI {

    /**
     * 插入向量
     * @param dto
     * @return
     */
    String insertVector(InsertVectorDto dto);
}
