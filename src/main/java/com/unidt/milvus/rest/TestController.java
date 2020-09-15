package com.unidt.milvus.rest;

import com.unidt.milvus.api.TestAPI;
import com.unidt.milvus.dto.TestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@EnableAutoConfiguration
@ComponentScan
public class TestController {

    @Autowired
    TestAPI testAPI;

    @RequestMapping(value = "/milvus/insertRandomVectors", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public String insertRandomVectors(@RequestBody @Valid TestDto dto, BindingResult results) {
        if (results.hasErrors())
            return results.getFieldError().getDefaultMessage();
        return testAPI.insertRandomVectors(dto);
    }
}
