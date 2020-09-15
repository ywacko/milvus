package com.unidt.milvus.rest;

import com.unidt.milvus.api.CreateCollectionAPI;
import com.unidt.milvus.dto.CreateCollectionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 创造集合的API类
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
public class CreateCollectionController {

    @Autowired
    CreateCollectionAPI createCollectionAPI;

    /**
     * 创建集合
     * @param dto
     * @return
     */
    @RequestMapping(value = "/milvus/createCollection", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public String createCollection(@RequestBody @Valid CreateCollectionDto dto,
                                   BindingResult results) {
        if (results.hasErrors())
            return results.getFieldError().getDefaultMessage();
        return createCollectionAPI.createCollection(dto);
    }

}
