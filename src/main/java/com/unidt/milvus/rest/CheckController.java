package com.unidt.milvus.rest;

import com.unidt.milvus.api.CheckAPI;
import com.unidt.milvus.dto.CheckDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 查询集合信息
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
public class CheckController {

    @Autowired
    CheckAPI checkAPI;

    /**
     * 查询集合信息
     * @param dto
     * @param results
     * @return
     */
    @RequestMapping(value = "/milvus/checkCollection", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public String checkController(@RequestBody @Valid CheckDto dto, BindingResult results) {
        if (results.hasErrors())
            return results.getFieldError().getDefaultMessage();
        return checkAPI.checkCollection(dto);
    }
}
