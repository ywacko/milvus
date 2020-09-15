package com.unidt.milvus.api;

import com.unidt.milvus.dto.SearchVectorDto;

public interface SearchVectorAPI {

    /**
     * 查找向量
     * @param dto
     * @return
     */
    String searchVector(SearchVectorDto dto);
}
