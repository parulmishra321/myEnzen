package com.enzen.hes.controller;

import com.enzen.hes.handler.response.ApiResponseCode;
import com.enzen.hes.handler.response.ResponseDTO;
import com.enzen.hes.handler.response.ResponseUtil;
import com.enzen.hes.service.ManufacturerService;
import com.enzen.hes.service.ZoneService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.enzen.hes.handler.response.ResponseUtil.responseUtil;

@RestController
@RequestMapping(value = "/api/v1/manufacturer")
public class ManufacturerController {
    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private ManufacturerService manufacturerService;

    @PostMapping
    public ResponseDTO<?> createManufacturer(@RequestBody Document manufacturerRequest) throws Exception {
        manufacturerService.createManufacturer(manufacturerRequest);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseDTO<?> getAllManufacturers() throws Exception {
        var result = manufacturerService.getAllManufacturers();
        return responseUtil.ok(result, ApiResponseCode.SUCCESS);
    }

    @DeleteMapping("{id}")
    public ResponseDTO<?> deleteManufacturer(@PathVariable String id) throws Exception {
        manufacturerService.deleteManufacturer(id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @PutMapping("{id}")
    public ResponseDTO<?> updateManufacturer(@RequestBody Document manufacturerRequest, @PathVariable String id) throws Exception {
        manufacturerService.updateManufacturer(manufacturerRequest, id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }
}
