package com.enzen.hes.controller;

import com.enzen.hes.handler.response.ApiResponseCode;
import com.enzen.hes.handler.response.ResponseDTO;
import com.enzen.hes.handler.response.ResponseUtil;
import com.enzen.hes.service.ZoneService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.enzen.hes.handler.response.ResponseUtil.responseUtil;

@RestController
@RequestMapping(value = "/api/v1/zone")
public class ZoneController {

    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private ZoneService zoneService;

    @PostMapping
    public ResponseDTO<?> createZone(@RequestBody Document zoneRequest) throws Exception {
        zoneService.createZone(zoneRequest);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseDTO<?> getAllZones() throws Exception {
        var result = zoneService.getAllZones();
        return responseUtil.ok(result, ApiResponseCode.SUCCESS);
    }

    @DeleteMapping("{id}")
    public ResponseDTO<?> deleteZone(@PathVariable String id) throws Exception {
        zoneService.deleteZone(id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @PutMapping("{id}")
    public ResponseDTO<?> updateZone(@RequestBody Document zoneRequest, @PathVariable String id) throws Exception {
        zoneService.updateZone(zoneRequest, id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }
}
