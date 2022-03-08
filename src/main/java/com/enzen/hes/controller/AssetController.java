package com.enzen.hes.controller;

import com.enzen.hes.handler.response.ApiResponseCode;
import com.enzen.hes.handler.response.ResponseDTO;
import com.enzen.hes.handler.response.ResponseUtil;
import com.enzen.hes.service.AssetService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/assets")
public class AssetController {
    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private AssetService assetService;

    @PostMapping("/template")
    public ResponseDTO<?> addAssetAttributes(@RequestBody Document assetTemplateRequest) throws Exception {
        assetService.addAssetAttributes(assetTemplateRequest);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @GetMapping("/template")
    public ResponseDTO<?> getAssetAttributes() throws Exception {
        var result = assetService.getAssetAttributes();
        return responseUtil.ok(result, ApiResponseCode.SUCCESS);
    }

    @PostMapping
    public ResponseDTO<?> createAsset(@RequestBody Document assetRequest) throws Exception {
        assetService.createAsset(assetRequest);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @PostMapping(value = "/bulk", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDTO<?> createAssetBulk(@RequestPart("formData") Document formData, @RequestPart("file") MultipartFile file) throws Exception {
        assetService.createAssetBulk(formData, file);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseDTO<?> getAllAssets() throws Exception {
        var result = assetService.getAllAssets();
        return responseUtil.ok(result, ApiResponseCode.SUCCESS);
    }

    @DeleteMapping("{id}")
    public ResponseDTO<?> deleteAsset(@PathVariable String id) throws Exception {
        assetService.deleteAsset(id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @PutMapping("{id}")
    public ResponseDTO<?> updateAsset(@RequestBody Document assetRequest, @PathVariable String id) throws Exception {
        assetService.updateAsset(assetRequest, id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }
}
