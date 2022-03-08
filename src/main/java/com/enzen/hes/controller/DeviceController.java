package com.enzen.hes.controller;

import com.enzen.hes.handler.response.ApiResponseCode;
import com.enzen.hes.handler.response.ResponseDTO;
import com.enzen.hes.handler.response.ResponseUtil;
import com.enzen.hes.service.DeviceService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/devices")
public class DeviceController {
    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/template")
    public ResponseDTO<?> addDeviceAttributes(@RequestBody Document deviceTemplateRequest) throws Exception {
        deviceService.addDeviceAttributes(deviceTemplateRequest);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @GetMapping("/template")
    public ResponseDTO<?> getDeviceAttributes() throws Exception {
        var result= deviceService.getDeviceAttributes();
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @PostMapping
    public ResponseDTO<?> createDevice(@RequestBody Document deviceRequest) throws Exception {
        deviceService.createDevice(deviceRequest);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }


    @PostMapping(value = "/bulk", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDTO<?> createDeviceBulk(@RequestPart("formData") Document formData, @RequestPart("file") MultipartFile file) throws Exception {
        deviceService.createDeviceBulk(formData, file);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseDTO<?> getAllDevices() throws Exception {
        var result= deviceService.getAllDevices();
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @DeleteMapping("{id}")
    public ResponseDTO<?> deleteDevice(@RequestBody String id) throws Exception {
        deviceService.deleteDevice(id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @PutMapping("{id}")
    public ResponseDTO<?> updateDevice(@RequestBody Document deviceInfoRequest,@PathVariable String id) throws Exception {
         deviceService.updateDevice(deviceInfoRequest,id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }
}

