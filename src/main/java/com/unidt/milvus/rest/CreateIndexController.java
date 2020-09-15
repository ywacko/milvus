package com.unidt.milvus.rest;

import com.unidt.milvus.api.CreateIndexAPI;
import com.unidt.milvus.dto.CreateIndexDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 创建索引的API类
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
public class CreateIndexController {

    @Autowired
    CreateIndexAPI createIndexAPI;

    /**
     * 创建索引
     * @param dto
     * @return
     */
    @RequestMapping(value = "/milvus/createIndex", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public String insertVector(@RequestBody @Valid CreateIndexDto dto, BindingResult results) {
        if (results.hasErrors())
            return results.getFieldError().getDefaultMessage();
        return createIndexAPI.createIndex(dto);
    }
}
