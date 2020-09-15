package com.unidt.milvus.api;

import com.unidt.milvus.dto.TestDto;

public interface TestAPI {

    String insertRandomVectors(TestDto dto);
}
