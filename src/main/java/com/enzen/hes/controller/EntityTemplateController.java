package com.enzen.hes.controller;

import com.enzen.hes.handler.response.ApiResponseCode;
import com.enzen.hes.handler.response.ResponseDTO;
import com.enzen.hes.handler.response.ResponseUtil;
import com.enzen.hes.service.EntityTemplateService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/templates/entities")
public class EntityTemplateController {
    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private EntityTemplateService entityTemplateService;

    @PostMapping
    public ResponseDTO<?> createTemplate(@RequestBody Document templateRequest) throws Exception {
        entityTemplateService.createTemplate(templateRequest);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseDTO<?> listTemplate(@RequestParam(required = false) Map<String, Object> query) throws Exception {
        var result = entityTemplateService.listTemplate(query);
        return responseUtil.ok(result, ApiResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseDTO<?> deleteTemplate(@RequestParam(required = false) Map<String, Object> query) throws Exception {
        entityTemplateService.deleteTemplate(query);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO<?> deleteTemplate(@PathVariable String id) throws Exception {
        entityTemplateService.deleteTemplate(id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

}
