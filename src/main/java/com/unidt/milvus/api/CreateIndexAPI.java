package com.unidt.milvus.api;

import com.unidt.milvus.dto.CreateIndexDto;

public interface CreateIndexAPI {

    /**
     * 建立索引
     * @param dto
     * @return
     */
    String createIndex(CreateIndexDto dto);
}
