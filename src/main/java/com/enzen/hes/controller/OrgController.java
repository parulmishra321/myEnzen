package com.enzen.hes.controller;

import com.enzen.hes.handler.response.ApiResponseCode;
import com.enzen.hes.handler.response.ResponseDTO;
import com.enzen.hes.handler.response.ResponseUtil;
import com.enzen.hes.service.OrgService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/orgs")
public class OrgController {
    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private OrgService orgService;

    @PostMapping("/template")
    public ResponseDTO<?> addOrgAttributes(@RequestBody Document orgTemplateRequest) throws Exception {
        orgService.addOrgAttributes(orgTemplateRequest);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @GetMapping("/template")
    public ResponseDTO<?> getOrgAttributes() throws Exception {
        var result= orgService.getOrgAttributes();
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @PostMapping
    public ResponseDTO<?> createOrg(@RequestBody Document orgRequest) throws Exception {
        orgService.createOrg(orgRequest);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @PostMapping(value = "/bulk", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDTO<?> createOrgBulk(@RequestPart("formData") Document formData,@RequestPart("file") MultipartFile file) throws Exception {
        orgService.createOrgBulk(formData,file);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseDTO<?> getAllDevices() throws Exception {
        var result= orgService.getAllOrg();
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @DeleteMapping
    public ResponseDTO<?> deleteDevice(@RequestBody String id) throws Exception {
        orgService.deleteOrg(id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @PutMapping
    public ResponseDTO<?> updateDevice(@RequestBody Document orgRequest,@RequestParam String id) throws Exception {
        orgService.updateOrg(orgRequest,id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }
}