package com.unidt.milvus.api;

import com.unidt.milvus.dto.DeleteCollectionDto;

public interface DeleteCollectionAPI {

    /**
     * 删除集合
     * @param dto
     * @return
     */
    String deleteCollection(DeleteCollectionDto dto);
}
