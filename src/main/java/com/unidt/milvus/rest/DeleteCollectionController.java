package com.unidt.milvus.rest;

import com.unidt.milvus.api.DeleteCollectionAPI;
import com.unidt.milvus.dto.DeleteCollectionDto;
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
public class DeleteCollectionController {

    @Autowired
    DeleteCollectionAPI deleteCollectionAPI;

    /**
     * 删除集合
     * @param dto
     * @return
     */
    @RequestMapping(value = "/milvus/deleteCollection", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public String deleteCollection(@RequestBody @Valid DeleteCollectionDto dto,
                                   BindingResult results) {
        if (results.hasErrors())
            return results.getFieldError().getDefaultMessage();
        return deleteCollectionAPI.deleteCollection(dto);
    }
}
