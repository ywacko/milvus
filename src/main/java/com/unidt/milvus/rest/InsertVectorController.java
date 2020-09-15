package com.unidt.milvus.rest;

import com.unidt.milvus.api.InsertVectorAPI;
import com.unidt.milvus.dto.InsertVectorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 插入向量的API类
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
public class InsertVectorController {

    @Autowired
    InsertVectorAPI insertVectorAPI;

    /**
     * 插入向量
     * @param dto
     * @return
     */
    @RequestMapping(value = "/milvus/insertVector", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public String insertVector(@RequestBody @Valid InsertVectorDto dto, BindingResult results) {
        if (results.hasErrors())
            return results.getFieldError().getDefaultMessage();
        return insertVectorAPI.insertVector(dto);
    }
}
