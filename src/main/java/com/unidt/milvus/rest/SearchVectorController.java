package com.unidt.milvus.rest;

import com.unidt.milvus.api.SearchVectorAPI;
import com.unidt.milvus.dto.SearchVectorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 搜索向量的API
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
public class SearchVectorController {

    @Autowired
    SearchVectorAPI searchVectorAPI;

    /**
     * 查询向量
     * @param dto
     * @return
     */
    @RequestMapping(value = "/milvus/searchVector", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public String searchVector(@RequestBody @Valid SearchVectorDto dto, BindingResult results) {
        if (results.hasErrors())
            return results.getFieldError().getDefaultMessage();
        return searchVectorAPI.searchVector(dto);
    }
}
