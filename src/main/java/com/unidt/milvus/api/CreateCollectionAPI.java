package com.unidt.milvus.api;

import com.unidt.milvus.dto.CreateCollectionDto;

public interface CreateCollectionAPI {

    /**
     * 创建集合
     * @param dto
     * @return
     */
    String createCollection(CreateCollectionDto dto);
}
