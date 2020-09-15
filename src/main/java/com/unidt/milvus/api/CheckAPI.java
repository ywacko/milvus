package com.unidt.milvus.api;

import com.unidt.milvus.dto.CheckDto;

public interface CheckAPI {

    /**
     * 返回集合的信息
     * @param dto
     * @return
     */
    String checkCollection(CheckDto dto);
}
